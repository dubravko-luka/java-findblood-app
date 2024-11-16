package com.example.findblood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView timeTextView = findViewById(R.id.splash_time);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        timeTextView.setText(currentTime);

        new Handler().postDelayed(() -> {
          Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
