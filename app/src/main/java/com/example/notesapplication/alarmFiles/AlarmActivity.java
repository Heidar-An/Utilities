package com.example.notesapplication.alarmFiles;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplication.MainActivity;
import com.example.notesapplication.R;
import com.example.notesapplication.activitySwipeListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmActivity extends AppCompatActivity implements alarmRecyclerView.OnAlarmListener{
    public static int NEW_ALARM_ACTIVITY_REQUEST_CODE = 1;
    public static int OLD_ALARM_ACTIVITY_REQUEST_CODE = 2;
    private AlarmViewModel mAlarmViewModel;
    private List<Alarm> mAlarms = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // create variables
        ExtendedFloatingActionButton createFAB = findViewById(R.id.extendedCreateFab);
        ConstraintLayout mainClockLayout = findViewById(R.id.mainClockLayout);
        RecyclerView recyclerAlarms = findViewById(R.id.recyclerAlarms);

        mAlarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        alarmRecyclerView recyclerAdapter = new alarmRecyclerView(this, this, mAlarmViewModel, this);
        recyclerAlarms.setAdapter(recyclerAdapter);
        recyclerAlarms.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new AlarmSwipeToDelete(recyclerAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerAlarms);

        // check if list of alarms change
        mAlarmViewModel.getmAllAlarms().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(@Nullable final List<Alarm> alarms) {
                // Update the cached copy of the words in the recyclerAdapter.
                recyclerAdapter.setmLocalDataSet(alarms);
                mAlarms = alarms;
                activateAlarms();
                alarmRecyclerView.AlarmViewHolder.setmDataSet(alarms);
            }
        });

        // Create a new alarm if FAB pressed
        createFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Has Been Clicked");
                Intent intent = new Intent(AlarmActivity.this, CreateAlarmActivity.class);
                startActivityForResult(intent, NEW_ALARM_ACTIVITY_REQUEST_CODE);
            }
        });

        // Change the activity if swiped to the right and left
        mainClockLayout.setOnTouchListener(new activitySwipeListener(AlarmActivity.this){
            public void onSwipeRight(){
                Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }

            public void onSwipeLeft(){
                System.out.println("This function is being called");
            }
        });
    }

    // function to set alarm for "alarm" item in list, if actived
    public void activateAlarms(){
        // request code initialisation
        int i = 0;
        for(Alarm alarm:mAlarms){
            System.out.println("This loop is being called " + alarm.getName());
            // create intent to send
            Intent intent = new Intent(this, AlarmReciever.class);
            intent.putExtra("AlarmCreator", "Create");
            intent.putExtra("Alarm", alarm);

            // find out what time to set the alarm
            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getTimeHour()));
            calendar.set(Calendar.MINUTE, Integer.parseInt(alarm.getTimeMinute()));

            // send the intent to the broadcast receiver
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
            // a different request code has to be set for each alarm
            i += 1;

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            System.out.println("Alarm has supposedly been sent");
        }
    }

    // When new alarm has been created or old is being edited
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // check if a new alarm is being created or an old one is being edited
        if(requestCode == NEW_ALARM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            if(data.hasExtra(CreateAlarmActivity.EXTRA_REPLY)){
                String[] intentData = data.getStringArrayExtra(CreateAlarmActivity.EXTRA_REPLY);
                Alarm alarm = new Alarm(intentData[0], intentData[1], intentData[2]);
                mAlarmViewModel.insert(alarm);
            }
        }else if(requestCode == OLD_ALARM_ACTIVITY_REQUEST_CODE){
            // old alarm is being edited
            System.out.println("Old Alarm");
            if(data.hasExtra(CreateAlarmActivity.EXTRA_REPLY)){
                String[] intentData = data.getStringArrayExtra(CreateAlarmActivity.EXTRA_REPLY);
                int alarmID = data.getIntExtra(CreateAlarmActivity.INTID_REPLY, -1);
                // Get the condition of if alarm is activated or not, and use this to create a new alarm
                Alarm alarm = new Alarm(alarmID, intentData[0], intentData[1], intentData[2]);
                mAlarmViewModel.updateAlarm(alarm);
            }
        }else{
            // title of alarm, or time of alarm was not created so a toast is sent to user
            Toast.makeText(getApplicationContext(), "Alarm not saved because parts were missing", Toast.LENGTH_LONG).show();
        }
    }

    // Check if alarm in recycler view is being pressed
    @Override
    public void onAlarmClick(Alarm alarm) {
        Intent intent = new Intent(this, CreateAlarmActivity.class);
        intent.putExtra("IDALARM", alarm.getId());
        intent.putExtra("selectedAlarm", alarm);
        System.out.println("Id = " + alarm.getId());
//        Intent notiIntent = new Intent(this, AlarmReceiver.class);
//        notiIntent.putExtra("AlarmCreator", "create");
//        notiIntent.putExtra("Alarm", alarm);
//        new AlarmReceiver(this, notiIntent);
        startActivityForResult(intent, OLD_ALARM_ACTIVITY_REQUEST_CODE);
    }

    // Check if slider is being changed, meaning alarm is now on or off
    @Override
    public void onSlideChanged(Alarm alarm, boolean isChecked) {
        alarm.setActiveAlarm(isChecked);
        System.out.println("New: " + alarm.getActiveAlarm());
        mAlarmViewModel.updateAlarm(alarm);
    }
}
