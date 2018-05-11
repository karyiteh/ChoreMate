package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;

import android.view.View;
import android.view.View.OnClickListener;
import java.util.Random;


// TODO: IMPORTANT NOTE: CAN ONLY ENTER ONE EMAIL RIGHT NOW

public class CreateHouseholdActivity extends AppCompatActivity {
    // Error messages.
    public static final String EMPTY_FIELD = "This field is required!";
    public static final String INVALID_EMAIL = "Please make sure emails are valid and are" +
                                               " entered with the proper format.";

    // Declare Text Fields
    private EditText editHouseholdName;
    private EditText editHousematesList;

    private Button buttonCreate;

    // Declare instance variables
    private Household house = new Household();

    private boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);

        // Initialize the views
        editHouseholdName = (EditText) findViewById(R.id.edit_household_name);
        editHousematesList = (EditText) findViewById(R.id.edit_roommates_list);

        buttonCreate = (Button) findViewById(R.id.btn_create_household);

        // Add click listener
        buttonCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel = false;

                attemptCreate();

                // If House object wasn't created, break out of code
                if (cancel) {
                    return;
                }

                attemptInvite();

                // If invites were not sent, break out of code
                if (cancel) {
                    return;
                }

                // TODO: INTENT TO MAINACTIVITY CRASHES FOR SOME REASON
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptCreate() {
        // Initialize local variables
        View focusView;

        // Parse household name
        String householdName = editHouseholdName.getText().toString().trim();

        // Checks if the Household name is not empty
        if(TextUtils.isEmpty(householdName)) {
            editHouseholdName.setError(EMPTY_FIELD);
            focusView = editHouseholdName;
            focusView.requestFocus();
            cancel = true;

            return;
        }

        // Generate random invite code
        InviteCodeGenerator code = new InviteCodeGenerator(getRandomLength());
        String house_code = code.nextString();

        // TODO: STORE HOUSEHOLD OBJECT IN DATABASE
        // Create new Household object and update fields
        house.setHouse_code(house_code);
        house.setHouse_name(householdName);
    }

    private void attemptInvite() {
        // Initialize local variables
        View focusView;

        // Getting content for email
        String emails = editHousematesList.getText().toString().trim();
        // TODO: Replace [USER] with name of user
        String subject = "[USER] is inviting you to " + house.getHouse_name() + " on ChoreMate!";
        String message = "Your invite code to " + house.getHouse_name() + " is: "
                         + house.getHouse_code();

        // Parse the String of emails
        String emailsList[] = emails.split(", ");

        // If no emails are entered, no invites need to be sent.
        if (noEmailsEntered(emailsList)) {
            return;
        }

        // Check if emails are valid
        // Note: Only works with .edu and .com
        if (isInvalidEmail(emailsList)) {
            editHousematesList.setError(INVALID_EMAIL);
            focusView = editHousematesList;
            focusView.requestFocus();

            cancel = true;
        }

        if (!cancel) {
            // Create SendMail object and send invites
            for (int i = 0; i < emailsList.length; i++) {
                SendMail sm = new SendMail(this, emailsList[i], subject, message);
                sm.execute();
            }
        }
    }

    // Generates a random number between [6, 12], inclusive
    private int getRandomLength() {
        Random rand = new Random();
        int high = 13;
        int low = 6;
        return rand.nextInt(high - low) + low;
    }

    // Returns true if any email in a list of emails does not end with @ucsd.edu
    private boolean isInvalidEmail(String[] emailsList) {
        for (int i = 0; i < emailsList.length; i++) {
            if (!emailsList[i].endsWith("@ucsd.edu")) {
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
