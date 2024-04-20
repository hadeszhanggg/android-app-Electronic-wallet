package com.example.electronicwallet;

import static androidx.fragment.app.FragmentStatePagerAdapter.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.electronicwallet.fragment.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager2.widget.ViewPager2;


public class HomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addControl();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        addEvent();
    }
    protected void addControl(){
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    protected void addEvent() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_deposit).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
                        break;
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.navigation_deposit) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.navigation_chat) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });
    }
}