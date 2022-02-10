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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.MainActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText nameField, phoneField;

    private TextView back, confirm;
    private TextView imageFon;

    private ImageView profileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;

    private String userId, name, phone, profileImageUri,userSex;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameField = (EditText) findViewById(R.id.name);
        phoneField = (EditText) findViewById(R.id.phone);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        imageFon = (TextView) findViewById(R.id.imageFon);

        back = (TextView) findViewById(R.id.back);
        confirm = (TextView) findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        try {
            userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        }catch (Exception e){
            Log.d("MyLog",e.toString());
        }

        getUserInfo();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setClickable(false);
                Toast.makeText(SettingsActivity.this, "new connections", Toast.LENGTH_LONG).show();
                saveUserInformation();
                confirm.setClickable(true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

                    if (map.get("phone") != null){
                        phone = map.get("phone").toString();
                        phoneField.setText(phone);
                    }
                    if (map.get("sex") != null){
                        userSex = map.get("sex").toString();
                    }
                    Glide.with(SettingsActivity.this).clear(profileImage);

                    if (map.get("profileImageUri") != null){
                        imageFon.setVisibility(View.GONE);
                        profileImageUri = map.get("profileImageUri").toString();
                        switch (profileImageUri){
                            case "defaultFemale":
                                Glide.with(SettingsActivity.this).load(R.drawable.default_female).into(profileImage);
                                break;
                            case "defaultMale":
                                Glide.with(SettingsActivity.this).load(R.drawable.default_male).into(profileImage);
                                break;

                            default:
                                Glide.with(SettingsActivity.this).load(profileImageUri).into(profileImage);
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

    private void saveUserInformation() {
        name = nameField.getText().toString();
        phone = phoneField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        userDatabase.updateChildren(userInfo);

        if (resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("MyLog",e.toString());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20,baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoLink = uri.toString();
                            Map userInfo = new HashMap();
                            userInfo.put("profileImageUri", photoLink);
                            userDatabase.updateChildren(userInfo);
                            finish();
                        }
                    });
                }
            });
        }else{
            finish();

        }
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