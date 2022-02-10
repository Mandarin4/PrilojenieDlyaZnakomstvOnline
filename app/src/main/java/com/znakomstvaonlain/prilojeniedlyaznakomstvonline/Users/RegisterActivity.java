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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.MainActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView registerBTN;
    private EditText email,pass, name;

    private RadioGroup radioGroup;

    private FirebaseAuth mAuth;


    private LinearLayout linerImage;
    private ImageView imageProfil;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerBTN = findViewById(R.id.registerBTN);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        name = findViewById(R.id.name);

        linerImage = findViewById(R.id.linerImage);
        imageProfil = findViewById(R.id.imageProfil);

        linerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        radioGroup = findViewById(R.id.radioGroup);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = findViewById(selectedId);

                if (radioButton.getText() == null){
                    return;
                }

                final String emailString = email.getText().toString();
                final String passString = pass.getText().toString();
                final String nameString = name.getText().toString();
                mAuth.createUserWithEmailAndPassword(emailString,passString).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, R.string.sign_up_ERROR, Toast.LENGTH_LONG).show();
                        }
                        else {
                            String userID = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                            Map userInfo = new HashMap();
                            userInfo.put("name", nameString);
                            userInfo.put("sex", radioButton.getText().toString());

                            if (resultUri != null){
                                StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userID);
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
                                                userInfo.put("profileImageUri", photoLink);
                                                currentUserDb.updateChildren(userInfo);
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }else {
                                if(radioButton.getText().toString().equals("Male")) userInfo.put("profileImageUri", "defaultMale");
                                if(radioButton.getText().toString().equals("Female")) userInfo.put("profileImageUri", "defaultFemale");
                            }

                            currentUserDb.updateChildren(userInfo);
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            imageProfil.setImageURI(resultUri);
        }
    }
}