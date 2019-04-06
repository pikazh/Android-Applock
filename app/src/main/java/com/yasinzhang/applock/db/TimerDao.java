package com.yasinzhang.applock.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TimerDao {
    @Query("select * from Timer")
    List<Timer> loadTimers();

    @Insert(onConflict = IGNORE)
    void insertTimer(Timer timer);

    @Update(onConflict = REPLACE)
    void updateTimer(Timer timer);

    @Query("update Timer set enabled = :enabled where id =:id")
    void updateTimerWithEnabled(int enabled, int id);

    @Query("update Timer set repeat = :repeat where id =:id")
    void updateTimerWithRepeat(int repeat, int id);

    @Query("update Timer set begin_time = :beginTime where id =:id")
    void updateTimerWithBeginTime(String beginTime, int id);

    @Query("delete from Timer where id = :id")
    void deleteTimer(int id);
}
