package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoHouseholdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_household);

        // Set up listener for the Create Household button
        Button mCreateButton = (Button) findViewById(R.id.btn_create_household);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Brings the user to the Create Household screen.
                Intent registerIntent = new Intent(view.getContext(), CreateHouseholdActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
