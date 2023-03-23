package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText input_email, input_password;
    Button register_btn;
    ImageView show_hide_password;
    dbHelper myDB;
    int val = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register_btn=findViewById(R.id.register_btn);
        input_email=findViewById(R.id.input_email);
        input_password=findViewById(R.id.input_password);
        myDB = new dbHelper(LoginActivity.this);
        show_hide_password = findViewById(R.id.show_hide_password);
        show_hide_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(val ==1){
                    input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show_hide_password.setImageResource(R.drawable.openeye);
                    val = 0;
                }
                else if(val ==0){
                    input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show_hide_password.setImageResource(R.drawable.closedeye);
                    val = 1;
                }
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString().trim();
                String password = input_password.getText().toString().trim();

                if (input_email.getText().toString() == null || input_email.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please insert email", Toast.LENGTH_SHORT).show();
                }
                else if (input_password.getText().toString() == null || input_password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please insert password", Toast.LENGTH_SHORT).show();
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Please insert email properly", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (myDB.isEmailAlreadyExist(email)) {
                        if (myDB.isPasswordCorrect(email,password)){
                            Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "Incorrect Email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void back(View view){
        startActivity(new Intent(LoginActivity.this,WelcomeActivity.class));
    }
    public void register(View view){
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }
    public void mainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}