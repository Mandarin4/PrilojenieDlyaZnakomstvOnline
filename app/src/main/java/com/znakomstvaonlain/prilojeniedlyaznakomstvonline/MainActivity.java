package com.znakomstvaonlain.prilojeniedlyaznakomstvonline;


import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Cards.arrayAdapter;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Cards.cards;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Mathes.MathesActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.ChooselLoginOrRegistrationActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.SettingProfileActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private long backPressedtimer;
    private Toast backToast;

    private cards cards_data[];
    private ArrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference usersDb;
//    private DatabaseReference usersDb2;

    ListView listView;
    List<cards> rowItems;

    private String userSex;
    private String oppositeUserSex;

//    private NotificationManager notificationManager;
//    private static final int NOTIFY_ID = 1;
//    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        checkUserSex();

       /* usersDb2 = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("matches");
        usersDb2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String nameProfile = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.getKey()).child("name").getValue().toString();
                notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setAutoCancel(false)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pendingIntent)
                                .setContentTitle("У вас новое совпадение")
                                .setContentText(nameProfile)
                                .setPriority(PRIORITY_HIGH);
                createChannelIfNeeded(notificationManager);
                notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
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
        });*/

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.not_like),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.like),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.new_connections), Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void checkUserSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("sex").getValue() != null){
                        userSex = snapshot.child("sex").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                        getOppositeSexUsers();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getOppositeSexUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUId) && !snapshot.child("connections").child("yeps").hasChild(currentUId) && snapshot.child("sex").getValue().toString().equals(oppositeUserSex)){
                    String profileImageUri = null;
                    if (userSex.equals("Male")) profileImageUri = "defaultMale";
                    if (userSex.equals("Female")) profileImageUri = "defaultFemale";
                    if (!snapshot.child("profileImageUri").getValue().equals("default")){
                        profileImageUri = snapshot.child("profileImageUri").getValue().toString();
                    }
                    cards item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString(),profileImageUri );
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

//    public void logoutUser(View view) {
//        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder notificationBuilder =
//        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setAutoCancel(false)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setWhen(System.currentTimeMillis())
//                .setContentIntent(pendingIntent)
//                .setContentTitle("Заголовок")
//                .setContentText("Какой то текст.............")
//                .setPriority(PRIORITY_HIGH);
//        createChannelIfNeeded(notificationManager);
//        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
//    }

//    public static void createChannelIfNeeded(NotificationManager manager) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(notificationChannel);
//        }
//    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingProfileActivity.class);
        startActivity(intent);
        return;
    }

    public void goToMathes(View view) {
        Intent intent = new Intent(this, MathesActivity.class);
        startActivity(intent);
        return;
    }


    // Системная кнопка назад
    @Override
    public void onBackPressed(){
        if (backPressedtimer + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), getResources().getString(R.string.clickAgainToExit),  Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedtimer = System.currentTimeMillis();
    }
}