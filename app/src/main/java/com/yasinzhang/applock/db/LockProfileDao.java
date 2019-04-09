package com.yasinzhang.applock.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters(AppStringTypeConverter.class)
public interface LockProfileDao {

    @Query("select id, name from lock_profile")
    List<LockProfileRecord.LockProfileBasicInfo> loadProfileBasicInfos();

    @Query("select package_names from lock_profile where id = :id")
    List<String> findProfile(int id);

    @Insert(onConflict = IGNORE)
    void insertProfile(LockProfileRecord profile);

    @Update(onConflict = REPLACE)
    void updateProfile(LockProfileRecord profile);

    @Query("delete from lock_profile where id = :id")
    void deleteProfile(int id);
}
