package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Controls the invite housemate page.
 */
public class InviteHousemateActivity extends AppCompatActivity {

    // UI elements on the screen.
    private EditText editHousemateEmail;
    private Button buttonInvite;

    // Appbar on the screen.
    private Toolbar appbar;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String firstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_housemate);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Redirect login screen
                if(firebaseAuth.getCurrentUser() == null){
                }
            }
        };

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

    /**
     * Checks the information that is entered by the user and invites the housemate.
     */
    private void attemptInvite() {
        // Getting content for email
        final String emails = editHousemateEmail.getText().toString().trim();

        // Replace [USER] with name of user
        // GET HOUSE NAME AND CODE FROM DATABASE (FROM HOUSEHOLD OBJECT)
        final String user_id = mCurrentUser.getUid();
        DatabaseReference mUser = mDatabase.child("Users").child(user_id);
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                firstname = user.getFirst_name();
                String householdKey = user.getHousehold();

                DatabaseReference mHousehold = mDatabase.child("Households").child(householdKey);
                mHousehold.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        View focusView;
                        boolean cancel = false;

                        Household household = dataSnapshot.getValue(Household.class);
                        String householdName = household.getHouse_name();
                        String householdCode = household.getHouse_code();

                        String subject = firstname + " is inviting you to " + householdName + " on ChoreMate!";
                        String message = "Your invite code to " + householdName + " is: " + householdCode;

                        // Parse the String of emails
                        String emailsList[] = emails.split(", ");

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
                                SendMail sm = new SendMail(InviteHousemateActivity.this, email, subject, message);
                                sm.execute();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(InviteHousemateActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });

                Toast.makeText(InviteHousemateActivity.this, "Sent invite", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(InviteHousemateActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Returns true if any email in a list of emails does not end with @ucsd.edu
    private boolean isInvalidEmail(String[] emailsList) {
        for (String email : emailsList) {
            if (!email.contains("@")) {
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
