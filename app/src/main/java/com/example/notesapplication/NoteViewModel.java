package com.example.notesapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository mRepository;
    private final LiveData<List<Note>> mAllTitles;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllTitles = mRepository.getAllWords();
    }

    public void updateNoteTitle(Note note){
        mRepository.updateNoteTitle(note);
    }

    LiveData<List<Note>> getAllTitles() { return mAllTitles; }

    public void insert(Note note) { mRepository.insert(note); }

    public void deleteAll() {mRepository.deleteAll();}

    public void delete(Note note) {mRepository.deleteNote(note);}
}
