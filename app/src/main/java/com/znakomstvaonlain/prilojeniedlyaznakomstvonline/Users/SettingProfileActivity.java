package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SettingProfileActivity extends AppCompatActivity {
    private TextView nameField, userSexText;


    private CircleImageView profileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;

    private String userId, name, profileImageUri,userSex;

    private Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
        nameField = (TextView) findViewById(R.id.name);

        profileImage =  findViewById(R.id.profileImage);
        userSexText =  findViewById(R.id.userSexText);


        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        try {
            userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        }catch (Exception e){
            Log.d("MyLog",e.toString());
        }
        getUserInfo();
    }

    private void getUserInfo() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object> ) snapshot.getValue();
                    if (map.get("name") != null){
                        name = map.get("name").toString();
                        nameField.setText(name);
                    }

                    if (map.get("sex") != null){
                        userSex = map.get("sex").toString();
                        userSexText.setText(userSex);
                    }
                    Glide.with(SettingProfileActivity.this).clear(profileImage);

                    if (map.get("profileImageUri") != null){
                        profileImageUri = map.get("profileImageUri").toString();
                        switch (profileImageUri){
                            case "defaultFemale":
                                Glide.with(SettingProfileActivity.this).load(R.drawable.default_female).into(profileImage);
                                break;
                            case "defaultMale":
                                Glide.with(SettingProfileActivity.this).load(R.drawable.default_male).into(profileImage);
                                break;

                            default:
                                Glide.with(SettingProfileActivity.this).load(profileImageUri).into(profileImage);
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



    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, ChooselLoginOrRegistrationActivity.class);
        Paper.book().destroy();
        startActivity(intent);
        finish();
    }
    public void backOnClick(View view) {
        finish();
    }
    public void settingOnClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profileImage.setImageURI(resultUri);
        }
    }
}