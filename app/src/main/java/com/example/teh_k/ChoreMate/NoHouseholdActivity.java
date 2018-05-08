package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * An Activity that allows user to create/join a household
 */
public class NoHouseholdActivity extends AppCompatActivity {


    private Button mCreateButton;
    private Button joinButton;
    private EditText inviteCode;

    //TODO: Replace all instances of dummyCode when database is set up
    public static final String dummyCode = "123";

    /**
     * Creates the main screen. Called when main page is loaded.
     * @param savedInstanceState    The last instance state that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_household);

        mCreateButton = (Button) findViewById(R.id.btn_create_household);
        joinButton = (Button) findViewById(R.id.btn_join_household);
        inviteCode = (EditText) findViewById(R.id.edit_invite_code);


        // Set up listener for the Create Household button
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Brings the user to the Create Household screen.
                Intent registerIntent = new Intent(view.getContext(), CreateHouseholdActivity.class);
                startActivity(registerIntent);

            }

        });


        // Set up listener for the Join Button
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inviteCode.getText().toString();

                if (checkCode(code)) {
                    // TODO: Take user to correct household task page
                    Intent toTask = new Intent(v.getContext(), MainActivity.class);
                    startActivity(toTask);
                }

            }
        });
    }


    /**
     * Function that handles the code inserted by the user in the text field
     * @param code: Code entered by the user.
     * @return true if the code is correct, false otherwise
     * TODO: Add database functionality
     */
    private boolean checkCode(String code){
        // Reset the error message
        inviteCode.setError(null);

        // Check if the code is correct
        if (isCorrectCode(code)){
            return true;
        }

        else {
            // Set the error
            inviteCode.setError("Please Enter a Valid Code");
            return false;
        }
    }


    /**
     * String compare the code entered with the actual household code
     * @param code: Code entered by user
     * @return true if the string matches, false otherwise.
     * TODO: Replace dummyCode with database code
     */
    private boolean isCorrectCode(String code){
        return (code.equals(dummyCode));
    }

}
