package com.example.cvbuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PROFILE_PIC = 1001;
    private static final int REQUEST_CODE_PERSONAL_DETAILS = 1002;
    private static final int REQUEST_CODE_SUMMARY = 1003;
    private static final int REQUEST_CODE_EDUCATION = 1004;
    private static final int REQUEST_CODE_EXPERIENCE = 1005;
    private static final int REQUEST_CODE_CERTIFICATIONS = 1006;
    private static final int REQUEST_CODE_REFERENCES = 1007;

    private CVDataModel cvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize CVDataModel
        cvData = new CVDataModel();

        // Set click listeners for all buttons
        findViewById(R.id.btnProfilePicture).setOnClickListener(this::onClick);
        findViewById(R.id.btnPersonalDetails).setOnClickListener(this::onClick);
        findViewById(R.id.btnSummary).setOnClickListener(this::onClick);
        findViewById(R.id.btnEducation).setOnClickListener(this::onClick);
        findViewById(R.id.btnExperience).setOnClickListener(this::onClick);
        findViewById(R.id.btnCertifications).setOnClickListener(this::onClick);
        findViewById(R.id.btnReferences).setOnClickListener(this::onClick);
        findViewById(R.id.btnPreview).setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnProfilePicture) {
            Intent intent = new Intent(this, ProfilePictureActivity.class);
            intent.putExtra("cv_data", cvData);
//            if (cvData.getProfilePicturePath() != null) {
//                intent.putExtra("current_image_path", cvData.getProfilePicturePath());
//                Toast.makeText(this, "Current image path: " + cvData.getProfilePicturePath(), Toast.LENGTH_SHORT).show();
//            }
            startActivityForResult(intent, REQUEST_CODE_PROFILE_PIC);
        }
        else if (id == R.id.btnPersonalDetails) {
            Intent intent = new Intent(this, PersonalDetailsActivity.class);
            intent.putExtra("cv_data", cvData);
            startActivityForResult(intent, REQUEST_CODE_PERSONAL_DETAILS);
        }
        else if (id == R.id.btnSummary) {
            Intent intent = new Intent(this, SummaryActivity.class);
            intent.putExtra("cv_data", cvData);
            startActivityForResult(intent, REQUEST_CODE_SUMMARY);
        }
        else if (id == R.id.btnEducation) {
            Intent intent = new Intent(this, EducationActivity.class);
            intent.putExtra("cv_data", cvData);
            startActivityForResult(intent, REQUEST_CODE_EDUCATION);
        }
        else if (id == R.id.btnExperience) {
            Intent intent = new Intent(this, ExperienceActivity.class);
            intent.putExtra("cv_data", cvData);
            startActivityForResult(intent, REQUEST_CODE_EXPERIENCE);
        }
        else if (id == R.id.btnCertifications) {
            Intent intent = new Intent(this, CertificationsActivity.class);
            intent.putExtra("cv_data", cvData);
            startActivityForResult(intent, REQUEST_CODE_CERTIFICATIONS);
        }
        else if (id == R.id.btnReferences) {
            Intent intent = new Intent(this, ReferencesActivity.class);
            intent.putExtra("cv_data", cvData);
            startActivityForResult(intent, REQUEST_CODE_REFERENCES);
        }
        else if (id == R.id.btnPreview) {
            if (isProfileComplete()) {
                Intent intent = new Intent(this, FinalActivity.class);
                intent.putExtra("cv_data", cvData);
//                intent.putExtra("name", cvData.getName());
//                intent.putExtra("email", cvData.getEmail());
//                intent.putExtra("phone", cvData.getPhone());
//                intent.putExtra("address", cvData.getAddress());
//                intent.putExtra("profile_picture", cvData.getImageUri());
//                intent.putExtra("summary", cvData.getSummary());
//                intent.putExtra("education_list", cvData.getEducationList().toString());
//                intent.putExtra("experience_list", cvData.getExperienceList().toString());
//                intent.putExtra("certifications_list", cvData.getCertificationsList().toString());
//                intent.putExtra("references_list", cvData.getReferences().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please complete your profile before preview", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_PROFILE_PIC:
//                    cvData.setImageUriString(data.getStringExtra("cv_data"));
//                    break;
                case REQUEST_CODE_PERSONAL_DETAILS:

                case REQUEST_CODE_SUMMARY:
                case REQUEST_CODE_EDUCATION:
                case REQUEST_CODE_EXPERIENCE:
                case REQUEST_CODE_CERTIFICATIONS:
                case REQUEST_CODE_REFERENCES:
                    cvData = (CVDataModel) data.getSerializableExtra("cv_data");
                    break;
            }
        }
//        Toast.makeText(this, "Profile updated" + cvData.getImageUri().toString() , Toast.LENGTH_SHORT).show();

    }

    private boolean isProfileComplete() {
        return true;
        // Basic validation to check if essential info is provided
//        return cvData.getName() != null && !cvData.getName().isEmpty() &&
//                cvData.getEmail() != null && !cvData.getEmail().isEmpty() &&
//                cvData.getProfilePicturePath() != null && !cvData.getProfilePicturePath().isEmpty() &&
//                cvData.getSummary() != null && !cvData.getSummary().isEmpty() &&
//                !cvData.getEducationList().isEmpty();
    }
}