package com.example.notesapplication;

import android.annotation.SuppressLint;
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

import com.example.notesapplication.alarmFiles.AlarmActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements noteRecyclerView.OnNoteListener{

    // Material Design

    public static final int NEW_TITLE_ACTIVITY_REQUEST_CODE = 1;
    public static final int OLD_TITLE_ACTIVITY_REQUEST_CODE = 2;
    private NoteViewModel mNoteViewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mainly related to notes application
        setContentView(R.layout.activity_main);
        ExtendedFloatingActionButton createFAB = findViewById(R.id.extendedCreateFab);
        RecyclerView recyclerTitles = findViewById(R.id.recyclerTitles);
        // used for alarm
        ConstraintLayout mainLayout = findViewById(R.id.mainPageLayout);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteRecyclerView recyclerAdapter = new noteRecyclerView(this, this, this, mNoteViewModel);

        recyclerTitles.setAdapter(recyclerAdapter);
        recyclerTitles.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NoteSwipeToDelete(recyclerAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerTitles);

        
        mNoteViewModel.getAllTitles().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the words in the recyclerAdapter.
                recyclerAdapter.setLocalDataSet(notes);
                System.out.println("This is being used");
                noteRecyclerView.ViewHolder.setmDataSet(notes);
            }
        });

        createFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Has Been Clicked");
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
//                intent.putExtra("classCall", "-1");
                startActivityForResult(intent, NEW_TITLE_ACTIVITY_REQUEST_CODE);
            }
        });

        // adding a swipe listener
        mainLayout.setOnTouchListener(new activitySwipeListener(MainActivity.this){
            public void onSwipeRight(){
                System.out.println("This function is being called");
            }

            public void onSwipeLeft(){
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_TITLE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            if(data.hasExtra(CreateNoteActivity.EXTRA_REPLY)){
                // added note text, check for errors here
                String[] intentData = data.getStringArrayExtra(CreateNoteActivity.EXTRA_REPLY);
                System.out.println("The note from this new note is:" + intentData[1]);
                Note note = new Note(intentData[0], intentData[1]);
                mNoteViewModel.insert(note);
            }
        }else if(requestCode == OLD_TITLE_ACTIVITY_REQUEST_CODE){
            if(data.hasExtra(CreateNoteActivity.EXTRA_REPLY)){
                String[]intentData = data.getStringArrayExtra(CreateNoteActivity.EXTRA_REPLY);
                // added note text, check for errors here
                System.out.println("The note data from the old note is: " + intentData[1]);
                int noteID = data.getIntExtra(CreateNoteActivity.INTID_REPLY, -1);
                System.out.println("The note ID is " + noteID);
                Note note = new Note(noteID, intentData[0], intentData[1]);
                mNoteViewModel.updateNoteTitle(note);
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNoteClick(Note note) {
        System.out.println(note.getTitle());
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("IDNOTE", note.getId());
        intent.putExtra("selectedNote", note);
        startActivityForResult(intent, OLD_TITLE_ACTIVITY_REQUEST_CODE);
    }
}
