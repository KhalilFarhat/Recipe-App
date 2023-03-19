package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {

    EditText input_name, input_email, input_password;
    Button register_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register_btn=findViewById(R.id.register_btn);
        input_name=findViewById(R.id.input_name);
        input_email=findViewById(R.id.input_email);
        input_password=findViewById(R.id.input_password);

    }

    public void back(View view){
        startActivity(new Intent(RegistrationActivity.this,WelcomeActivity.class));
    }
    public void signin(View view){
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }

}