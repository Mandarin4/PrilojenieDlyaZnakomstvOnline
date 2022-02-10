package com.znakomstvaonlain.prilojeniedlyaznakomstvonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.znakomstvaonlain.prilojeniedlyaznakomstvonline.Users.ChooselLoginOrRegistrationActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ChooselLoginOrRegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}