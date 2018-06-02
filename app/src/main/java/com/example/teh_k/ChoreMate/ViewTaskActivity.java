package com.example.teh_k.ChoreMate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


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

    // Next user assigned to the task for a rotational task and his index.
    private User userNext;
    private int newIndex;

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
        DatabaseReference mUser = mDatabase.child("Users");

        // If theres a list of user -> task is rotational
        if(task.getUser_list().size() > 1){

            // normalize the index
            newIndex = task.getIndex() + 1;
            if(newIndex == task.getUser_list().size()){
                newIndex = 0;
            }

            // get uid of next user
            String userNextUid = task.getUser_list().get(newIndex);

            // get next user object from db
            mUser.child(userNextUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userNext = dataSnapshot.getValue(User.class);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ViewTaskAvtivity", "Error.");
                }
            });
        } else {

            // otherwise set next user to null;
            userNext = null;

        }

        mUser.child(task.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
                Log.d("ViewTaskAvtivity", "Target user :" + user.getLast_name());
                textUser.setText(user.getFirst_name());
                // TODO: Do more processing to the date to be displayed nicely.
                String dueDate = task.getTime();
                textDueDate.setText(dueDate);
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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        skipTask();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ViewTaskAvtivity", "Error.");
            }
        });
    }

    private void finishTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you have completed this task?");

        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // TODO: Remove the task from the task list and database
                deleteTaskDb();

                // if next user is not null; reschedule and assign to new user;
                if(userNext != null && task.isRecur()){
                    Task newTask = task;

                    // set new thumbnails
                    newTask.setIndex(newIndex);
                    newTask.setUid(userNext.getUid());
                    newTask.setHousemateAvatar(userNext.getAvatar());

                    // TODO: Reschedule and assign to new user
                    assignTaskDb(rescheduleTaskDb(newTask));
                }

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
                deleteTaskDb();

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void skipTask() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to delete this task?");

        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // TODO: Remove the task from the task list and database
                deleteTaskDb();

                // TODO: Assign the task to the next person in line according to database
                // if next user is not null; reschedule and assign to new user;
                if(userNext != null && task.isRecur()){
                    Task newTask = task;

                    // set new thumbnails
                    newTask.setIndex(newIndex);
                    newTask.setUid(userNext.getUid());
                    newTask.setHousemateAvatar(userNext.getAvatar());

                    // TODO: Assign to new user
                    assignTaskDb(newTask);
                }

                Intent skip = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(skip);

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

    private void deleteTaskDb() {

        mDatabase.child("Tasks").child(task.getKey()).removeValue();
        Log.d("ViewTaskAvtivity", "Deleted task: " + task.getTask_name());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Task rescheduleTaskDb(Task newTask) {

        // get current time
        LocalDateTime now = LocalDateTime.now();

        // if the task is recurring
        if (newTask.isRecur()) {

            int futureTime = newTask.getAmountOfTime();

            // Rescheduling.
            switch (newTask.getUnitOfTime()) {
                case "day(s)":
                    now = now.plusDays(futureTime);
                    break;
                case "week(s)":
                    now = now.plusWeeks(futureTime);
                    break;
                case "month(s)":
                    now = now.plusMonths(futureTime);
                    break;
                case "year(s)":
                    now = now.plusYears(futureTime);
                    break;
            }
        }

        // Set the new deadline
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
        newTask.setTime(formatter.format(now));

        // set db sorting index
        DateTimeFormatter indexingFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String indexHousehold = newTask.getHousehold() + indexingFormatter.format(now);
        String indexUid = newTask.getUid() + indexingFormatter.format(now);

        newTask.setIndexUid(indexUid);
        newTask.setIndexHousehold(indexHousehold);

        return newTask;
    }

    private void assignTaskDb(Task newTask) {

        DatabaseReference mTask = mDatabase.child("Tasks").push();
        String key = mTask.getKey();
        newTask.setKey(key);

        mTask.setValue(newTask.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("ViewTaskAvtivity", "Create Task: success");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ViewTaskAvtivity", "Create Task: failure");
                Toast.makeText(ViewTaskActivity.this, "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
