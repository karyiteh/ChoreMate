package com.example.teh_k.ChoreMate;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {
    // Declare Text Fields
    private EditText editTaskName;
    private EditText editTaskDescription;

    private Button buttonRecurrence;
    private Button createTask;

    // Initialize field variables
    private ArrayList<User> housemateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignTaskAdapter assignTaskAdapter;

    public Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // Set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.pick_housemates);

        assignTaskAdapter = new AssignTaskAdapter(housemateList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assignTaskAdapter);

        // TODO: Test purposes. Remove after database implementation
        loadSampleHousemates();

        // Initialize the views
        editTaskName = (EditText) findViewById(R.id.task_name);
        editTaskDescription = (EditText) findViewById(R.id.task_description);

        createTask = (Button) findViewById(R.id.btn_create_task);
        buttonRecurrence = (Button) findViewById(R.id.recurrence_options);

        // Add click listener for recurrence button
        buttonRecurrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new RecurringTaskFragment());
            }
        });

        // Add click listener for create task button
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add Task object to database
                task.setTask_name(editTaskName.getText().toString().trim());
                task.setTask_detail(editTaskDescription.getText().toString().trim());

                // TODO: Add List of users to Task object
                ArrayList<Boolean> selectedPositions = new ArrayList<>();
                for (int i = 0; i < housemateList.size(); i++) {
                    //if (recyclerView.findViewHolderForAdapterPosition(i).itemView)
                }
            }
        });
    }

    /**
     * Loads the fragment to the screen.
     * @param fragment  The fragment to be loaded to the screen.
     * @return  true if load is successful, false otherwise.
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    // TODO: For testing purposes. Remove later after database implementation
    private void loadSampleHousemates() {
        Uri imageUri = Uri.parse("android.resource://com.example.teh_k.ChoreMate/" +
                R.drawable.john_emmons_headshot);

        User user1 = new User("John", "Emmons", imageUri);
        housemateList.add(user1);

        User user2 = new User("John", "Emmons", imageUri);
        housemateList.add(user2);

        User user3 = new User("John", "Emmons", imageUri);
        housemateList.add(user3);
    }
}
