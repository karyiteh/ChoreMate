package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.Button;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.Random;


// TODO: IMPORTANT NOTE: CAN ONLY ENTER ONE EMAIL RIGHT NOW

public class CreateHouseholdActivity extends AppCompatActivity {

    // Declare Text Fields
    private EditText editHouseholdName;
    private EditText editRoommatesList;

    private Button buttonCreate;

    // Declare invite code generator
    private InviteCodeGenerator code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);

        // Initialize the views
        editHouseholdName = (EditText) findViewById(R.id.edit_household_name);
        editRoommatesList = (EditText) findViewById(R.id.edit_roommates_list);

        buttonCreate = (Button) findViewById(R.id.btn_create_household);

        // Add click listener
        buttonCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendEmail() {
        // Generate random invite code
        // TODO: STORE INVITE CODE IN HOUSEHOLD DATABASE
        code = new InviteCodeGenerator(getRandomLength());

        //Getting content for email
        String householdName = editHouseholdName.getText().toString().trim();
        String email = editRoommatesList.getText().toString().trim();
        String subject = "PLACEHOLDER SUBJECT";
        String message = "Your invite code to " + householdName + " is: " + code.nextString();

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    // Generates a random number between [6, 12], inclusive
    public int getRandomLength() {
        Random rand = new Random();
        int high = 13;
        int low = 6;
        return rand.nextInt(high - low) + low;
    }
}
