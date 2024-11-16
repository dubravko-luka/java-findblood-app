package com.example.findblood;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import android.content.Intent;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.CheckBox; // Added import for CheckBox
import android.content.SharedPreferences; // Added import for SharedPreferences

public class DonorActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle toggle;
    private Spinner spinnerBloodType; // Add Spinner for blood type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        // Initialize Spinner
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        setupBloodTypeSpinner(); // Setup blood type spinner

        // Set default gender to "Nam"
        RadioButton radioMale = findViewById(R.id.radioMale);
        radioMale.setChecked(true); // Set "Nam" as the default selected option

        // Retrieve the logged-in user's ID (this is just an example)
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1); // Assuming user ID is stored in preferences

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.donorName)).getText().toString().trim();
                String phone = ((EditText) findViewById(R.id.donorPhone)).getText().toString().trim();
                String ageString = ((EditText) findViewById(R.id.donorAge)).getText().toString().trim();
                String bloodType = spinnerBloodType.getSelectedItem().toString();
                boolean registerForOthers = ((CheckBox) findViewById(R.id.checkBoxRegisterForSomeoneElse)).isChecked();

                // Validate inputs
                if (name.isEmpty() || phone.isEmpty() || ageString.isEmpty() || bloodType.isEmpty()) {
                    Toast.makeText(DonorActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageString);
                } catch (NumberFormatException e) {
                    Toast.makeText(DonorActivity.this, "Tuổi không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String gender = radioMale.isChecked() ? "Nam" :
                                ((RadioButton) findViewById(R.id.radioFemale)).isChecked() ? "Nữ" : "Khác";

                // Add donor to database
                DatabaseHelper databaseHelper = new DatabaseHelper(DonorActivity.this);
                boolean result = databaseHelper.addDonor(DonorActivity.this, name, phone, age, gender, bloodType, registerForOthers, userId);
                if (result) {
                    Toast.makeText(DonorActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                    // Clear input fields
                    ((EditText) findViewById(R.id.donorName)).setText("");
                    ((EditText) findViewById(R.id.donorPhone)).setText("");
                    ((EditText) findViewById(R.id.donorAge)).setText("");
                    spinnerBloodType.setSelection(0); // Reset spinner to the first item
                    radioMale.setChecked(true); // Reset gender to male
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_register_donate);
    }

    private void setupBloodTypeSpinner() {
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"}; // Full list of blood types
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, bloodTypes); // Use custom layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.registered) {
            Intent intent = new Intent(this, RegisteredActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_update_info_bottom) {
            Intent intent = new Intent(this, UpdateInfoActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
