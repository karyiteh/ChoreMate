package com.example.teh_k.ChoreMate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Controls the password recovery page.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    // The UI elements on the screen.
    private Toolbar appbar;
    private EditText mEditEmail;
    private Button btnSubmit;

    /**
     * Database references.
     */
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_forgot_password);
        setSupportActionBar(appbar);

        // Link the UI elements to the app.
        mEditEmail = findViewById(R.id.edit_email);
        btnSubmit = findViewById(R.id.btn_submit);

        // Set up on-click listener for submit button.
        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Attempts to submit email to database.
                attemptSubmit();
            }
        });
    }

    /**
     * Function to attempt submit the email so that database can send to reset the password.
     */
    private void attemptSubmit(){
        // Reset the errors.
        mEditEmail.setError(null);

        // Store value of email at time of button click.
        String email = mEditEmail.getText().toString();

        // Set up focus view variable.
        boolean cancel = false;
        View focusView = null;

        // Check for valid email, if the user entered one.
        if(TextUtils.isEmpty(email)) {
            mEditEmail.setError("Please enter your account email.");
            focusView = mEditEmail;
            cancel = true;
        }
        else if(!isValidEmail(email)) {
            mEditEmail.setError("Please enter a valid email address.");
            focusView = mEditEmail;
            cancel = true;
        }

        // If there is invalid items entered, do not actually submit email.
        if(cancel) {
            mEditEmail.requestFocus();
        }
        // else send the email to reset the password.
        else {
            boolean success = sendResetPasswordEmail();

            if(success) {
                // Creates a dialog that informs the user that an email is sent to their email.
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage("An email containing the link to reset your password is sent to "
                        + email);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Redirect user to main activity.
                        Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(mainIntent);
                    }
                });

                builder.show();
            }
            // Else display error message.
            else {
                Toast.makeText(this, "Failed to send reset password email.", Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * Checks if the entered email is valid.
     * @param email The email that is entered.
     * @return  true if email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    /**
     * Sends the reset password email.
     * @return true if send reset password email is successful, false otherwise.
     */
    private boolean sendResetPasswordEmail() {
        // The email that is entered.
        final String email = mEditEmail.getText().toString().trim();

        // Sends the reset password email.
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d("ForgotPasswordAvtivity", "Email sent.");
                        }

                    }
                });
        return true;
    }
}
