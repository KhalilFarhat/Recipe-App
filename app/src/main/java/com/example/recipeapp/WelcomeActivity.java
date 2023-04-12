package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @SuppressLint("AppCompatMethod")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        if(sp.getBoolean("IsSignedIn", false))
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
    }

    public void register(View view){
        startActivity(new Intent(WelcomeActivity.this,RegistrationActivity.class));
    }
    public void signin(View view){
        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
    }
}