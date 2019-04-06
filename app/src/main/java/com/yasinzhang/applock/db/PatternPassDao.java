package com.yasinzhang.applock.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface PatternPassDao {
    @Query("select pass from PatternPass limit 1")
    String getPatternPass();

    @Insert(onConflict = IGNORE)
    void insertPatternPassRecord(PatternPass sel);

    @Query("delete from PatternPass")
    void deleteAllRecord();
}
