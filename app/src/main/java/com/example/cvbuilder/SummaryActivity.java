package com.example.cvbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SummaryActivity extends AppCompatActivity {
    private EditText editTextSummary;
    private Button btnSave;
    private Button btnCancel;

    private CVDataModel cvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        editTextSummary = findViewById(R.id.editTextSummary);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Get data from intent
        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");

            // Populate fields with existing data
            if (cvData.getSummary() != null) {
                editTextSummary.setText(cvData.getSummary());
            }
        } else {
            cvData = new CVDataModel();
        }

        // Setup save button
        btnSave.setOnClickListener(v -> {
            if (!editTextSummary.getText().toString().isEmpty()) {
                cvData.setSummary(editTextSummary.getText().toString());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("cv_data", cvData);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please enter your professional summary", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup cancel button
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}