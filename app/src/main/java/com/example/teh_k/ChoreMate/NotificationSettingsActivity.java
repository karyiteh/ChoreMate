package com.example.teh_k.ChoreMate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Class that supports the behavior behind the change notification settings screen.
 * TO BE IMPLEMENTED.
 */
public class NotificationSettingsActivity extends AppCompatActivity {

    // UI elements.
    private Switch toggleSnooze;
    private Spinner dropDown;

    // Appbar for the page.
    private Toolbar appbar;

    // How long the user decides to mute the notifications.
    private String snoozeOption;
    private boolean snoozed;

    // String array for the options in the spinner.
    private String[] options = new String[]{"1 hour", "1 day", "1 year", "Forever"};

    /**
     * Loads the user profile screen when the user profile tab is clicked on.
     * @param savedInstanceState    The last instance that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_notification_settings);
        setSupportActionBar(appbar);

        // Sets up the toggle switch.
        toggleSnooze = (Switch) findViewById(R.id.toggle_snooze);

        // Sets up the spinner object.
        dropDown = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        dropDown.setAdapter(adapter);

        // Set up listener for toggle switch.
        toggleSnooze.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is set.
                snoozed = isChecked;
            }
        });

        // Set up listener for spinner object.
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the item selected.
                snoozeOption = (String)parent.getItemAtPosition(position);

                // Set the notification settings to mute for that long.
                setSnoozeNotifications(snoozeOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }

    /**
     * Mutes the notifications for a certain amount of time.
     * @param muteOption    How long to mute the notifications for.
     */
    private void setSnoozeNotifications(String muteOption) {
        // TODO: Mute notifications implementation here.
        // TODO: Ray said it has something to do with database here.

        // Show confirmation of how long the notifications is muted for.
        String message = "Notifications muted for " + muteOption;
        Toast notificationsToast = Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_LONG);
        notificationsToast.show();
    }


}
