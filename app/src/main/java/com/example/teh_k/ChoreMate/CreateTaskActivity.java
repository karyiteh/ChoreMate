package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CreateTaskActivity extends AppCompatActivity implements RecurringTaskFragment.OnFragmentInteractionListener {
    // Declare Text Fields
    private EditText editTaskName;
    private EditText editTaskDescription;

    private Button buttonRecurrence;
    private Button createTask;

    // Initialize field variables
    private ArrayList<User> housemateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignHousemateAdapter assignHousemateAdapter;
    private DatePicker dueDate;

    private int amountOfTime;
    private String unitOfTime;
    private RecurringTaskFragment recurFrag;

    public Task task;
    private GregorianCalendar calendar;

    private boolean fragmentShown;
    private boolean isRecurring;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // Set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.pick_housemates);

        assignHousemateAdapter = new AssignHousemateAdapter(housemateList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assignHousemateAdapter);

        // TODO: Test purposes. Remove after database implementation
        loadSampleHousemates();

        // Initialize the views
        editTaskName = (EditText) findViewById(R.id.task_name);
        editTaskDescription = (EditText) findViewById(R.id.task_description);

        createTask = (Button) findViewById(R.id.btn_create_task);
        buttonRecurrence = (Button) findViewById(R.id.recurrence_options);

        dueDate = (DatePicker) findViewById(R.id.task_due_date);

        // Intialize the due date to be current date
        dueDate.getAutofillValue();

        // Set up listener for NumberPicker object
        dueDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth){
                dueDate = datePicker;
            }
        });

        // Add click listener for recurrence button
        buttonRecurrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recurFrag == null) {
                    recurFrag = new RecurringTaskFragment();
                    loadFragment(recurFrag);
                }
                else {
                    toggleFragment(recurFrag);
                }
            }
        });

        // Add click listener for create task button
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = new Task();
                createTask(task);
            }
        });
    }

    /**
     * Loads the fragment to the screen.
     * @param fragment  The fragment to be loaded to the screen.
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        fragmentShown = true;
    }

    /**
     * Toggles the visibility of the recurrence options fragment
     * @param fragment The fragment to be toggled.
     */
    private void toggleFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentShown) {
            //ft.replace(R.id.fragment_container, fragment);
            ft.hide(fragment);
            fragmentShown = false;
        }
        else {
            ft.show(fragment);
            fragmentShown = true;
        }
        ft.commit();
    }

    /**
     * Creates the task and saves it to the database.
     * @param task Task object to save in database
     */
    private void createTask(Task task) {
        task.setTask_name(editTaskName.getText().toString().trim());
        task.setTask_detail(editTaskDescription.getText().toString().trim());

        // Get the currently selected housemates by checking CheckBox state of each housemate
        // and add housemate to array if true
        ArrayList<User> selectedHousemates = new ArrayList<>();
        CheckBox checkBox;
        for (int i = 0; i < housemateList.size(); i++) {
            checkBox = recyclerView.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.checkBox);
            if (checkBox.isChecked()) {
                selectedHousemates.add(housemateList.get(i));
            }
        }

        task.setUser_list(selectedHousemates);

        // Set amount and unit of time from recurring options fragment
        if (recurFrag != null && recurFrag.getAmountOfTime() != 0 && recurFrag.getSpinnerOption() != null) {
            getRecurringOptions();
            task.setAmountOfTime(amountOfTime);
            task.setUnitOfTime(unitOfTime);
        }

        // Set the task deadline
        calendar = new GregorianCalendar();
        calendar.set(dueDate.getYear(), dueDate.getMonth(), dueDate.getDayOfMonth());
        task.setTime(calendar);

        // TODO: Add Task object to database
    }

    public void getRecurringOptions() {
        amountOfTime = recurFrag.getAmountOfTime();
        unitOfTime = recurFrag.getSpinnerOption();
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
