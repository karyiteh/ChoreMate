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
                attemptInvite();
            }
        });
    }

    private void attemptInvite() {
        // Initialize local variables
        View focusView;

        String houseCode = "";
        String householdName = "";

        boolean cancel = false;

        // Getting content for email
        String emails = editHousemateEmail.getText().toString().trim();
        // TODO: Replace [USER] with name of user
        // TODO: GET HOUSE NAME AND CODE FROM DATABASE (FROM HOUSEHOLD OBJECT)
        String subject = "[USER] is inviting you to " + householdName + " on ChoreMate!";
        String message = "Your invite code to " + householdName + " is: " + houseCode;

        // Parse the String of emails
        String emailsList[] = emails.split(", ");

        // If no emails are entered, no invites need to be sent.
        if (noEmailsEntered(emailsList)) {
            return;
        }

        // Check if emails are valid
        if (isInvalidEmail(emailsList)) {
            editHousemateEmail.setError(getString(R.string.error_invalid_email_list));
            focusView = editHousemateEmail;
            focusView.requestFocus();

            cancel = true;
        }

        if (!cancel) {
            // Create SendMail object and send invites
            for (String email : emailsList) {
                SendMail sm = new SendMail(this, email, subject, message);
                sm.execute();
            }
        }
    }

    // Returns true if any email in a list of emails does not end with @ucsd.edu
    private boolean isInvalidEmail(String[] emailsList) {
        for (String email : emailsList) {
            if (!email.endsWith("@ucsd.edu")) {
                return true;
            }
        }

        return false;
    }

    // Returns true if no emails were entered
    private boolean noEmailsEntered(String[] emailsList) {
        if (emailsList[0].equals("")) {
            return true;
        }

        return false;
    }
}
