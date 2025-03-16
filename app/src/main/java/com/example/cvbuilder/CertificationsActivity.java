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

public class CertificationsActivity extends AppCompatActivity {
    private LinearLayout certificationsContainer;
    private FloatingActionButton fabAddCertification;
    private Button btnSave;
    private Button btnCancel;

    private CVDataModel cvData;
    private List<String> tempCertificationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_certifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views
        certificationsContainer = findViewById(R.id.certificationsContainer);
        fabAddCertification = findViewById(R.id.fabAddCertification);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        tempCertificationsList = new ArrayList<>();

        // Get data from intent
        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");

            // Populate fields with existing data
            if (cvData.getCertifications() != null && !cvData.getCertifications().isEmpty()) {
                tempCertificationsList.addAll(cvData.getCertifications());
                updateCertificationsList();
            }
        } else {
            cvData = new CVDataModel();
        }

        // Setup add certification button
        fabAddCertification.setOnClickListener(v -> {
            showAddCertificationDialog();
        });

        // Setup save button
        btnSave.setOnClickListener(v -> {
            // It's okay to have no certifications
            // Clear existing certifications list and add all from temp list
            cvData.getCertifications().clear();
            cvData.getCertifications().addAll(tempCertificationsList);

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

    private void showAddCertificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_certification, null);
        builder.setView(dialogView);

        EditText etCertification = dialogView.findViewById(R.id.editTextCertification);

        builder.setTitle("Add Certification")
                .setPositiveButton("Add", (dialog, id) -> {
                    if (etCertification.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please enter certification name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Add to temp list
                    tempCertificationsList.add(etCertification.getText().toString());
                    updateCertificationsList();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateCertificationsList() {
        // Clear the current view
        certificationsContainer.removeAllViews();

        // Add each certification item
        for (int i = 0; i < tempCertificationsList.size(); i++) {
            String certification = tempCertificationsList.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_certification, certificationsContainer, false);

            TextView tvCertification = itemView.findViewById(R.id.textViewCertification);
            Button btnDelete = itemView.findViewById(R.id.btnDelete);

            tvCertification.setText(certification);

            final int position = i;
            btnDelete.setOnClickListener(v -> {
                tempCertificationsList.remove(position);
                updateCertificationsList();
            });

            certificationsContainer.addView(itemView);
        }
    }
}