package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    EditText input_name, input_email, input_password;
    Button register_btn;
    ImageView show_hide_password;
    dbHelper myDB;
    int val = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register_btn=findViewById(R.id.register_btn);
        input_name=findViewById(R.id.input_name);
        input_email=findViewById(R.id.input_email);
        input_password=findViewById(R.id.input_password);
        show_hide_password=findViewById(R.id.show_hide_password);
        myDB = new dbHelper(RegistrationActivity.this);

        show_hide_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(val ==1){
                    //input_password.set("false");
                    input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show_hide_password.setImageResource(R.drawable.openeye);
                    val = 0;
                }
                else if(val ==0){
                    //input_password.setPassword("true");
                    input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show_hide_password.setImageResource(R.drawable.closedeye);
                    val = 1;
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = input_name.getText().toString().trim();
                String email = input_email.getText().toString().trim();
                String password = input_password.getText().toString().trim();

                if(input_name.getText().toString() == null || input_name.getText().toString().isEmpty() || name.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Please insert name", Toast.LENGTH_SHORT).show();
                }
                else if (input_email.getText().toString() == null || input_email.getText().toString().isEmpty() || name.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Please insert email", Toast.LENGTH_SHORT).show();
                }
                else if (input_password.getText().toString() == null || input_password.getText().toString().isEmpty() || name.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Please insert password", Toast.LENGTH_SHORT).show();
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegistrationActivity.this, "Please insert email properly", Toast.LENGTH_SHORT).show();
                }else{
                    if (myDB.isEmailAlreadyExist(email)) {
                        Toast.makeText(RegistrationActivity.this, "This email is already used!", Toast.LENGTH_SHORT).show();
                    }else {
                        myDB.addUser(name, email, password);
                        String emptyText = "";
                        input_name.setText(emptyText);
                        input_email.setText(emptyText);
                        input_password.setText(emptyText);
                        Intent intent= new Intent(RegistrationActivity.this, MainActivity.class);
                        intent.putExtra("name", name);
                    }
                }
            }
        });

    }

    public void back(View view){
        startActivity(new Intent(RegistrationActivity.this,WelcomeActivity.class));
    }
    public void signin(View view){
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }

}