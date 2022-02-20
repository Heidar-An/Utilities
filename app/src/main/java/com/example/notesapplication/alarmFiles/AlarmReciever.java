package com.example.notesapplication.alarmFiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Alarm is being recieved");
        Alarm alarm = intent.getParcelableExtra("Alarm");
        System.out.println("alarm is " + alarm);
        NotificationCreator notificationCreator = new NotificationCreator(context, intent);

    }
}
