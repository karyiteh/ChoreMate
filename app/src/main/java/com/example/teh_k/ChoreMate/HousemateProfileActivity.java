package com.example.teh_k.ChoreMate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Class that controls the elements on the housemate profile page.
 */
public class HousemateProfileActivity extends AppCompatActivity {

    // Appbar on the screen.
    private Toolbar appbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housemate_profile);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_displayhousemateprofile);
        setSupportActionBar(appbar);
    }
}
