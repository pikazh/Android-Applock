package com.yasinzhang.applock.db;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
@TypeConverters(AppStringTypeConverter.class)
public class Profile {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    @ColumnInfo(name="package_names")
    public List<String> packageNames;


    public static class ProfileBasicInfo {

        public int id;
        public String name;
    }
}

