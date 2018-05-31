package com.example.teh_k.ChoreMate;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Displays the tasks in MainActivity.java.
 */
public class TaskFragment extends Fragment {

    // UI elements in the task fragment.
    private RecyclerView mTasklist;
    private TaskAdapter taskListAdapter;
    private RecyclerView.LayoutManager taskListManager;

    private String user_id;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Task list.
    private ArrayList<Task> tasks;

    public TaskFragment() {
        // Required empty public constructor
    }


    /**
     * Creates the fragment instance to be loaded.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();
        user_id = mCurrentUser.getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Redirect login screen
                if(firebaseAuth.getCurrentUser() == null){
                }
            }
        };

        // Tells Android that it has its own options menu on the appbar.
        setHasOptionsMenu(true);
    }


    /**
     * Parses the layout from the layout file and sets it as the fragment.
     * @return  The view of the fragment that is inflated from the layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_view, container, false);
    }


    /**
     * Does final initializing of the items on the fragment.
     * Sets up listeners for items in the fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Gets UI elements from the view.
        if(getView()!= null) {
            mTasklist = getView().findViewById(R.id.tasks);
        }

        // Get the task list from the database.
        initializeTasks();
    }

    /**
     * Creates the options menu for tasks.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasks_payment, menu);
    }

    /**
     * Handles click events on the menu item selected.
     * @param item  The menu item that is selected by the user.
     * @return  true if item is processed accordingly.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch(item.getItemId()) {
            case R.id.action_create:
                // Method to create task.
                createNewTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // PRIVATE METHODS!
    /**
     * Fetches the task list from the database.
     */
    private void initializeTasks() {
        Log.d("TaskFragment", "Inside initializeTask");
        tasks = new ArrayList<Task>();

        // TODO: populate task (from database)
        DatabaseReference mUser = mDatabase.child("Users").child(user_id);
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String householdKey = user.getHousehold();

                Query mQueryUserTask = mDatabase.child("Tasks").orderByChild("household").equalTo(householdKey);

                mQueryUserTask.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                            Task task = taskSnapshot.getValue(Task.class);
                            Log.d("TaskFregment", "Task populated: " + task.getTask_name());
                            tasks.add(task);
                        }

                        // Creates the adapter.
                        taskListAdapter = new TaskAdapter(tasks);
                        mTasklist.setAdapter(taskListAdapter);

                        // Creates the layout manager.
                        taskListManager = new LinearLayoutManager(getContext());
                        mTasklist.setLayoutManager(taskListManager);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Starts intent to create new task.
     */
    private void createNewTask() {
        // Intent to create the new task.
        Intent intent = new Intent(getContext(), CreateTaskActivity.class);
        startActivity(intent);
    }
}
