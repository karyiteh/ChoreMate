package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InviteHousemateActivity extends AppCompatActivity {

    private EditText editHousemateEmail;

    private Button buttonInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_housemate);

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
        String message = "Your invite code to " + householdName + " is: " + house_code;;

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

}
