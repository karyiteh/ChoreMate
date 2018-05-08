package com.example.teh_k.ChoreMate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Class that controls the UI elements on the User Profile page.
 */
public class UserProfileActivity extends AppCompatActivity {

    /**
     * Loads the user profile screen when the user profile tab is clicked on.
     * @param savedInstanceState    The last instance that the activity is in.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }
}
