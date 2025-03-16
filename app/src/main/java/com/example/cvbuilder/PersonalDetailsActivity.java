package com.example.cvbuilder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PersonalDetailsActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextAddress;
    private EditText editTextDob;
    private Button btnSave;
    private Button btnCancel;

    private CVDataModel cvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
// Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextDob = findViewById(R.id.editTextDob);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Get data from intent
        if (getIntent().hasExtra("cv_data")) {
            cvData = (CVDataModel) getIntent().getSerializableExtra("cv_data");

            // Populate fields with existing data
            if (cvData.getName() != null) editTextName.setText(cvData.getName());
            if (cvData.getEmail() != null) editTextEmail.setText(cvData.getEmail());
            if (cvData.getPhone() != null) editTextPhone.setText(cvData.getPhone());
            if (cvData.getAddress() != null) editTextAddress.setText(cvData.getAddress());
            if (cvData.getDob() != null) editTextDob.setText(cvData.getDob());
        } else {
            cvData = new CVDataModel();
        }

        // Setup date picker for DOB
        editTextDob.setOnClickListener(v -> showDatePickerDialog());

        // Setup save button
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveData();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("cv_data", cvData);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // Setup cancel button
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    editTextDob.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private boolean validateInputs() {
        if (editTextName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Basic email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!editTextEmail.getText().toString().matches(emailPattern)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveData() {
        cvData.setName(editTextName.getText().toString());
        cvData.setEmail(editTextEmail.getText().toString());
        cvData.setPhone(editTextPhone.getText().toString());
        cvData.setAddress(editTextAddress.getText().toString());
        cvData.setDob(editTextDob.getText().toString());
    }
}