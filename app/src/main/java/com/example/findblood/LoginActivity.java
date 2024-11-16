package com.example.findblood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextPhone, editTextPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        databaseHelper = new DatabaseHelper(this);
    }

    public void onLoginClick(View view) {
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkUser(phone, SecurityUtils.hashPassword(password))) {
            // Retrieve user ID, user name, and avatar path
            int userId = databaseHelper.getUserIdByPhone(phone);
            String userName = databaseHelper.getUserNameByPhone(phone);

            // Save user ID and login state in SharedPreferences
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_logged_in", true);
            editor.putInt("user_id", userId);
            editor.putString("user_name", userName);
            editor.putString("user_phone", phone);
            editor.apply();

            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Close LoginActivity
        } else {
            Toast.makeText(this, "Số điện thoại hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            super.onBackPressed();
        }
    }
}