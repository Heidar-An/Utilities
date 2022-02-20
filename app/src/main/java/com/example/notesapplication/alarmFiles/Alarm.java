package com.example.notesapplication.alarmFiles;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm_table")
public class Alarm implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "alarmName")
    private final String name;

    @NonNull
    @ColumnInfo(name = "alarmHour")
    private final String timeHour;

    @NonNull
    @ColumnInfo(name = "alarmMinute")
    private final String timeMinute;

    @ColumnInfo(name = "activeAlarm")
    private boolean activeAlarm;

    public Alarm(@NonNull String name, @NonNull String timeHour, @NonNull String timeMinute){
        this.name = name;
        this.timeHour = timeHour;
        this.timeMinute = timeMinute;
        activeAlarm = false;
    }

    @Ignore
    public Alarm(int id, @NonNull String name, @NonNull String timeHour, @NonNull String timeMinute){
        this.id = id;
        this.name = name;
        this.timeHour = timeHour;
        this.timeMinute = timeMinute;
    }

    protected Alarm(Parcel in) {
        id = in.readInt();
        name = in.readString();
        timeHour = in.readString();
        timeMinute = in.readString();
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public void setActiveAlarm(boolean activeAlarm){
        this.activeAlarm = activeAlarm;
    }

    public boolean getActiveAlarm(){
        return activeAlarm;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getTimeHour() {
        return timeHour;
    }

    @NonNull
    public String getTimeMinute() {
        return timeMinute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(timeHour);
        dest.writeString(timeMinute);
    }


}
