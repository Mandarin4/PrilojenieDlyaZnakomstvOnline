package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.MainActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import io.paperdb.Paper;

public class ChooselLoginOrRegistrationActivity extends AppCompatActivity {
    private long backPressedtimer;
    private Toast backToast;


    private ProgressDialog loadbar;
    DatabaseReference rootRef , demoRef,demoRef2;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosel_login_or_registration);

        mAuth = FirebaseAuth.getInstance();

        loadbar = new ProgressDialog(this);

        Paper.init(this);

        String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserEmailKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey)){
                ValidateUser(UserEmailKey, UserPasswordKey);
                loadbar.setTitle(getResources().getString(R.string.loginText));
                loadbar.setMessage(getResources().getString(R.string.pleaseWaitText));
                loadbar.setCanceledOnTouchOutside(false);
                loadbar.show();
            }
        }


    }

    private void ValidateUser(final String email, final String pass) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ChooselLoginOrRegistrationActivity.this, getResources().getString(R.string.sign_in_True), Toast.LENGTH_SHORT).show();
                            Intent Intent = new Intent(ChooselLoginOrRegistrationActivity.this, MainActivity.class);
                            startActivity(Intent);
                            finish();
                        }else Toast.makeText(ChooselLoginOrRegistrationActivity.this, getResources().getString(R.string.sign_in_ERROR), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String removeChar(String s, char c) {
        StringBuilder r = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) {
                r.append(cur);
            }
        }
        return r.toString();
    }

    public void loginOnClick(View view){
        view.setClickable(false);
        Intent intent = new Intent(ChooselLoginOrRegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void registerOnClick(View view){
        view.setClickable(false);
        Intent intent = new Intent(ChooselLoginOrRegistrationActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
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