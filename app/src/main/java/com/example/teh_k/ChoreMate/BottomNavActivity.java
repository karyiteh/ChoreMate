package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class BottomNavActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_tasks:
                    // TODO: Start the tasks activity.
                    mTextMessage.setText(R.string.task_view);
                    return true;
                case R.id.action_transactions:
                    // TODO: Start the transactions activity.
                    mTextMessage.setText(R.string.transaction_view);
                    return true;
                case R.id.action_household:
                    // Starts the household activity.
                    //mTextMessage.setText(R.string.household_view);
                    householdIntent();
                    return true;
                case R.id.action_profile:
                    // Starts the user profile activity.
                    //mTextMessage.setText(R.string.profile_view);
                    profileIntent();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * Starts the household activity.
     */
    private void householdIntent() {
        Intent intent = new Intent(this, HouseholdActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the user profile activity.
     */
    private void profileIntent() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}
