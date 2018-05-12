package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Class responsible for renaming the household
 */
public class RenameGroup extends AppCompatActivity {

    private EditText renameGroup;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_group);

        renameGroup = (EditText) findViewById(R.id.new_group_name);
        finishButton = (Button) findViewById(R.id.btn_finish);


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name = renameGroup.getText().toString();

                //TODO: UPDATE NEW_NAME IN DATABASE.

                // Brings the user to household screen
                Intent renameIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(renameIntent);

            }
        });



    }
}
