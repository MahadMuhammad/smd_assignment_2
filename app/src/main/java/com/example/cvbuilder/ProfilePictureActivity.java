package com.example.cvbuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfilePictureActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Uri imgUri;

    Intent resultIntent = new Intent();
    private CVDataModel cvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_picture);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profileImageView = findViewById(R.id.profileImageView);
        Button btnPickImage = findViewById(R.id.btnPickImage);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        // Check if we have a current image path

        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");
            Bitmap savedImage = cvData.getProfileImageBitMap();
            if (savedImage != null) {
                profileImageView.setImageBitmap(savedImage);
                imgUri = cvData.getImageUri();
            }
        } else {
            cvData = new CVDataModel();
        }

        btnPickImage.setOnClickListener(v -> {
            openGallery();
        });

        btnSave.setOnClickListener(v -> {
            if (imgUri != null) {
                cvData.setImageUri(imgUri);
                resultIntent.putExtra("cv_data", cvData);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                profileImageView.setImageBitmap(bitmap);
                cvData.setProfileImage(bitmap);
                cvData.setImageUri(imgUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            imgUri = data.getData();
//            profileImageView.setImageURI(imgUri);
//            Toast.makeText(this, "Image selected" + imgUri.toString(), Toast.LENGTH_SHORT).show();
//            cvData.setImageUri(imgUri);
        }
    }
}