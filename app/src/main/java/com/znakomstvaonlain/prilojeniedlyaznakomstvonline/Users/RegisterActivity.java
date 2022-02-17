package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private TextView registerBTN;
    private EditText email,pass, name;

    private RadioGroup radioGroup;

    private FirebaseAuth mAuth;


    private LinearLayout linerImage;
    private CircleImageView imageProfil;
    private Uri resultUri;

    private ProgressDialog loadbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerBTN = findViewById(R.id.registerBTN);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        name = findViewById(R.id.name);

        loadbar = new ProgressDialog(this);

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
                validateAccount();
            }
        });
    }

    private void validateAccount() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedId);
        String username = name.getText().toString();
        String userpass = pass.getText().toString().trim();
        String useremail = email.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, getResources().getString(R.string.Enter_your_name), Toast.LENGTH_SHORT).show();
        }else if (userpass.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.password_cannot_empty), Toast.LENGTH_SHORT).show();
        }else if (!validatePassword(userpass)){
        }else if (!validateEmailAddress(useremail)){
            Toast.makeText(this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
        }else if (radioButton.getText() == null){
            Toast.makeText(this, getResources().getString(R.string.your_gender), Toast.LENGTH_SHORT).show();
        }
        else {
            loadbar.setTitle(getResources().getString(R.string.Create_account));
            loadbar.setMessage(getResources().getString(R.string.Please_wait));
            loadbar.setCanceledOnTouchOutside(false);
            loadbar.show();

            createAccount();
        }
    }
    public static boolean validateEmailAddress(String email){
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }else {
            return false;
        }
    }

    private boolean validatePassword(String pass){
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern digitCase = Pattern.compile("[0-9]");

        if (!lowerCase.matcher(pass).find()){
            Toast.makeText(this, getResources().getString(R.string.erorr_pass1), Toast.LENGTH_SHORT).show();
            return false;
        } else
        if (!upperCase.matcher(pass).find()){
            Toast.makeText(this, getResources().getString(R.string.erorr_pass2), Toast.LENGTH_SHORT).show();
            return false;
        }else
        if (!digitCase.matcher(pass).find()){
            Toast.makeText(this, getResources().getString(R.string.erorr_pass3), Toast.LENGTH_SHORT).show();
            return false;
        }else
        if (pass.length() < 6){
            Toast.makeText(this, getResources().getString(R.string.erorr_pass4), Toast.LENGTH_SHORT).show();
            return false;
        }else
        if (pass.length() > 20){
            Toast.makeText(this, getResources().getString(R.string.erorr_pass5), Toast.LENGTH_SHORT).show();
            return false;
        }else

            return true;
    }

    public void createAccount(){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedId);

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
                                        loadbar.dismiss();
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }else {
                        loadbar.dismiss();
                        //Toast.makeText(RegisterActivity.this, R.string.sign_up_ERROR, Toast.LENGTH_LONG).show();
                        if(radioButton.getText().toString().equals("Male")) userInfo.put("profileImageUri", "defaultMale");
                        if(radioButton.getText().toString().equals("Female")) userInfo.put("profileImageUri", "defaultFemale");
                    }
                    loadbar.dismiss();
                    currentUserDb.updateChildren(userInfo);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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