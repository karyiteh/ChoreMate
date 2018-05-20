package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InviteHousemateActivity extends AppCompatActivity {

    // UI elements on the screen.
    private EditText editHousemateEmail;
    private Button buttonInvite;

    // Appbar on the screen.
    private Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_housemate);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_invite_housemate);
        setSupportActionBar(appbar);

        // Initialize the view
        editHousemateEmail = (EditText) findViewById(R.id.edit_housemate_email);

        buttonInvite = (Button) findViewById(R.id.btn_invite);

        // Add click listener
        buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvite();
            }
        });
    }

    private void sendInvite() {
        // TODO: GET HOUSE NAME AND CODE FROM DATABASE (FROM HOUSEHOLD OBJECT)
        // TODO: ADD EMAIL ERROR CHECKS
        // Retrieve house name and code from database
        String house_code = "";
        String householdName = "";

        String email = editHousemateEmail.getText().toString().trim();
        String subject = "PLACEHOLDER SUBJECT";
        String message = "Your invite code to " + householdName + " is: " + house_code;

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

}
