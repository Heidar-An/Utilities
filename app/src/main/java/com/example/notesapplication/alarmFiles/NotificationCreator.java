package com.example.notesapplication.alarmFiles;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.notesapplication.R;

public class NotificationCreator extends ContextWrapper {
    public int channelId = 0;
    private NotificationManager mManager;


    public NotificationCreator(Context context, Intent intent) {
        super(context);

        if(intent.getStringExtra("AlarmCreator") != null){
                if(intent.getParcelableExtra("Alarm") != null){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        createNotificationChannel();
                    }
                    Alarm alarm = intent.getParcelableExtra("Alarm");

                    NotificationManager manager = getManager();
                    NotificationCompat.Builder builder = getChannelNotification(context, alarm);

                    manager.notify(alarm.getId(), builder.build());
                }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel(){
            CharSequence name = "Everything App";
            String description = "Alarm";
            NotificationChannel channel = new NotificationChannel("NotificationId", name, NotificationManager.IMPORTANCE_HIGH);
            channelId += 1;
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);

            getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(Context context, Alarm alarm){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent i = new Intent(context, AlarmActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        i,
                        PendingIntent.FLAG_ONE_SHOT
                );

        return new NotificationCompat.Builder(context, "NotificationId")
                .setSmallIcon(R.mipmap.staricon_round)
                .setContentTitle(alarm.getName())
                .setContentText("Alarm " + alarm.getTimeHour() + ":" + alarm.getTimeMinute())
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setLights(0xFFb71c1c, 1000, 2000)
                .setSound(notification)
                .setContentIntent(pendingIntent);
    }
}
