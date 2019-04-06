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
public interface ProfileDao {

    @Query("select id, name from Profile")
    List<Profile.ProfileBasicInfo> loadProfileBasicInfos();

    @Query("select package_names from Profile where id = :id")
    List<String> findProfile(int id);

    @Insert(onConflict = IGNORE)
    void insertProfile(Profile profile);

    @Update(onConflict = REPLACE)
    void updateProfile(Profile profile);

    @Query("delete from Profile where id = :id")
    void deleteProfile(int id);
}
