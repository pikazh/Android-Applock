package com.yasinzhang.applock.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yasinzhang.applock.AppLockApplication;
import com.yasinzhang.applock.R;
import com.yasinzhang.applock.adapters.ContentPageAdapter;

import androidx.appcompat.app.AlertDialog;
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

    @Override
    protected void onStart(){
        super.onStart();

        //showNormalDialog();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(myIntent, 1);
        }

        //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    private void showNormalDialog(){

        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher_round);
        normalDialog.setTitle("我是一个普通Dialog");
        normalDialog.setMessage("你要点击哪一个按钮呢?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // 显示
        normalDialog.show();
    }

}
