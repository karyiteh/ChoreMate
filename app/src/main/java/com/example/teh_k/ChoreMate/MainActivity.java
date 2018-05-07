package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // buttons
    private Button btnLogout;

    // XML elements on the screen.
    private Toolbar appbar;
    private RecyclerView tasklist;
    private RecyclerView.Adapter tasklistAdapter;
    private RecyclerView.LayoutManager tasklistLayout;

    // Intent key.
    public static final String TASK_TITLE = "com.example.teh_k.ChoreMate.TITLE";

    /**
     * Creates the main screen. Called when main page is loaded.
     * @param savedInstanceState    The last instance state that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.logout);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();

        // check if user already signed in.
        // if not redirect to login page
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        // Creates the appbar.
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        // Creates the task list.
        tasklist = findViewById(R.id.tasks);
        tasklistLayout = new LinearLayoutManager(this);
        tasklist.setLayoutManager(tasklistLayout);

        // Test input dataset.
        String[] inputDataset = new String[3];
        inputDataset[0] = "Take out trash";
        inputDataset[1] = "Wash dishes";
        inputDataset[2] = "Vacuum the living room";

        // Creates the adapter for the task list.
        tasklistAdapter = new TaskAdapter(inputDataset);
        tasklist.setAdapter(tasklistAdapter);

    }

    /**
     * Creates the options menu on the app bar.
     * @param menu  The menu item to be passed in.
     * @return  true if options menu is created successfully.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_appbar, menu);
        return true;
    }
}
