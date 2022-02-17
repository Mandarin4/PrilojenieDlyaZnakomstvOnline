package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Mathes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.MainActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.ChooselLoginOrRegistrationActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MathesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mathesAdapter;
    private  RecyclerView.LayoutManager mathesLayoutManager;

    private String currentUserId;

    private ImageView imageBTNBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathes);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        imageBTNBack = findViewById(R.id.imageBTNBack);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        mathesLayoutManager = new LinearLayoutManager(MathesActivity.this);
        recyclerView.setLayoutManager(mathesLayoutManager);
        mathesAdapter = new MathesAdapter(getDataSetMathes(), MathesActivity.this);
        recyclerView.setAdapter(mathesAdapter);

        getUserMathId();




    }

    public void imageBTNBackOnClick(View view){
        finish();
    }


    private void getUserMathId() {
        DatabaseReference matchDB = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches");
        matchDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userId = snapshot.getKey();
                    String userName = "";
                    String userProfileImageUri = "";

                    if (snapshot.child("name").getValue() != null){
                        userName = snapshot.child("name").getValue().toString();
                    }

                    if (snapshot.child("profileImageUri").getValue() != null){
                        userProfileImageUri = snapshot.child("profileImageUri").getValue().toString();
                    }


                    MathesObject obj = new MathesObject(userId, userName, userProfileImageUri);
                    resultsMathes.add(obj);
                    mathesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private ArrayList<MathesObject> resultsMathes = new ArrayList<MathesObject>();
    private List<MathesObject> getDataSetMathes() {
        return resultsMathes;
    }


    // Системная кнопка назад
    @Override
    public void onBackPressed(){
        finish();
    }
}