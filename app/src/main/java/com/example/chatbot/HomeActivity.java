package com.example.chatbot;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    ImageView profile;
    Button btnStart;
    Button btnSelectImage;
    TextView title, welcomeText, funfact;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ContentResolver contentResolver;
    private ImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        findViewById(R.id.background).startAnimation(fadeIn);
        imageView = findViewById(R.id.background);
        btnSelectImage =(Button) findViewById(R.id.btnStart1);
        Button btnUpload = findViewById(R.id.btnStart2);
        String userEmail = getIntent().getStringExtra("user_email");
        profile = (ImageView) findViewById(R.id.profile);
        btnStart = (Button) findViewById(R.id.btnStart);
        title = (TextView) findViewById(R.id.title);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        funfact = (TextView) findViewById(R.id.funfact);
        contentResolver = getContentResolver();

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    // Save the image URI using the content provider
                    saveImageToProvider(imageUri);
                    Toast.makeText(HomeActivity.this, "Image uploaded and saved!", Toast.LENGTH_SHORT).show();

                    // Retrieve and display the last uploaded image
                    displayLastImage();
                } else {
                    Toast.makeText(HomeActivity.this, "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                imageView.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToProvider(Uri imageUri) {
        ContentValues values = new ContentValues();
        values.put(ImageContract.ImageEntry.COLUMN_IMAGE_URI, imageUri.toString());

        contentResolver.insert(ImageContract.ImageEntry.CONTENT_URI, values);
    }

    private void displayLastImage() {
        Cursor cursor = contentResolver.query(
                ImageContract.ImageEntry.CONTENT_URI,
                null,
                null,
                null,
                ImageContract.ImageEntry._ID + " DESC LIMIT 1"
        );

        if (cursor != null && cursor.moveToFirst()) {
            String lastImageUri = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.ImageEntry.COLUMN_IMAGE_URI));
            Uri lastUploadedUri = Uri.parse(lastImageUri);

            try {
                imageView.setImageURI(lastUploadedUri);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
