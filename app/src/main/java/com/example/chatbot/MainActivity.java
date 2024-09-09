package com.example.chatbot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Objects;
public class MainActivity extends AppCompatActivity {
    private Context context;
    DatabaseHelper databaseHelper;
    EditText usernameEditText, passwordEditText;
    TextView signup;
    RadioGroup userTypeRadioGroup;
    Button loginButton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        signup=findViewById(R.id.textView5);
        loginButton=findViewById(R.id.loginButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,signup.class);
                startActivity(intent);
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if ((TextUtils.isEmpty(email))){
                    Toast.makeText(MainActivity.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Enter The Password", Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)){
                    Toast.makeText(MainActivity.this, "Give Proper Email Address", Toast.LENGTH_SHORT).show();
                }else if (passwordEditText.length()<6){
                    Toast.makeText(MainActivity.this, "Password Needs To Be Longer Then Six Characters", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    if(checkCredentials){
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadeout);
                        view.startAnimation(fadeOut);
                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {}

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();  // Optional: Close the LoginActivity if not needed anymore
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {}
                        });
                        Intent intent  = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("user_email", email);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        context = this;
        scheduleSkinTipBroadcast();
    }
    private void scheduleSkinTipBroadcast() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, SkinBroadcastReceiver.class);
        intent.setAction("SKIN_TIP_BROADCAST");

        PendingIntent pendingIntent;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            // Use FLAG_IMMUTABLE for Android S (version 31) and above
            pendingIntent = PendingIntent.getBroadcast(
                    this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            // Use FLAG_UPDATE_CURRENT for versions below Android S
            pendingIntent = PendingIntent.getBroadcast(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        // Schedule a daily broadcast for skin tips (adjust the timing as needed)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9); // 9 AM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, // Repeat daily
                pendingIntent
        );
    }

}
