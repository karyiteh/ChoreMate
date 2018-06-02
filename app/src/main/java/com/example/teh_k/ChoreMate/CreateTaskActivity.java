package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String householdKey;
    private String user_id;


    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();

        // Set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.pick_housemates);

        assignHousemateAdapter = new AssignHousemateAdapter(housemateList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assignHousemateAdapter);

        // TODO: populate housemates
        user_id = mCurrentUser.getUid();
        DatabaseReference mUser = mDatabase.child("Users").child(user_id);
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                householdKey = user.getHousehold();
                Query mQueryHousemateMatch = mDatabase.child("Users").orderByChild("household").equalTo(householdKey);

                mQueryHousemateMatch.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot housemate: dataSnapshot.getChildren()){
                            User user =  housemate.getValue(User.class);
                            housemateList.add(user);
                            Log.d("CreateTaskAvtivity", "roommate populated: " + user.getLast_name());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(CreateTaskActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreateTaskActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });

        // Initialize the views
        editTaskName = (EditText) findViewById(R.id.task_name);
        editTaskDescription = (EditText) findViewById(R.id.task_description);

        createTask = (Button) findViewById(R.id.btn_create_task);
        buttonRecurrence = (Button) findViewById(R.id.recurrence_options);

        dueDate = (DatePicker) findViewById(R.id.task_due_date);

        // Initialize the due date to be current date
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

                MyUtils.HideSoftKeyboard(CreateTaskActivity.this);

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
        View focusView;
        task.setTask_name(editTaskName.getText().toString().trim());
        task.setTask_detail(editTaskDescription.getText().toString().trim());

        // Get the currently selected housemates by checking CheckBox state of each housemate
        // and add housemate to array if true
        boolean isFirstUser = true;
        User target = new User();
        ArrayList<String> selectedHousemates = new ArrayList<>();
        CheckBox checkBox;
        for (int i = 0; i < housemateList.size(); i++) {
            checkBox = recyclerView.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.checkBox);
            if (checkBox.isChecked()) {
                // TODO: this task first goes to the first one who appears on the checklist, really?
                selectedHousemates.add(housemateList.get(i).getUid());
                if(isFirstUser){
                    target = housemateList.get(i);
                    isFirstUser = false;
                }
            }
        }

        if(selectedHousemates.size() == 0){
            // Set error and focus view
            focusView = recyclerView;
            focusView.requestFocus();
            Toast.makeText(CreateTaskActivity.this, "Please select at least one housemate.", Toast.LENGTH_LONG).show();
            return;
        }

        task.setUser_list(selectedHousemates);

        // Set amount and unit of time from recurring options fragment
        if (recurFrag != null && recurFrag.getAmountOfTime() != 0 && recurFrag.getSpinnerOption() != null) {
            getRecurringOptions();
            task.setRecur(true);
            task.setAmountOfTime(amountOfTime);
            task.setUnitOfTime(unitOfTime);
        }

        // Set the task deadline
        calendar = new GregorianCalendar();
        calendar.set(dueDate.getYear(), dueDate.getMonth(), dueDate.getDayOfMonth());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        String time = formatter.format(calendar.getTime());
        task.setTime(time);

        SimpleDateFormat indexingFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String indexHousehold = householdKey + indexingFormatter.format(calendar.getTime());
        String indexUid = target.getUid() + indexingFormatter.format(calendar.getTime());

        task.setIndexUid(indexUid);
        task.setIndexHousehold(indexHousehold);
        task.setUid(target.getUid());
        task.setHousemateAvatar(target.getAvatar().toString());
        task.setHousehold(householdKey);
        task.setIndex(0);

        // TODO: Add Task object to database
        DatabaseReference mTask = mDatabase.child("Tasks").push();
        String key = mTask.getKey();
        task.setKey(key);

        mTask.setValue(task.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("CreateTaskAvtivity", "Create Task: success");
                Toast.makeText(CreateTaskActivity.this, "Task Created",
                        Toast.LENGTH_SHORT).show();

                // Redirect user back to the main activity.
                Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CreateTaskAvtivity", "Create Task:failure");
                Toast.makeText(CreateTaskActivity.this, "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRecurringOptions() {
        amountOfTime = recurFrag.getAmountOfTime();
        unitOfTime = recurFrag.getSpinnerOption();
    }
}
