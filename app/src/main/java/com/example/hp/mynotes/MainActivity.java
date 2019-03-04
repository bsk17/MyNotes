package com.example.hp.mynotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("com.example.hp.mynotes", Context.MODE_PRIVATE);

        // this line of code is to make sure that when we start the app then if there is any content
        // before we show it
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(set == null){
            notes.add("MY NOTES");
        }else{
            notes = new ArrayList<>(set);
        }

        ListView listView = findViewById(R.id.listView);
        notes.add("MY NOTE");
        arrayAdapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);

        // function to perform when item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                // we putExtra so that we can identify when we move to editor class
                intent.putExtra("Noteid", position);
                startActivity(intent);
            }
        });

        // function to perform when item is long pressed
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemToDelete = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure ?")
                        .setMessage("Do you want to delete ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();

                                // we have to create shared preferences whenever we are working with the editing part
                                HashSet<String> setOfNotes = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", setOfNotes).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                // we have to mke this true because we have to allow the function to happen
                return true;
            }
        });
    }

    // function to perform when add_item in the menu is clicked

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // this function will move to the editor screen when add item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id._add){
            Intent intent = new Intent(getApplicationContext(),EditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
