package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Class that controls the elements for activity_housemate_transaction_history.xml
 */
public class HousemateTransactionHistoryActivity extends AppCompatActivity {

    // The housemate for the balance.
    private HousemateBalance housemate;

    // UI elements.
    private TextView mHousemateNameText;
    private TextView mHousemateBalanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housemate_transaction_history);

        // Initialize the elements.
        mHousemateNameText = findViewById(R.id.text_housemate_name);
        mHousemateBalanceText = findViewById(R.id.text_balance);

        // Get the housemate from the intent.
        Intent intent = getIntent();
        housemate = intent.getParcelableExtra(MainActivity.HOUSEMATE);

        // Set the housemate name and balance.
        mHousemateBalanceText.setText(housemate.getBalanceString());
        mHousemateNameText.setText(housemate.getHousemateFirstName());

        // TODO: Implement the RecyclerView.
    }
}
