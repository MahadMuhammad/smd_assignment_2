package com.example.cvbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExperienceActivity extends AppCompatActivity {
    private LinearLayout experienceContainer;
    private FloatingActionButton fabAddExperience;
    private Button btnSave;
    private Button btnCancel;

    private CVDataModel cvData;
    private List<CVDataModel.Experience> tempExperienceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize views
        experienceContainer = findViewById(R.id.experienceContainer);
        fabAddExperience = findViewById(R.id.fabAddExperience);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        tempExperienceList = new ArrayList<>();

        // Get data from intent
        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");

            // Populate fields with existing data
            if (cvData.getExperienceList() != null && !cvData.getExperienceList().isEmpty()) {
                tempExperienceList.addAll(cvData.getExperienceList());
                updateExperienceList();
            }
        } else {
            cvData = new CVDataModel();
        }

        // Setup add experience button
        fabAddExperience.setOnClickListener(v -> {
            showAddExperienceDialog();
        });

        // Setup save button
        btnSave.setOnClickListener(v -> {
            // It's okay to have no experience (for students/fresh graduates)
            // Clear existing experience list and add all from temp list
            cvData.getExperienceList().clear();
            cvData.getExperienceList().addAll(tempExperienceList);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("cv_data", cvData);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Setup cancel button
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void showAddExperienceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_experience, null);
        builder.setView(dialogView);

        EditText etPosition = dialogView.findViewById(R.id.editTextPosition);
        EditText etCompany = dialogView.findViewById(R.id.editTextCompany);
        EditText etDuration = dialogView.findViewById(R.id.editTextDuration);
        EditText etDescription = dialogView.findViewById(R.id.editTextDescription);

        builder.setTitle("Add Experience")
                .setPositiveButton("Add", (dialog, id) -> {
                    if (etPosition.getText().toString().isEmpty() ||
                            etCompany.getText().toString().isEmpty() ||
                            etDuration.getText().toString().isEmpty()) {

                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create new Experience object
                    CVDataModel.Experience experience = new CVDataModel.Experience(
                            etPosition.getText().toString(),
                            etCompany.getText().toString(),
                            etDuration.getText().toString(),
                            etDescription.getText().toString()
                    );

                    // Add to temp list
                    tempExperienceList.add(experience);
                    updateExperienceList();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateExperienceList() {
        // Clear the current view
        experienceContainer.removeAllViews();

        // Add each experience item
        for (int i = 0; i < tempExperienceList.size(); i++) {
            CVDataModel.Experience experience = tempExperienceList.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_experience, experienceContainer, false);

            TextView tvPosition = itemView.findViewById(R.id.textViewPosition);
            TextView tvCompany = itemView.findViewById(R.id.textViewCompany);
            TextView tvDuration = itemView.findViewById(R.id.textViewDuration);
            TextView tvDescription = itemView.findViewById(R.id.textViewDescription);
            Button btnDelete = itemView.findViewById(R.id.btnDelete);

            tvPosition.setText(experience.getPosition());
            tvCompany.setText(experience.getCompany());
            tvDuration.setText(experience.getDuration());
            tvDescription.setText(experience.getDescription());

            final int position = i;
            btnDelete.setOnClickListener(v -> {
                tempExperienceList.remove(position);
                updateExperienceList();
            });

            experienceContainer.addView(itemView);
        }
    }
}