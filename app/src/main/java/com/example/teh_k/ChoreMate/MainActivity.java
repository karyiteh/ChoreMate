package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    // XML elements on the screen.
    private Toolbar appbar;
    private BottomNavigationView navbar;

    // Intent key.
    public static final String TASK_TITLE = "com.example.teh_k.ChoreMate.TASK_TITLE";

    /**
     * Creates the main screen. Called when main page is loaded.
     * @param savedInstanceState    The last instance state that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        // Loads the default fragment.
        loadFragment(new TaskFragment());

        // Create the bottom nav bar.
        navbar = findViewById(R.id.bottom_profile);
        navbar.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // The fragment to be loaded.
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_tasks:
                // Creates task fragment.
                fragment = new TaskFragment();
                break;
            case R.id.action_transactions:
                // TODO: Create transactions fragment.
                break;
            case R.id.action_household:
                // Creates the household fragment.
                fragment = new HouseholdFragment();
                break;
            case R.id.action_profile:
                // Creates the user profile fragment.
                fragment = new UserProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }

    /**
     * Loads the fragment to the screen.
     * @param fragment  The fragment to be loaded to the screen.
     * @return  true if load is successful, false otherwise.
     */
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
