package com.example.notesapplication.alarmFiles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AlarmViewModel extends AndroidViewModel {
    private final AlarmRepository mRepository;
    private final LiveData<List<Alarm>> mAllAlarms;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AlarmRepository(application);
        mAllAlarms = mRepository.getAllAlarms();
    }

    public void updateAlarm(Alarm alarm) {
        mRepository.updateAlarm(alarm);
    }

    LiveData<List<Alarm>> getmAllAlarms() {
        return mAllAlarms;
    }

    public void insert(Alarm alarm) {
        mRepository.insert(alarm);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void delete(Alarm alarm){
        mRepository.deleteAlarm(alarm);
    }
}
