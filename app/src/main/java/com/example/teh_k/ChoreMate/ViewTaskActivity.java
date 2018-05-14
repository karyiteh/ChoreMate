package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Class that controls the elements on the view task page.
 */
public class ViewTaskActivity extends AppCompatActivity {

    // The appbar for the screen.
    private Toolbar appbar;

    // UI elements on the screen.
    private TextView textUser;
    private TextView textDueDate;
    private TextView textTaskInfo;

    // The task to be displayed.
    private Task task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_view_task);
        setSupportActionBar(appbar);

        // Link the UI elements.
        textUser = findViewById(R.id.text_user);
        textDueDate = findViewById(R.id.text_due_date);
        textTaskInfo = findViewById(R.id.text_task_info);

        // Gets the intent.
        Intent intent = getIntent();
        task = intent.getParcelableExtra(MainActivity.TASK);

        // Updates the text fields.
        appbar.setTitle(task.getTask_name());
        textUser.setText(task.getUser_list().get(0).getFirst_name());
        Calendar dueDate = task.getTime();
        textDueDate.setText(dueDate.getTime().toString());
        textTaskInfo.setText(task.getTask_detail());

    }
}
