
package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.SettingsActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    private RelativeLayout activity_chat;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter chatAdapter;
    private  RecyclerView.LayoutManager chatLayoutManager;

    private String currentUserId, matchId, chatId;

    private EditText sendEditText;
    private Button sendButton;


    DatabaseReference databaseUser, databaseChat;

    CircleImageView imageIconProfile;
    TextView nameProfile;
    private DatabaseReference userDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity_chat = findViewById(R.id.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchId).child("ChatId");
        databaseChat = FirebaseDatabase.getInstance().getReference().child("Chats");

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(matchId);

        getChatId();

        sendEditText = findViewById(R.id.message);
        sendButton = findViewById(R.id.send);

        imageIconProfile = findViewById(R.id.imageIconProfile);
        nameProfile = findViewById(R.id.nameProfile);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        chatLayoutManager = new LinearLayoutManager(ChatActivity.this);

        recyclerView.setLayoutManager(chatLayoutManager);
        chatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        recyclerView.setAdapter(chatAdapter);


        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object> ) snapshot.getValue();
                    if (map.get("name") != null){

                        nameProfile.setText(map.get("name").toString());
                    }

                    Glide.with(ChatActivity.this).clear(imageIconProfile);

                    if (map.get("profileImageUri") != null){
                        String profileImageUri = map.get("profileImageUri").toString();
                        switch (profileImageUri){
                            case "defaultFemale":
                                Glide.with(ChatActivity.this).load(R.drawable.default_female).into(imageIconProfile);
                                break;
                            case "defaultMale":
                                Glide.with(ChatActivity.this).load(R.drawable.default_male).into(imageIconProfile);
                                break;

                            default:
                                Glide.with(ChatActivity.this).load(profileImageUri).into(imageIconProfile);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendMessage() {
        String sendMessageText = sendEditText.getText().toString();

        if (!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = databaseChat.push();

            Map newMasssage = new HashMap();
            newMasssage.put("createdByUser", currentUserId);
            newMasssage.put("text", sendMessageText);
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
            String dateText = dateFormat.format(currentDate);
            DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String timeText = timeFormat.format(currentDate);
            newMasssage.put("dateTime", dateText + " " + timeText);

            newMessageDb.setValue(newMasssage);
        }
        sendEditText.setText(null);
    }


    private void getChatId(){
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatId = snapshot.getValue().toString();
                    databaseChat = databaseChat.child(chatId);
                    getChatMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessage() {
        databaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String message = null;
                    String createdByUser = null;
                    String dateTime = null;

                    if (snapshot.child("text").getValue() != null){
                        message = snapshot.child("text").getValue().toString();
                    }

                    if (snapshot.child("createdByUser").getValue() != null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }

                    if (snapshot.child("dateTime").getValue() != null){
                        dateTime = snapshot.child("dateTime").getValue().toString();
                    }

                    if (message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if (createdByUser.equals(currentUserId)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message, dateTime, currentUserBoolean);
                        resultsChat.add(newMessage);
                        chatAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(resultsChat.size() - 1);
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }

    public void imageBTNBackOnClick(View view){
        finish();
    }
    // Системная кнопка назад
    @Override
    public void onBackPressed(){
        finish();
    }
}