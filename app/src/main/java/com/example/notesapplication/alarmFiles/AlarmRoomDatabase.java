package com.example.notesapplication.alarmFiles;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;



@Database(entities = {Alarm.class}, version = 3, exportSchema = false)
public abstract class AlarmRoomDatabase extends RoomDatabase {
    public abstract AlarmDao AlarmDao();
    private static AlarmRoomDatabase INSTANCE;

    public static AlarmRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AlarmRoomDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AlarmRoomDatabase.class, "alarm_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(sRoomDatabaseCallback)
                        .build();
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final AlarmDao mDao;
        String[] titles = {"First Alarm"};

        PopulateDbAsync(AlarmRoomDatabase db) {
            mDao = db.AlarmDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            // mDao.deleteAll();

            if(mDao.getAnyAlarm().length < 1){
                for (int i = 0; i < titles.length - 1; i++) {
                    // add the note text
                    Alarm alarm = new Alarm(titles[i], "12", "10");
                    mDao.insert(alarm);
                }
            }
            return null;
        }
    }

}
