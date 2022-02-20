package com.example.notesapplication;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

public class NoteRepository {
    private final NoteDao mNoteDao;
    private final LiveData<List<Note>> mAllTitles;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.NoteDao();
        mAllTitles = mNoteDao.getAllWords();
    }

    public void updateNoteTitle(Note note) {new changeNoteAsyncTask(mNoteDao).execute(note);}

    public void insert (Note note) {
        new insertAsyncTask(mNoteDao).execute(note);
    }

    public void deleteAll(){
        new deleteAllNotesAsyncTask(mNoteDao).execute();
    }

    public void deleteNote(Note note){
        new deleteNoteAsyncTask(mNoteDao).execute(note);
    }

    LiveData<List<Note>> getAllWords() {
        return mAllTitles;
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private final NoteDao mAsyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private final NoteDao mAsyncTaskDao;

        deleteAllNotesAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao mAsyncTaskDao;

        deleteNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.deleteNote(params[0]);
            return null;
        }
    }

    private static class changeNoteAsyncTask extends AsyncTask<Note, Note, Void>{
        private final NoteDao mAsyncTaskDao;
        changeNoteAsyncTask(NoteDao dao){ mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Note... params){
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

}
