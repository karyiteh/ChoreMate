package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Class that is responsible for deleting the household.
 */
public class DeleteActivity extends AppCompatActivity {
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        deleteButton = (Button) findViewById(R.id.btn_delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Send notification to all housemates that household is being deleted

                // TODO: Change information in database


                // TODO: Change which screen gets loaded upon deletion
                Intent deleteIntent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(deleteIntent);



            }
        });
    }
}
