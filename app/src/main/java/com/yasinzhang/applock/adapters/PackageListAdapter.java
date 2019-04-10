package com.yasinzhang.applock.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yasinzhang.applock.R;
import com.yasinzhang.applock.commons.AppInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.PackageListViewHolder> {

    List<AppInfo> mData = null;

    public void setData(List<AppInfo> mData){
        this.mData = mData;
    }

    @NonNull
    @Override
    public PackageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item_view, parent, false);
        return new PackageListViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull PackageListViewHolder holder, int position) {
        AppInfo info = mData.get(position);
        holder.mAppName.setText(info.appName);
        holder.mAppDesc.setText(info.getDescription());
        holder.mIcon.setImageDrawable(info.appIcon);
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;

        return mData.size();
    }


    class PackageListViewHolder extends RecyclerView.ViewHolder {
        TextView mAppName;
        TextView mAppDesc;
        ImageView mIcon;


        public PackageListViewHolder(View itemView) {
            super(itemView);
            mAppName = itemView.findViewById(R.id.app_name);
            mAppDesc = itemView.findViewById(R.id.app_desc);
            mIcon = itemView.findViewById(R.id.app_icon);
        }
    }
}