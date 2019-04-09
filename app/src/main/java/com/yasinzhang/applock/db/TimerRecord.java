package com.yasinzhang.applock.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "timer", foreignKeys = {
        @ForeignKey(entity = LockProfileRecord.class,
                parentColumns = "id",
                childColumns = "profile_id",
                onDelete = CASCADE,
                onUpdate = RESTRICT)
})
public class TimerRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "profile_id", index = true)
    public int profileId;

    @NonNull
    @ColumnInfo(name = "begin_time")
    public String beginTime;

    @ColumnInfo(name = "repeat")
    public int repeatInWeeks;

    public int enabled;
}
