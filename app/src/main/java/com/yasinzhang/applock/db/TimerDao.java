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
    @Query("select * from timer")
    List<TimerRecord> loadTimers();

    @Insert(onConflict = IGNORE)
    void insertTimer(TimerRecord timer);

    @Update(onConflict = REPLACE)
    void updateTimer(TimerRecord timer);

    @Query("update timer set enabled = :enabled where id =:id")
    void updateTimerWithEnabled(int enabled, int id);

    @Query("update timer set repeat = :repeat where id =:id")
    void updateTimerWithRepeat(int repeat, int id);

    @Query("update timer set begin_time = :beginTime where id =:id")
    void updateTimerWithBeginTime(String beginTime, int id);

    @Query("delete from timer where id = :id")
    void deleteTimer(int id);
}
