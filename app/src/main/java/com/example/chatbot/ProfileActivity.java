package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileActivity extends AppCompatActivity {
    TextView emailtextview;

    private ContentResolver contentResolver;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        logout=findViewById(R.id.button2);
        String userEmail = getIntent().getStringExtra("user_email");
        emailtextview=findViewById(R.id.textView2);
        emailtextview.setText(userEmail);

        if (findViewById(R.id.fragment1)!=null)
        {
            fragmenta frag=new fragmenta();
            Bundle bundle = new Bundle();
            bundle.putString("user_email", userEmail);
            frag.setArguments(bundle);
            FragmentManager manager=getSupportFragmentManager();
            FragmentTransaction transaction= manager.beginTransaction();
            transaction.replace(R.id.fragment1,frag);
            transaction.commit();
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}