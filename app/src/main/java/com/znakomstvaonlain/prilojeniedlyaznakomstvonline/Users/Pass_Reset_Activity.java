package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

public class Pass_Reset_Activity extends AppCompatActivity {
    private Button btn_back_reset_pass_b, reset_pass;

    private EditText reset_pass_email;

    private FirebaseAuth mAuth;
    RelativeLayout relativeLayout;
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);
        relativeLayout = findViewById(R.id.relativ);
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        reset_pass_email =  findViewById(R.id.btn_reset_pass_email_input);
        reset_pass =  findViewById(R.id.btn_reset_pass);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();


        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = reset_pass_email.getText().toString();
                if (TextUtils.isEmpty(userEmail)){
                    Toast.makeText(Pass_Reset_Activity.this, getResources().getString(R.string.enterEmailText), Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Pass_Reset_Activity.this, getResources().getString(R.string.check_email), Toast.LENGTH_LONG).show();
                                //Toast.makeText(Pass_Reset_Activity.this, "2 " + userEmail, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Pass_Reset_Activity.this, LoginActivity.class));finish();
                            }else{
                                String message = task.getException().getMessage();
                                Toast.makeText(Pass_Reset_Activity.this, " 3 " + userEmail, Toast.LENGTH_LONG).show();
                                //  Toast.makeText(Pass_Reset_Activity.this, "Error Occured: " + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void backOnClick(View view) {
        finish();
    }
}