package com.yasinzhang.applock.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yasinzhang.applock.R;
import com.yasinzhang.applock.adapters.PackageListAdapter;
import com.yasinzhang.applock.asynctasks.AppInfoRetrieveTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PackageListFragment extends Fragment {

    RecyclerView mAppListView = null;
    PackageListAdapter mAdapter = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.package_list_view, parent, false);
        mAppListView = rootView.findViewById(R.id.package_list_view);

        Context context = this.getActivity();
        mAppListView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new PackageListAdapter();
        mAppListView.setAdapter(mAdapter);

        AppInfoRetrieveTask getDataTask = new AppInfoRetrieveTask();
        getDataTask.registerCallback(this, datas->{
            mAdapter.setData(datas);
            mAdapter.notifyDataSetChanged();
        });
        getDataTask.start();

        return rootView;
    }

    @Override
    public void onStop(){
        super.onStop();

    }

}
