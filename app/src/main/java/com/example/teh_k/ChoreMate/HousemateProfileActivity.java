package com.example.teh_k.ChoreMate;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Class that controls the elements on the housemate profile page.
 */
public class HousemateProfileActivity extends AppCompatActivity {

    // Appbar on the screen.
    private Toolbar appbar;

    // UI elements on the screen.
    private CircleImageView avatar;
    private TextView mHousemateName;
    private Button mBtnRemoveHousemate;

    // The list view for the tasks.
    private RecyclerView mTaskList;
    private TaskAdapter taskListAdapter;
    private RecyclerView.LayoutManager taskListManager;

    // The housemate to be displayed.
    private User currentHousemate;
    private ArrayList<Task> currentHousemateTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housemate_profile);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_displayhousemateprofile);
        setSupportActionBar(appbar);

        // Linking the UI elements.
        avatar = findViewById(R.id.avatar);
        mHousemateName = findViewById(R.id.housemate_username);
        mTaskList = findViewById(R.id.user_task_scroll);
        mBtnRemoveHousemate = findViewById(R.id.btn_remove_housemate);

        // Getting the intent and therefore, the housemate.
        Intent intent = getIntent();
        currentHousemate = intent.getParcelableExtra(MainActivity.HOUSEMATE);

        // Update the fields.
        avatar.setImageURI(currentHousemate.getAvatar());
        mHousemateName.setText(currentHousemate.getFirst_name());

        // Getting the user task list from the database.
        currentHousemateTask = getTasksFromDatabase();

        // Set up the adapter for the recycler view.
        taskListAdapter = new TaskAdapter(currentHousemateTask);
        mTaskList.setAdapter(taskListAdapter);
        taskListManager = new LinearLayoutManager(this);
        mTaskList.setLayoutManager(taskListManager);

        // Set up the listener for remove housemate button.
        mBtnRemoveHousemate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRemoveHousemate();
            }
        });
    }

    /**
     * Gets the tasks for the current user.
     * @return  A list of tasks that is assigned to the current user.
     */
    private ArrayList<Task> getTasksFromDatabase() {
        // TODO: Replace code with code that gets tasks from database.
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
     * Sets up prompt to remove the housemate.
     */
    private void attemptRemoveHousemate() {
        // Get the user name.
        String housemateName = currentHousemate.getFirst_name();

        // Set up the dialog prompt.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove " + housemateName);

        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Delete the housemate in the database

                // Send the toast message as confirmation.
                String message = "Remove request sent to " + currentHousemate.getFirst_name();
                Toast confirmation = Toast.makeText(HousemateProfileActivity.this, message, Toast.LENGTH_LONG);
                confirmation.show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
