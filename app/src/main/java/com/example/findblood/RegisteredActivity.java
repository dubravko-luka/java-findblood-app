package com.example.findblood;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.content.SharedPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import androidx.appcompat.app.ActionBarDrawerToggle;
import java.util.List;
import android.view.MenuItem;

public class RegisteredActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private DonationAdapter registeredAdapter;
    private List<Donor> registeredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.registered);

        initDonor();
    }

    private void initDonor() {
        recyclerView = findViewById(R.id.recycler_view_registered);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 1);

        // Fetch recent donors from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Donor> donationList = databaseHelper.getMyRecentDonors(userId); // Pass userId to fetch specific donors

        registeredAdapter = new DonationAdapter(donationList); // Initialize the adapter with the donor list
        recyclerView.setAdapter(registeredAdapter); // Set the adapter to the RecyclerView
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