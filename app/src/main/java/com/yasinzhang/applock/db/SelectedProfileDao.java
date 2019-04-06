package com.yasinzhang.applock.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SelectedProfileDao {
    @Query("select profile_id from SelectedProfile limit 1")
    int getSelectedProfile();

    @Insert(onConflict = IGNORE)
    void insertSelectProfileRecord(SelectedProfile sel);

    @Query("delete from SelectedProfile")
    void deleteAllRecord();
}
