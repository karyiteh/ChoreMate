package com.example.teh_k.ChoreMate;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

    // Task list.
    private ArrayList<Task> taskList;

    public TaskFragment() {
        // Required empty public constructor
    }


    /**
     * Creates the fragment instance to be loaded.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        taskList = initializeTasks();

        // Creates the adapter.
        taskListAdapter = new TaskAdapter(taskList);
        mTasklist.setAdapter(taskListAdapter);

        // Creates the layout manager.
        taskListManager = new LinearLayoutManager(getContext());
        mTasklist.setLayoutManager(taskListManager);
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
     * @return  A list of tasks for the household.
     */
    private ArrayList<Task> initializeTasks() {
        // TODO: Replace code with fetching tasks from database.
        // Dummy task list.
        ArrayList<Task> tasks = new ArrayList<Task>();
        Task task1 = new Task();
        task1.setTask_name("Do the dishes");
        task1.setTask_detail("Dishes needs to be washed as soon as they are used.");
        task1.setTime(new GregorianCalendar(2018, 6, 10, 12,0));
        User user1 = new User();
        user1.setFirst_name("John");
        Uri imageUri = Uri.parse("android.resource://com.example.teh_k.ChoreMate/" +
                R.drawable.john_emmons_headshot);
        user1.setAvatar(imageUri);
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        task1.setUser_list(userList);
        Task task2 = new Task();
        task2.setTask_name("Take out the trash");
        task2.setTask_detail("Trash to be taken out when it is full");
        task2.setTime(new GregorianCalendar(2018, 6, 1, 13, 0));
        task2.setUser_list(userList);
        tasks.add(task1);
        tasks.add(task2);
        tasks.trimToSize();

        // Return the task list obtained from the database.
        return tasks;
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
