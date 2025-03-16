package com.example.cvbuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class FinalActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private TextView tvName;
    private TextView tvContactInfo;
    private TextView tvSummary;

    private LinearLayout educationContainer;
    private LinearLayout experienceContainer;
    private LinearLayout certificationsContainer;
    private LinearLayout referencesContainer;

    private Button btnShareCV;
    private CVDataModel cvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_final);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        tvName = findViewById(R.id.textViewName);
        tvContactInfo = findViewById(R.id.textViewContactInfo);
        tvSummary = findViewById(R.id.textViewSummary);

        educationContainer = findViewById(R.id.educationContainer);
        experienceContainer = findViewById(R.id.experienceContainer);
        certificationsContainer = findViewById(R.id.certificationsContainer);
        referencesContainer = findViewById(R.id.referencesContainer);

        btnShareCV = findViewById(R.id.btnShareCV);

        // Get CV data from intent
        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");
            populateCVData();
        } else {
            Toast.makeText(this, "Error: No CV data found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup share button
        btnShareCV.setOnClickListener(v -> {
            shareCV();
        });
    }

    private void populateCVData() {
        // Set profile picture
        Bitmap profileImage = cvData.getProfileImageBitMap();
        if (profileImage != null) {
            profileImageView.setImageBitmap(profileImage);
        }

        // Set name
        tvName.setText(cvData.getName());

        // Set contact info
        StringBuilder contactInfo = new StringBuilder();
        if (cvData.getEmail() != null) contactInfo.append(cvData.getEmail()).append("\n");
        if (cvData.getPhone() != null) contactInfo.append(cvData.getPhone()).append("\n");
        if (cvData.getAddress() != null) contactInfo.append(cvData.getAddress()).append("\n");
        if (cvData.getDob() != null) contactInfo.append("DOB: ").append(cvData.getDob());
        tvContactInfo.setText(contactInfo.toString());

        // Set summary
        tvSummary.setText(cvData.getSummary());

        // Add education items
        populateEducation(cvData.getEducationList());

        // Add experience items
        populateExperience(cvData.getExperienceList());

        // Add certification items
        populateCertifications(cvData.getCertifications());

        // Add reference items
        populateReferences(cvData.getReferences());
    }

    private void populateEducation(List<CVDataModel.Education> educationList) {
        if (educationList == null || educationList.isEmpty()) {
            CardView cardView = findViewById(R.id.cardViewEducation);
            cardView.setVisibility(View.GONE);
            return;
        }

        for (CVDataModel.Education education : educationList) {
            View view = getLayoutInflater().inflate(R.layout.item_preview_education, educationContainer, false);

            TextView tvDegree = view.findViewById(R.id.textViewDegree);
            TextView tvInstitution = view.findViewById(R.id.textViewInstitution);
            TextView tvYear = view.findViewById(R.id.textViewYear);
            TextView tvGrade = view.findViewById(R.id.textViewGrade);

            tvDegree.setText(education.getDegree());
            tvInstitution.setText(education.getInstitution());
            tvYear.setText(education.getYear());

            if (education.getGrade() != null && !education.getGrade().isEmpty()) {
                tvGrade.setText("Grade: " + education.getGrade());
            } else {
                tvGrade.setVisibility(View.GONE);
            }

            educationContainer.addView(view);
        }
    }

    private void populateExperience(List<CVDataModel.Experience> experienceList) {
        if (experienceList == null || experienceList.isEmpty()) {
            CardView cardView = findViewById(R.id.cardViewExperience);
            cardView.setVisibility(View.GONE);
            return;
        }

        for (CVDataModel.Experience experience : experienceList) {
            View view = getLayoutInflater().inflate(R.layout.item_preview_experience, experienceContainer, false);

            TextView tvPosition = view.findViewById(R.id.textViewPosition);
            TextView tvCompany = view.findViewById(R.id.textViewCompany);
            TextView tvDuration = view.findViewById(R.id.textViewDuration);
            TextView tvDescription = view.findViewById(R.id.textViewDescription);

            tvPosition.setText(experience.getPosition());
            tvCompany.setText(experience.getCompany());
            tvDuration.setText(experience.getDuration());

            if (experience.getDescription() != null && !experience.getDescription().isEmpty()) {
                tvDescription.setText(experience.getDescription());
            } else {
                tvDescription.setVisibility(View.GONE);
            }

            experienceContainer.addView(view);
        }
    }

    private void populateCertifications(List<String> certificationsList) {
        if (certificationsList == null || certificationsList.isEmpty()) {
            CardView cardView = findViewById(R.id.cardViewCertifications);
            cardView.setVisibility(View.GONE);
            return;
        }

        for (String certification : certificationsList) {
            View view = getLayoutInflater().inflate(R.layout.item_preview_certification, certificationsContainer, false);

            TextView tvCertification = view.findViewById(R.id.textViewCertification);
            tvCertification.setText("• " + certification);

            certificationsContainer.addView(view);
        }
    }

    private void populateReferences(List<CVDataModel.Reference> referencesList) {
        if (referencesList == null || referencesList.isEmpty()) {
            CardView cardView = findViewById(R.id.cardViewReferences);
            cardView.setVisibility(View.GONE);
            return;
        }

        for (CVDataModel.Reference reference : referencesList) {
            View view = getLayoutInflater().inflate(R.layout.item_preview_reference, referencesContainer, false);

            TextView tvName = view.findViewById(R.id.textViewName);
            TextView tvPosition = view.findViewById(R.id.textViewPosition);
            TextView tvContact = view.findViewById(R.id.textViewContact);

            tvName.setText(reference.getName());
            tvPosition.setText(reference.getPosition());
            tvContact.setText(reference.getContact());

            referencesContainer.addView(view);
        }
    }

    private void shareCV() {
        // Create CV content in a structured format
        StringBuilder cvContent = new StringBuilder();

        // Header with name
        cvContent.append("# CURRICULUM VITAE\n\n");
        cvContent.append("## ").append(cvData.getName()).append("\n\n");

        // Contact Information
        cvContent.append("### Contact Information\n");
        if (cvData.getEmail() != null) cvContent.append("Email: ").append(cvData.getEmail()).append("\n");
        if (cvData.getPhone() != null) cvContent.append("Phone: ").append(cvData.getPhone()).append("\n");
        if (cvData.getAddress() != null) cvContent.append("Address: ").append(cvData.getAddress()).append("\n");
        if (cvData.getDob() != null) cvContent.append("Date of Birth: ").append(cvData.getDob()).append("\n");
        cvContent.append("\n");

        // Summary
        if (cvData.getSummary() != null && !cvData.getSummary().isEmpty()) {
            cvContent.append("### Professional Summary\n");
            cvContent.append(cvData.getSummary()).append("\n\n");
        }

        // Education
        if (cvData.getEducationList() != null && !cvData.getEducationList().isEmpty()) {
            cvContent.append("### Education\n");
            for (CVDataModel.Education education : cvData.getEducationList()) {
                cvContent.append("- **").append(education.getDegree()).append("**\n");
                cvContent.append("  ").append(education.getInstitution()).append("\n");
                cvContent.append("  ").append(education.getYear());
                if (education.getGrade() != null && !education.getGrade().isEmpty()) {
                    cvContent.append(" | Grade: ").append(education.getGrade());
                }
                cvContent.append("\n\n");
            }
        }

        // Experience
        if (cvData.getExperienceList() != null && !cvData.getExperienceList().isEmpty()) {
            cvContent.append("### Work Experience\n");
            for (CVDataModel.Experience experience : cvData.getExperienceList()) {
                cvContent.append("- **").append(experience.getPosition()).append("**\n");
                cvContent.append("  ").append(experience.getCompany()).append("\n");
                cvContent.append("  ").append(experience.getDuration()).append("\n");
                if (experience.getDescription() != null && !experience.getDescription().isEmpty()) {
                    cvContent.append("  ").append(experience.getDescription()).append("\n");
                }
                cvContent.append("\n");
            }
        }

        // Certifications
        if (cvData.getCertifications() != null && !cvData.getCertifications().isEmpty()) {
            cvContent.append("### Certifications\n");
            for (String certification : cvData.getCertifications()) {
                cvContent.append("- ").append(certification).append("\n");
            }
            cvContent.append("\n");
        }

        // References
        if (cvData.getReferences() != null && !cvData.getReferences().isEmpty()) {
            cvContent.append("### References\n");
            for (CVDataModel.Reference reference : cvData.getReferences()) {
                cvContent.append("- **").append(reference.getName()).append("**\n");
                cvContent.append("  ").append(reference.getPosition()).append("\n");
                cvContent.append("  ").append(reference.getContact()).append("\n\n");
            }
        }

        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CV of " + cvData.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, cvContent.toString());

        // Create chooser and start the activity
        startActivity(Intent.createChooser(shareIntent, "Share CV via"));
    }
}