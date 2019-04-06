package com.yasinzhang.applock.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(foreignKeys = {
        @ForeignKey(entity = Profile.class,
                parentColumns = "id",
                childColumns = "profile_id",
                onDelete = CASCADE,
                onUpdate = RESTRICT)
})
public class SelectedProfile {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "profile_id", index = true)
    public int profileId;
}
