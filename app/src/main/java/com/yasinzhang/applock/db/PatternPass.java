package com.yasinzhang.applock.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PatternPass {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String pass;
}
