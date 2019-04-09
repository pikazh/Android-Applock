package com.yasinzhang.applock.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "preference")
public class ConfigRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "pattern")
    public String encryptionPatternLock;
}
