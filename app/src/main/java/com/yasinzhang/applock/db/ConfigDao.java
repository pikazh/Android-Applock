package com.yasinzhang.applock.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface ConfigDao {
    @Query("select * from preference limit 1")
    ConfigRecord getConfig();

    @Insert(onConflict = IGNORE)
    void insertRecord(ConfigRecord sel);

    @Query("update preference set pattern = :pattern")
    void updatePattern(String pattern);

    @Query("delete from preference")
    void deleteAllRecord();
}
