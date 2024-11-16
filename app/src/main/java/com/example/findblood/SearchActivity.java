package com.example.findblood;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import android.content.Intent;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private DonationAdapter donationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý tìm kiếm khi nhấn nút tìm kiếm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cập nhật danh sách khi người dùng nhập
                return false;
            }
        });

        recyclerView = findViewById(R.id.recycler_view_donors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách người hiến máu
        List<Donor> donorList = new ArrayList<>();
        donorList.add(new Donor("Nguyễn Văn A", "(+84) 123.456.789", "25", "A"));
        donorList.add(new Donor("Trần Thị B", "(+84) 987.654.321", "30", "O"));
        donorList.add(new Donor("Phạm Văn C", "(+84) 934.123.456", "35", "B"));
        donorList.add(new Donor("Lê Văn D", "(+84) 912.345.678", "28", "AB"));
        donorList.add(new Donor("Nguyễn Thị E", "(+84) 876.543.210", "22", "A"));
        donorList.add(new Donor("Trần Văn F", "(+84) 765.432.109", "40", "O"));
        donorList.add(new Donor("Phạm Thị G", "(+84) 654.321.098", "33", "B"));
        donorList.add(new Donor("Lê Văn H", "(+84) 543.210.987", "29", "AB"));
        donorList.add(new Donor("Nguyễn Văn I", "(+84) 432.109.876", "31", "A"));
        donorList.add(new Donor("Trần Thị J", "(+84) 321.098.765", "27", "O"));

        // Khởi tạo adapter và gán cho RecyclerView
        donationAdapter = new DonationAdapter(donorList);
        recyclerView.setAdapter(donationAdapter);
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
