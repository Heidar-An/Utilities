package com.example.notesapplication.alarmFiles;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.example.notesapplication.Note;

import java.util.List;

public class AlarmRepository {
    private final AlarmDao mAlarmDao;
    private final LiveData<List<Alarm>> mAllTitles;

    AlarmRepository(Application application) {
        AlarmRoomDatabase db = AlarmRoomDatabase.getDatabase(application);
        mAlarmDao = db.AlarmDao();
        mAllTitles = mAlarmDao.getAllAlarms();
    }

    public void updateAlarm(Alarm alarm) {new AlarmRepository.changeAlarmAsyncTask(mAlarmDao).execute(alarm);}

    public void insert (Alarm alarm) {
        new AlarmRepository.insertAsyncTask(mAlarmDao).execute(alarm);
    }

    public void deleteAll(){
        new AlarmRepository.deleteAllAlarmsAsyncTask(mAlarmDao).execute();
    }

    public void deleteAlarm(Alarm alarm){
        new AlarmRepository.deleteAlarmAsyncTask(mAlarmDao).execute(alarm);
    }

    LiveData<List<Alarm>> getAllAlarms() {
        return mAllTitles;
    }

    private static class insertAsyncTask extends AsyncTask<Alarm, Void, Void> {

        private final AlarmDao mAsyncTaskDao;

        insertAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Alarm... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAlarmsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final AlarmDao mAsyncTaskDao;

        deleteAllAlarmsAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private final AlarmDao mAsyncTaskDao;

        deleteAlarmAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Alarm... params) {
            mAsyncTaskDao.deleteAlarm(params[0]);
            return null;
        }
    }

    private static class changeAlarmAsyncTask extends AsyncTask<Alarm, Note, Void> {
        private final AlarmDao mAsyncTaskDao;
        changeAlarmAsyncTask(AlarmDao dao){ mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Alarm... params){
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
