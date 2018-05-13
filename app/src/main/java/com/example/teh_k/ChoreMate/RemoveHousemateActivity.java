package com.example.teh_k.ChoreMate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Class that controls the remove housemate page of the app.
 */
public class RemoveHousemateActivity extends AppCompatActivity {

    // Appbar for the screen.
    private Toolbar appbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_housemate);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_remove_housemate);
        setSupportActionBar(appbar);
    }
}
