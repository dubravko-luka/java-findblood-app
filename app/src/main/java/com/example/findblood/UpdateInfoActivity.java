package com.example.findblood;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UpdateInfoActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private EditText editTextName, editTextPhone;
    private TextView avatarInfo;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_update_info_bottom);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        avatarInfo = findViewById(R.id.avatar_update_info);
        databaseHelper = new DatabaseHelper(this);

        loadUserInfo();
    }

    private void loadUserInfo() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userName = preferences.getString("user_name", "");
        String userPhone = preferences.getString("user_phone", "");

        editTextName.setText(userName);
        editTextPhone.setText(userPhone);
        String avatarText = userName.substring(0, 1).toUpperCase();
        avatarInfo.setText(avatarText);
    }

    public void updateUserProfile(View view) {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve user ID from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        // Update user information in the database
        boolean isUpdated = databaseHelper.updateUser(userId, name, phone); // Pass avatarPath
        if (isUpdated) {
            // Update SharedPreferences with new values
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_name", name);
            editor.putString("user_phone", phone);
            editor.apply();

            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            loadUserInfo();
        } else {
            Toast.makeText(this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_register_donate) {
            Intent intent = new Intent(this, DonorActivity.class);
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

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_logged_in", false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}