package com.example.notesapplication;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 5, exportSchema = false)
public abstract class NoteRoomDatabase extends RoomDatabase {
    public abstract NoteDao NoteDao();
    private static NoteRoomDatabase INSTANCE;

    public static NoteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class, "title_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
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

        private final NoteDao mDao;
        String[] titles = {"First Note", "Second Note", "Third Note"};


        PopulateDbAsync(NoteRoomDatabase db) {
            mDao = db.NoteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            // mDao.deleteAll();

            if(mDao.getAnyNote().length < 1){
                for (int i = 0; i < titles.length - 1; i++) {
                    // add the note text
                    Note note = new Note(titles[i], "");
                    mDao.insert(note);
                }
            }
            return null;
        }
    }
}
