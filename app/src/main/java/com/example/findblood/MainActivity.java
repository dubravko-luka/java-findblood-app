package com.example.findblood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager; // Thêm ViewPager
    private SlideshowAdapter slideshowAdapter; // Thêm adapter cho slideshow
    private Handler handler;
    private Runnable runnable;
    private boolean isScrolling = false;
    private RecyclerView recyclerView;
    private DonationAdapter donationAdapter;
    private DonateCenterAdapter donateCenterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Initialize the slideshow
        viewPager = findViewById(R.id.view_pager);
        initSlideshow(); // Initialize slideshow
        startAutoScroll(); // Start auto-scrolling

        initDonor();
        initDonateCenter();
    }

    private void initSlideshow() {
        String[] imageUrls = {
            "https://images.hcmcpv.org.vn/res/news/2022/02/12-02-2022-benh-vien-cho-ray-keu-goi-nguoi-dan-hien-mau-1A9D705.jpg",
            "https://winhome.com.vn/wp-content/uploads/2021/12/HIEN-MAU-1400x787.png",
            "https://giaan115.com/uploads/files/2022/09/19/GG-FORM-_-CT-HI-N-M-U-NH-N-O-Aeon-Binh-T-n-V2-_-5-10-2022-01.jpg"
        };

        slideshowAdapter = new SlideshowAdapter(this, imageUrls);
        viewPager.setAdapter(slideshowAdapter);
    }

    private void initDonor() {
        recyclerView = findViewById(R.id.recycler_view_donations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch recent donors from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Donor> donationList = databaseHelper.getRecentDonors(3); // Get the 3 most recent donors

        donationAdapter = new DonationAdapter(donationList);
        recyclerView.setAdapter(donationAdapter);
    }

    private void initDonateCenter() {
        recyclerView = findViewById(R.id.recycler_view_donate_center);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create sample data for the donation list
        List<DonateCenter> donateCenterList = new ArrayList<>();
        donateCenterList.add(new DonateCenter("https://images.hcmcpv.org.vn/res/news/2022/02/12-02-2022-benh-vien-cho-ray-keu-goi-nguoi-dan-hien-mau-1A9D705.jpg", "Trung tâm hiến máu nhân đạo TP.HCM", "(+84) 123.456.789", "106 Thiên Phước, Phường 9, Tân Bình, Hồ Chí Minh"));
        donateCenterList.add(new DonateCenter("https://winhome.com.vn/wp-content/uploads/2021/12/HIEN-MAU-1400x787.png", "Trung tâm hiến máu nhân đạo TP.HCM", "(+84) 987.654.321", "106 Thiên Phước, Phường 9, Tân Bình, Hồ Chí Minh"));
        donateCenterList.add(new DonateCenter("https://giaan115.com/uploads/files/2022/09/19/GG-FORM-_-CT-HI-N-M-U-NH-N-O-Aeon-Binh-T-n-V2-_-5-10-2022-01.jpg", "Trung tâm hiến máu nhân đạo TP.HCM", "(+84) 934.123.456", "106 Thiên Phước, Phường 9, Tân Bình, Hồ Chí Minh"));

        donateCenterAdapter = new DonateCenterAdapter(donateCenterList);
        recyclerView.setAdapter(donateCenterAdapter);
    }

    private void startAutoScroll() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isScrolling) { // Only scroll if not currently scrolling
                    int currentItem = viewPager.getCurrentItem();
                    int totalItems = slideshowAdapter.getCount();

                    // Move to the next item, looping back to the start if at the end
                    if (currentItem < totalItems - 1) {
                        isScrolling = true; // Set the flag to true
                        viewPager.setCurrentItem(currentItem + 1, true); // Smooth scroll
                    } else {
                        isScrolling = true; // Set the flag to true
                        viewPager.setCurrentItem(0, true); // Smooth scroll to the first item
                    }

                    // Reset the scrolling flag after a delay to allow for smooth transition
                    handler.postDelayed(() -> isScrolling = false, 500); // Adjust delay based on your slide duration
                }

                // Repeat this runnable code block again after a delay
                handler.postDelayed(this, 3000); // 3000 milliseconds = 3 seconds
            }
        };

        // Start the auto-scrolling
        handler.postDelayed(runnable, 3000); // Initial delay before starting
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop auto-scrolling when the activity is paused
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume auto-scrolling when the activity is resumed
        handler.postDelayed(runnable, 3000);
        
        // Reload the donor list
        initDonor(); // Refresh the list of recent donors
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_register_donate) {
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
        else if (id == R.id.nav_update_info_bottom) {
            Intent intent = new Intent(this, UpdateInfoActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
    
}
