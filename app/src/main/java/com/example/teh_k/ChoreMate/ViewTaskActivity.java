package com.example.teh_k.ChoreMate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Button finishBttn;
    private Button remindBttn;
    private Button skipBttn;
    private Button deleteBttn;

    // The task to be displayed.
    private Task task;

    // The user assigned to the task
    private User user;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();

        setContentView(R.layout.activity_view_task);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_view_task);
        setSupportActionBar(appbar);

        // Link the UI elements.
        textUser = findViewById(R.id.text_user);
        textDueDate = findViewById(R.id.text_due_date);
        textTaskInfo = findViewById(R.id.text_task_info);
        finishBttn = findViewById(R.id.button_finish);
        remindBttn = findViewById(R.id.button_remind_task);
        skipBttn = findViewById(R.id.button_skip);
        deleteBttn = findViewById(R.id.button_delete);


        // Gets the intent.
        Intent intent = getIntent();
        task = intent.getParcelableExtra(MainActivity.TASK);

        // Updates the text fields.
        appbar.setTitle(task.getTask_name());

        DatabaseReference mUser = mDatabase.child("Users").child(task.getUser_list().get(0));
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textUser.setText(user.getFirst_name());
        // TODO: Do more processing to the date to be displayed nicely.
        Calendar dueDate = task.getTime();
        textDueDate.setText(dueDate.getTime().toString());
        textTaskInfo.setText(task.getTask_detail());

        // TODO: Set the button listeners and logic.
        finishBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTask();

            }
        });

        deleteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });

        remindBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindTask();
            }
        });

        skipBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipTask();
            }
        });


    }

    private void finishTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you have completed this task?");

        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Remove the task from the task list and database
                mDatabase.child("Tasks").child(task.getKey()).removeValue();

                Intent finish = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(finish);

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

    private void deleteTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to delete this task?");

        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Remove the task from the task list and database
                mDatabase.child("Task").child(task.getKey()).removeValue();

                Intent delete = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(delete);

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

    private void remindTask() {
        // TODO: Somehow remind the person in charge of the task to complete it through firebase
        Intent remind = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(remind);

    }

    private void skipTask() {
        // TODO: Somehow assign the task to the next person in line according to database
        Intent skip = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(skip);
    }


}
