package com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.MainActivity;
import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.R;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
//    private TextView signInBTN;
    private EditText email,pass;

    private ProgressDialog loadbar;

    private CheckBox checkBoxRememberMe;

    RelativeLayout relativeLayout;
    LinearLayout linearLayuot;
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        relativeLayout = findViewById(R.id.relativeLayout);
        linearLayuot = findViewById(R.id.linearLayuot);
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        linearLayuot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
//        signInBTN = findViewById(R.id.signInBTN);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

        loadbar = new ProgressDialog(this);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkbox_sign_in);
        Paper.init(this);

    }

    public void signInBTNOnClick(View view){
        view.setClickable(false);
        loginUser();
    }

    private void loginUser() {
        String passString = pass.getText().toString();
        String emailString = email.getText().toString();

        if (TextUtils.isEmpty(emailString)){
            Toast.makeText(this, getResources().getString(R.string.enterEmailText), Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(passString)){
            Toast.makeText(this, getResources().getString(R.string.enterRasswordText), Toast.LENGTH_SHORT).show();
        }else {
            loadbar.setTitle(getResources().getString(R.string.loginText));
            loadbar.setMessage(getResources().getString(R.string.pleaseWaitText));
            loadbar.setCanceledOnTouchOutside(false);
            loadbar.show();

            ValidateUser(passString, emailString);
        }
    }

    private void ValidateUser(final String pass, final String email) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                        loadbar.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.sign_in_True), Toast.LENGTH_SHORT).show();
                        if(checkBoxRememberMe.isChecked()){
                            Paper.book().write(Prevalent.UserEmailKey, email);
                            Paper.book().write(Prevalent.UserPasswordKey, pass);
                        }
                        Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                }else{ loadbar.dismiss();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.sign_in_ERROR), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Системная кнопка назад
    @Override
    public void onBackPressed(){
        Intent backintent = new Intent(LoginActivity.this, ChooselLoginOrRegistrationActivity.class);
        startActivity(backintent);finish();
    }
}