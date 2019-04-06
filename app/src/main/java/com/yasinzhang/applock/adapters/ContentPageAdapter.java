package com.yasinzhang.applock.adapters;

import com.yasinzhang.applock.fragments.PackageLockListFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ContentPageAdapter extends FragmentStatePagerAdapter {

    public ContentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return new PackageLockListFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

}
