package com.example.notesapplication.alarmFiles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapplication.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class CreateAlarmActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.notesapplication.alarmFiles.REPLY";
    public static final String INTID_REPLY = "com.example.notesapplication.alarmFiles.INTIDREPLY";
    public static boolean materialBool = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        TextView timeSetCurrent = findViewById(R.id.timeSetCurrent);
        ExtendedFloatingActionButton finishFab = findViewById(R.id.extended_fab);
        ExtendedFloatingActionButton setTimeFab = findViewById(R.id.setTimeFab);
        EditText clockTitleEdit = findViewById(R.id.alarmNameEdit);

        if(getIntent().getParcelableExtra("selectedAlarm") != null){
            System.out.println("This is an old alarm");
            Alarm alarm = getIntent().getParcelableExtra("selectedAlarm");
            clockTitleEdit.setText(alarm.getName());
            timeSetCurrent.setText("Selected Time is: " + alarm.getTimeHour() + ":" + alarm.getTimeMinute());
        }

        materialBool = false;
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set Time")
                .build();

        setTimeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialBool = true;
                materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
            }
        });

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                timeSetCurrent.setText("Selected Time is: " + materialTimePicker.getHour() + ":" + materialTimePicker.getMinute());
            }
        });

        finishFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Return Button Has Been Clicked");
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(clockTitleEdit.getText()) || timeSetCurrent.getText().equals("Time Not Set Yet")){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String alarmTitle = clockTitleEdit.getText().toString();
                    String alarmHour = "";
                    String alarmMinute = "";
                    if(materialBool){
                        alarmHour = String.valueOf(materialTimePicker.getHour());
                        alarmMinute = String.valueOf(materialTimePicker.getMinute());
                        if(getIntent().getParcelableExtra("selectedAlarm") != null){
                            replyIntent.putExtra(INTID_REPLY, getIntent().getExtras().getInt("IDALARM"));
                        }
                    }else{
                        if(getIntent().getParcelableExtra("selectedAlarm") != null){
                            Alarm alarm = getIntent().getParcelableExtra("selectedAlarm");
                            replyIntent.putExtra(INTID_REPLY, getIntent().getExtras().getInt("IDALARM"));
                            alarmHour = alarm.getTimeHour();
                            alarmMinute = alarm.getTimeMinute();
                        }
                    }

                    String[]output = {alarmTitle, alarmHour, alarmMinute};
                    replyIntent.putExtra(EXTRA_REPLY, output);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
