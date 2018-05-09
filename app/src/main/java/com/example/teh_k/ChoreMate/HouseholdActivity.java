package com.example.teh_k.ChoreMate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HouseholdActivity extends AppCompatActivity {

    private Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household);

        // Creates the app bar.
        appbar = findViewById(R.id.appbar_household);
        setSupportActionBar(appbar);
    }

    /**
     * Creates the options menu on the app bar.
     * @param menu  The menu item to be passed in.
     * @return  true if options menu is created successfully.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_household, menu);
        return true;
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
            case R.id.action_rename_household:
                // TODO: Method to rename household.
                return true;
            case R.id.action_delete_household:
                // TODO: Method to delete household.
                return true;
            case R.id.action_invite_housemate:
                // TODO: Method to invite housemate.
                return true;
            case R.id.action_remove_housemate:
                // TODO: Method to remove housemate.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
