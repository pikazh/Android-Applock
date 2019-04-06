package com.yasinzhang.applock.activities;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yasinzhang.applock.R;
import com.yasinzhang.applock.adapters.ContentPageAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private ViewPager mMainViewPager = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = (item) -> {
                switch (item.getItemId()) {
                    case R.id.navigation_app_list:
                        mMainViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_profiles:
                        mMainViewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_time_scheduler:
                        mMainViewPager.setCurrentItem(2);
                        return true;
                }
                return false;

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewPager = findViewById(R.id.main_viewpager);
        mMainViewPager.setAdapter(new ContentPageAdapter(getSupportFragmentManager()));

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
