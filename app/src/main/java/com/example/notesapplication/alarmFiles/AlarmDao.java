package com.example.notesapplication.alarmFiles;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Delete
    void deleteAlarm(Alarm alarm);

    @Update
    void update(Alarm... alarm);

    @Query("SELECT * FROM alarm_table LIMIT 1")
    Alarm[] getAnyAlarm();

    @Query("SELECT * FROM alarm_table ORDER BY alarmName ASC")
    LiveData<List<Alarm>> getAllAlarms();
}
