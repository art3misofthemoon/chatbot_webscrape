package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatbot.databinding.SignupBinding;
public class signup extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView loginbut;
    EditText rg_username, rg_email , rg_password, rg_repassword;
    Button rg_signup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        databaseHelper = new DatabaseHelper(this);
        getSupportActionBar().hide();
        loginbut = findViewById(R.id.loginbut);
        rg_username = findViewById(R.id.rgusername);
        rg_email = findViewById(R.id.rgemail);
        rg_password = findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepassword);
        rg_signup = findViewById(R.id.signupbutton);

        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = rg_username.getText().toString();
                String email = rg_email.getText().toString();
                String password = rg_password.getText().toString();
                String confirmPassword = rg_repassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(signup.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    Toast.makeText(signup.this, "Type A Valid Email Here", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(signup.this, "Password Must Be 6 Characters Or More", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(signup.this, "The Password Doesn't Match", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserEmail = databaseHelper.checkEmail(email);
                    if(!checkUserEmail){
                        Boolean insert = databaseHelper.insertData(name,email, password);
                        if(insert){
                            Toast.makeText(signup.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(signup.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(signup.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
