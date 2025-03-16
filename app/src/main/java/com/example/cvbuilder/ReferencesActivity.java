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

public class ReferencesActivity extends AppCompatActivity {
    private LinearLayout referencesContainer;
    private FloatingActionButton fabAddReference;
    private Button btnSave;
    private Button btnCancel;

    private CVDataModel cvData;
    private List<CVDataModel.Reference> tempReferencesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_references);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views
        referencesContainer = findViewById(R.id.referencesContainer);
        fabAddReference = findViewById(R.id.fabAddReference);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        tempReferencesList = new ArrayList<>();

        // Get data from intent
        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");

            // Populate fields with existing data
            if (cvData.getReferences() != null && !cvData.getReferences().isEmpty()) {
                tempReferencesList.addAll(cvData.getReferences());
                updateReferencesList();
            }
        } else {
            cvData = new CVDataModel();
        }

        // Setup add reference button
        fabAddReference.setOnClickListener(v -> {
            showAddReferenceDialog();
        });

        // Setup save button
        btnSave.setOnClickListener(v -> {
            // It's okay to have no references
            // Clear existing references list and add all from temp list
            cvData.getReferences().clear();
            cvData.getReferences().addAll(tempReferencesList);

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

    private void showAddReferenceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_reference, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.editTextName);
        EditText etPosition = dialogView.findViewById(R.id.editTextPosition);
        EditText etContact = dialogView.findViewById(R.id.editTextContact);

        builder.setTitle("Add Reference")
                .setPositiveButton("Add", (dialog, id) -> {
                    if (etName.getText().toString().isEmpty() ||
                            etPosition.getText().toString().isEmpty() ||
                            etContact.getText().toString().isEmpty()) {

                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create new Reference object
                    CVDataModel.Reference reference = new CVDataModel.Reference(
                            etName.getText().toString(),
                            etPosition.getText().toString(),
                            etContact.getText().toString()
                    );

                    // Add to temp list
                    tempReferencesList.add(reference);
                    updateReferencesList();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateReferencesList() {
        // Clear the current view
        referencesContainer.removeAllViews();

        // Add each reference item
        for (int i = 0; i < tempReferencesList.size(); i++) {
            CVDataModel.Reference reference = tempReferencesList.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_reference, referencesContainer, false);

            TextView tvName = itemView.findViewById(R.id.textViewName);
            TextView tvPosition = itemView.findViewById(R.id.textViewPosition);
            TextView tvContact = itemView.findViewById(R.id.textViewContact);
            Button btnDelete = itemView.findViewById(R.id.btnDelete);

            tvName.setText(reference.getName());
            tvPosition.setText(reference.getPosition());
            tvContact.setText(reference.getContact());

            final int position = i;
            btnDelete.setOnClickListener(v -> {
                tempReferencesList.remove(position);
                updateReferencesList();
            });

            referencesContainer.addView(itemView);
        }
    }
}