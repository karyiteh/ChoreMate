package com.example.teh_k.ChoreMate;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Class that controls the change password page.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    // Error messages.
    public static final String EMPTY_FIELD = "This field is required!";
    public static final String INVALID_PASSWORD =
            "Password has to be alphanumeric and at least 6 characters long.";
    public static final String EMPTY_PASSWORD_CONFIRM = "Please confirm your password.";
    public static final String PASSWORD_NO_MATCH = "The two passwords do not match.";

    public static final int PASSWORD_LENGTH = 6;

    // UI elements on the screen.
    private EditText editCurrentPassword;
    private EditText editNewPassword;
    private EditText editConfirmPassword;
    private Button mBtnChangePassword;

    private Toolbar appbar;

    // User of the current app.
    private User currentUser;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    /**
     * Loads the user profile screen when the user profile tab is clicked on.
     * @param savedInstanceState    The last instance that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Set up user database reference.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        getUserFromDatabase();

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_change_password);
        setSupportActionBar(appbar);

        // Links UI elements to code.
        editCurrentPassword = findViewById(R.id.edit_current_password);
        editNewPassword = findViewById(R.id.edit_new_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        mBtnChangePassword = findViewById(R.id.btn_change_password);

        // Set up button listener for submit button.
        mBtnChangePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyUtils.HideSoftKeyboard(ChangePasswordActivity.this);
                attemptChangePassword();
            }
        });
    }

    /**
     * Gets the user information from the database.
     * The user data of the current user logged in the app.
     */
    private void getUserFromDatabase() {
        DatabaseReference mCurrUser = mDatabase.child("Users").child(mCurrentUser.getUid());
        mCurrUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChangePasswordActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Performs validation checks against the password entered.
     * If there are form errors, then the errors are highlighted, and no actual submit attempt
     * is made.
     */
    private void attemptChangePassword() {

        // Reset errors.
        editCurrentPassword.setError(null);
        editNewPassword.setError(null);
        editConfirmPassword.setError(null);

        // Store value at change password attempt.
        String currentPassword = editCurrentPassword.getText().toString();
        String newPassword = editNewPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();

        // Initializing local variables.
        boolean cancel = false;
        View focusView = null;

        // Checks whether confirm password matches new password.
        if(TextUtils.isEmpty(confirmPassword)){
            editConfirmPassword.setError(EMPTY_PASSWORD_CONFIRM);
            focusView = editConfirmPassword;
            cancel = true;
        }
        else if(!newPassword.equals(confirmPassword)) {
            editConfirmPassword.setError(PASSWORD_NO_MATCH);
            focusView = editConfirmPassword;
            cancel = true;
        }

        // Checks for a valid password.
        if(TextUtils.isEmpty(newPassword)) {
            editNewPassword.setError(EMPTY_FIELD);
            focusView = editNewPassword;
            cancel = true;
        }
        else if(!isValidPassword(newPassword)) {
            editNewPassword.setError(INVALID_PASSWORD);
            focusView = editNewPassword;
            cancel = true;
        }

        // Checks whether the current password matches the one from database.
        if(TextUtils.isEmpty(currentPassword)) {
            editCurrentPassword.setError(EMPTY_FIELD);
            focusView = editCurrentPassword;
            cancel = true;
        }
        else if(!isCorrectPassword(currentPassword)) {
            editCurrentPassword.setError(getString(R.string.error_incorrect_password));
            focusView = editCurrentPassword;
            cancel = true;
        }

        if(cancel) {
            // There is an error in the form data provided.
            // Don't attempt to submit, and highlight the part where there are errors.
            focusView.requestFocus();
        }
        else {

            // TODO: Change the user's password in the database.
            mCurrentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, "User password updated.", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    /**
     * Checks whether the new password is valid.
     * @param password  The new password to change to.
     * @return  true if new password is valid, false otherwise.
     */
    private boolean isValidPassword(String password) {
        // Checks if the password is at least 6 characters long.
        if(password.length() < PASSWORD_LENGTH) {
            return false;
        }

        // Goes through each character to check if it is alphanumeric.
        for(int index = 0; index < password.length(); index++) {

            // Get the current character in the password.
            char currChar = password.charAt(index);
            if(!Character.isLetterOrDigit(currChar)) {
                return false;
            }
        }

        // Password is valid, return true.
        return true;
    }

    /**
     * Checks whether the current password entered matches the one from database.
     * @param password  The current password entered by the user.
     * @return  true if current password matches database, false otherwise.
     */
    private boolean isCorrectPassword(String password) {

        return currentUser.getPassword().equals(password);

    }
}
