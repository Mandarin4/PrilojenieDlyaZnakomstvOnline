
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter chatAdapter;
    private  RecyclerView.LayoutManager chatLayoutManager;

    private String currentUserId, matchId, chatId;

    private EditText sendEditText;
    private Button sendButton;

    DatabaseReference databaseUser, databaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchId).child("ChatId");
        databaseChat = FirebaseDatabase.getInstance().getReference().child("Chats");

        getChatId();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        chatLayoutManager = new LinearLayoutManager(ChatActivity.this);

        recyclerView.setLayoutManager(chatLayoutManager);
        chatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        recyclerView.setAdapter(chatAdapter);

        sendEditText = findViewById(R.id.message);
        sendButton = findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
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

                    if (snapshot.child("text").getValue() != null){
                        message = snapshot.child("text").getValue().toString();
                    }

                    if (snapshot.child("createdByUser").getValue() != null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }

                    if (message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if (createdByUser.equals(currentUserId)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        chatAdapter.notifyDataSetChanged();
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
}