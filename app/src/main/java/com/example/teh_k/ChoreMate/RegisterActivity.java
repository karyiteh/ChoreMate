package com.example.teh_k.ChoreMate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The register screen that allows user to register an account with the app.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI elements.
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPasswordConfirm;
    private Button mSubmitButton;

    // Constants.
    public static final int PASSWORD_LENGTH = 6;

    // Error messages.
    public static final String EMPTY_FIELD = "This field is required!";
    public static final String INVALID_NAME = "The name is invalid";
    public static final String INVALID_EMAIL = "Please enter a valid @ucsd.edu email.";
    public static final String INVALID_PASSWORD =
            "Password has to be alphanumeric and at least 6 characters long.";
    public static final String EMPTY_PASSWORD_CONFIRM = "Please confirm your password.";
    public static final String PASSWORD_NO_MATCH = "The two passwords do not match.";

    /**
     * Loads the register screen.
     * @param savedInstanceState    The activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Binds the UI elements to the references in code.
        editFirstName = findViewById(R.id.edit_first_name);
        editLastName = findViewById(R.id.edit_last_name);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editPasswordConfirm = findViewById(R.id.edit_password_confirm);
        mSubmitButton = findViewById(R.id.btn_submit);

        // Set up listener for the submit button.
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });
    }

    /**
     * Checks whether the data that is entered into the form is valid.
     * If there are errors present, the errors are presented and no actual submit is made.
     */
    private void attemptSubmit() {

        // Reset the errors present in the form.
        editFirstName.setError(null);
        editLastName.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);
        editPasswordConfirm.setError(null);

        // Store values at time of submit attempt.
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editPasswordConfirm.getText().toString();

        // Initializing the local variables.
        boolean cancel = false;
        View focusView = null;

        // Checks whether the confirm password matches the valid password.
        if(TextUtils.isEmpty(confirmPassword)) {
            editPasswordConfirm.setError(EMPTY_PASSWORD_CONFIRM);
            focusView = editPasswordConfirm;
            cancel = true;
        }
        else if(!password.equals(confirmPassword)) {
            editPasswordConfirm.setError(PASSWORD_NO_MATCH);
            focusView = editPasswordConfirm;
            cancel = true;
        }

        // Checks for a valid password.
        if(TextUtils.isEmpty(password)) {
            editPassword.setError(EMPTY_FIELD);
            focusView = editPassword;
            cancel = true;
        }
        else if(!isPasswordValid(password)) {
            editPassword.setError(INVALID_PASSWORD);
            focusView = editPassword;
            cancel = true;
        }

        // Checks for a valid email.
        if(TextUtils.isEmpty(email)) {
            editEmail.setError(EMPTY_FIELD);
            focusView = editEmail;
            cancel = true;
        }
        else if(!isEmailValid(email)) {
            editEmail.setError(INVALID_EMAIL);
            focusView = editEmail;
            cancel = true;
        }

        // Checks for a valid last name.
        if(TextUtils.isEmpty(lastName)) {
            editLastName.setError(EMPTY_FIELD);
            focusView = editLastName;
            cancel = true;
        }
        else if(!isNameValid(lastName)) {
            editLastName.setError(INVALID_NAME);
            focusView = editLastName;
            cancel = true;
        }

        // Checks for a valid first name.
        if(TextUtils.isEmpty(firstName)) {
            editFirstName.setError(EMPTY_FIELD);
            focusView = editFirstName;
            cancel = true;
        }
        else if(!isNameValid(firstName)) {
            editFirstName.setError(INVALID_NAME);
            focusView = editFirstName;
            cancel = true;
        }

        if(cancel) {
            // There is an error in the form data provided.
            // Don't attempt to submit, and highlight the part where there are errors.
            focusView.requestFocus();
        }
        else {
            // TODO: Kick off background task to register the user to the database.
        }
    }

    /**
     * Checks if the name entered is valid.
     * @param name  The name to be checked.
     * @return  True if the name is valid, false otherwise.
     */
    private boolean isNameValid(String name) {

        // Goes through each character, check if it is only alphabets.
        for(int index = 0; index < name.length(); index++) {

            char currLetter = name.charAt(index);
            if(!Character.isLetter(currLetter) && !Character.isWhitespace(currLetter)) {
                return false;
            }
        }

        // Returns true because name is valid.
        return true;
    }

    /**
     * Checks if the email entered is a valid @ucsd.edu email.
     * @param email The email to be checked.
     * @return  True if the email is valid, false otherwise.
     */
    private boolean isEmailValid(String email) {

        // Returns if the domain of the email is ucsd.
        return email.endsWith("@ucsd.edu");
    }

    /**
     * Checks if the password entered is valid (password is alphanumeric).
     * @param password  The password to be entered.
     * @return  True if the password is valid, false otherwise.
     */
    private boolean isPasswordValid(String password) {

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
}
