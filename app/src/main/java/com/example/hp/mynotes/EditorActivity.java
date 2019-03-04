package com.example.hp.mynotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class EditorActivity extends AppCompatActivity {
    int Noteid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        EditText editText = findViewById(R.id.editText);

        // this will get the list item id
        Intent intent =getIntent();
        Noteid = intent.getIntExtra("Noteid",-1);

        if (Noteid != -1){
            editText.setText(MainActivity.notes.get(Noteid));
        }else {
            // this is to make sure that when we add a new item then we don't get an error
            // and we set the note id of the last item
            MainActivity.notes.add("");
            Noteid = MainActivity.notes.size() -1;
        }

        // this will set the text whatever we write on the editText
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.notes.set(Noteid,String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                // we have to create shared preferences whenever we are working with the editing part
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.hp.mynotes", Context.MODE_PRIVATE);
                HashSet<String> setOfNotes = new HashSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", setOfNotes).apply();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
