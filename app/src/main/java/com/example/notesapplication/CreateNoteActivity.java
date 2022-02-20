package com.example.notesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class CreateNoteActivity extends AppCompatActivity{

    public static final String EXTRA_REPLY = "com.example.notesapplication.REPLY";
    public static final String INTID_REPLY = "com.example.notesapplication.INTIDREPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_section);

        ExtendedFloatingActionButton finishFab = findViewById(R.id.extended_fab);
        EditText titleEdit = findViewById(R.id.TitleEdit);
        EditText notesEdit = findViewById(R.id.notesEdit);

        if (getIntent().getParcelableExtra("selectedNote") != null){
            Note note = getIntent().getParcelableExtra("selectedNote");
            titleEdit.setText(note.getTitle());
//            System.out.println("The note text that I am receiving is: " + note.getNoteText());
            notesEdit.setText(note.getNoteText());
        }

        finishFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(titleEdit.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    // this is when the user is editing a previously created note
                    String title = titleEdit.getText().toString();
                    String noteText = notesEdit.getText().toString();
                    // added note text, check for errors here
                    String[]output = {title, noteText};
                    replyIntent.putExtra(EXTRA_REPLY, output);
                    if(getIntent() != null){
//                        System.out.println("Class Call Back Intent");
                        replyIntent.putExtra(INTID_REPLY, getIntent().getExtras().getInt("IDNOTE"));
                    }
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}