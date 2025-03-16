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

public class EducationActivity extends AppCompatActivity {
    private LinearLayout educationContainer;
    private FloatingActionButton fabAddEducation;
    private Button btnSave;
    private Button btnCancel;

    private CVDataModel cvData;
    private List<CVDataModel.Education> tempEducationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views
        educationContainer = findViewById(R.id.educationContainer);
        fabAddEducation = findViewById(R.id.fabAddEducation);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        tempEducationList = new ArrayList<>();

        // Get data from intent
        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");

            // Populate fields with existing data
            if (cvData.getEducationList() != null && !cvData.getEducationList().isEmpty()) {
                tempEducationList.addAll(cvData.getEducationList());
                updateEducationList();
            }
        } else {
            cvData = new CVDataModel();
        }

        // Setup add education button
        fabAddEducation.setOnClickListener(v -> {
            showAddEducationDialog();
        });

        // Setup save button
        btnSave.setOnClickListener(v -> {
            if (tempEducationList.isEmpty()) {
                Toast.makeText(this, "Please add at least one education entry", Toast.LENGTH_SHORT).show();
                return;
            }

            // Clear existing education list and add all from temp list
            cvData.getEducationList().clear();
            cvData.getEducationList().addAll(tempEducationList);

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

    private void showAddEducationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_education, null);
        builder.setView(dialogView);

        EditText etDegree = dialogView.findViewById(R.id.editTextDegree);
        EditText etInstitution = dialogView.findViewById(R.id.editTextInstitution);
        EditText etYear = dialogView.findViewById(R.id.editTextYear);
        EditText etGrade = dialogView.findViewById(R.id.editTextGrade);

        builder.setTitle("Add Education")
                .setPositiveButton("Add", (dialog, id) -> {
                    if (etDegree.getText().toString().isEmpty() ||
                            etInstitution.getText().toString().isEmpty() ||
                            etYear.getText().toString().isEmpty()) {

                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create new Education object
                    CVDataModel.Education education = new CVDataModel.Education(
                            etDegree.getText().toString(),
                            etInstitution.getText().toString(),
                            etYear.getText().toString(),
                            etGrade.getText().toString()
                    );

                    // Add to temp list
                    tempEducationList.add(education);
                    updateEducationList();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateEducationList() {
        // Clear the current view
        educationContainer.removeAllViews();

        // Add each education item
        for (int i = 0; i < tempEducationList.size(); i++) {
            CVDataModel.Education education = tempEducationList.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_education, educationContainer, false);

            TextView tvDegree = itemView.findViewById(R.id.textViewDegree);
            TextView tvInstitution = itemView.findViewById(R.id.textViewInstitution);
            TextView tvYear = itemView.findViewById(R.id.textViewYear);
            Button btnDelete = itemView.findViewById(R.id.btnDelete);

            tvDegree.setText(education.getDegree());
            tvInstitution.setText(education.getInstitution());
            tvYear.setText(education.getYear());

            final int position = i;
            btnDelete.setOnClickListener(v -> {
                tempEducationList.remove(position);
                updateEducationList();
            });

            educationContainer.addView(itemView);
        }
    }
}