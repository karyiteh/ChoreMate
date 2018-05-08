package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoHouseholdActivity extends AppCompatActivity {


    private Button mCreateButton;
    private Button joinButton;
    private EditText inviteCode;

    public static final String dummyCode = "123";


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
        // TODO: Make sure user gets sent to correct group (Ask Database)
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inviteCode.getText().toString();

                if (checkCode(code)) {
                    // Take user to the Tasks page
                    Intent toTask = new Intent(v.getContext(), MainActivity.class);
                    startActivity(toTask);
                }

            }
        });
    }


    //TODO: checkCode with database
    private boolean checkCode(String code){
        inviteCode.setError(null);

        if (isCorrectCode(code)){
            return true;
        }

        else {
            inviteCode.setError("Please Enter a Valid Code");
            return false;
        }
    }

    private boolean isCorrectCode(String code){
        return (code.equals(dummyCode));
    }

}
