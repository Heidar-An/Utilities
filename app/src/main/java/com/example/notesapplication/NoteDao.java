package com.example.notesapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Delete
    void deleteNote(Note note);

    @Update
    void update(Note... note);

    @Query("SELECT * FROM note_table LIMIT 1")
    Note[] getAnyNote();

    @Query("SELECT * FROM note_table ORDER BY note ASC")
    LiveData<List<Note>> getAllWords();

}
