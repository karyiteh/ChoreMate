package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Activity screen that displays the individual task.
 */
public class DisplayTaskActivity extends AppCompatActivity {

    // XML Components on the screen.
    private Toolbar appbar;
    private TextView taskTitle;

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        // Initializing the elements on the screen.
        appbar = findViewById(R.id.appbar_displaytask);
        taskTitle = findViewById(R.id.tasktitle);

        // Making the app bar.
        setSupportActionBar(appbar);

        // Setting the up button in the app bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting the intent that started this activity, and extract what task it was.
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.TASK_TITLE);

        // Set the text view to the task title.
        taskTitle.setText(title);

    }
}
