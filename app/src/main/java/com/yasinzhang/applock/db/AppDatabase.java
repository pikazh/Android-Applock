package com.yasinzhang.applock.db;

import com.yasinzhang.applock.AppLockApplication;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Profile.class, Timer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE = null;

    public abstract ProfileDao profileModel();

    public abstract TimerDao timerModel();

    public static AppDatabase getDatabase() {
        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(AppLockApplication.getInstance(), AppDatabase.class, "data").build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if(INSTANCE != null){
            INSTANCE.close();
            INSTANCE = null;
        }

    }
}