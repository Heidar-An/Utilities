package com.example.notesapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note implements Parcelable{

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "note")
    private final String title;

    @NonNull
    @ColumnInfo(name = "noteText")
    private final String noteText;

    public Note(@NonNull String title, @NonNull String noteText){
        this.title = title;
        this.noteText = noteText;
    }

    @Ignore
    public Note(int id, @NonNull String title, @NonNull String noteText){
        this.id = id;
        this.title = title;
        this.noteText = noteText;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        noteText = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @NonNull
    public String getNoteText(){return this.noteText;}

    public int getId(){ return this.id;}

    @NonNull
    public String getTitle() {
        return this.title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(noteText);
    }
}