package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

public class CreatePaymentActivity extends AppCompatActivity {
    // Initialize UI elements
    private EditText editPaymentName;
    private EditText editPaymentAmount;
    private CheckBox checkBoxSplit;
    private Button createPayment;
    private Button createCharge;

    // Initialize field variables
    private ArrayList<User> housemateList = new ArrayList<>();
    private ArrayList<HousemateBalance> balanceList=  new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignHousemateAdapter assignHousemateAdapter;

    public Payment payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_payment);

        // Set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_housemates);

        assignHousemateAdapter = new AssignHousemateAdapter(housemateList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assignHousemateAdapter);

        // TODO: Test purposes. Remove after database implementation
        loadSampleHousemates();
        // TODO: Loading current housemates from DB into housemateList goes here

        // Initialize the views
        editPaymentName = (EditText) findViewById(R.id.edit_payment_name);
        editPaymentAmount = (EditText) findViewById(R.id.edit_payment_amount);
        checkBoxSplit = (CheckBox) findViewById(R.id.check_split);
        createPayment = (Button) findViewById(R.id.btn_create_payment);
        createCharge = (Button) findViewById(R.id.btn_create_charge);

        // Add click listener for create payment button
        createPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPayment(false);
            }
        });

        // Add click listener for create charge button
        createCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayment(true);
            }
        });
    }

    /**
     * Creates the payment and saves it to the database.
     * @param isCharge  Whether the payment is a charge.
     */
    private void createPayment(boolean isCharge) {
        View focusView;
        HousemateBalance currBalance;
        String housemateFirstName;
        String housemateLastName;

        String paymentName = editPaymentName.getText().toString().trim();
        String paymentToEachStr = editPaymentAmount.getText().toString().trim();
        double paymentToEach;

        // Empty field check for payment name
        if (paymentName.equals("")) {
            editPaymentName.setError(getString(R.string.error_field_required));
            focusView = editPaymentName;
            focusView.requestFocus();

            return;
        }

        // Empty field check for payment amount
        if (paymentToEachStr.equals("")) {
            editPaymentAmount.setError(getString(R.string.error_field_required));
            focusView = editPaymentAmount;
            focusView.requestFocus();

            return;
        }

        // Convert payment amount to a double
        paymentToEach = Double.parseDouble(paymentToEachStr);

        // If payment is a charge, then make the amount to be negative.
        if (isCharge) {
            paymentToEach = - paymentToEach;
        }

        /* Get the currently selected housemates by checking CheckBox state of each housemate
           and add housemate to array if true */
        ArrayList<User> selectedHousemates = new ArrayList<>();
        CheckBox checkBox;
        for (int i = 0; i < housemateList.size(); i++) {
            checkBox = recyclerView.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.checkBox);
            if (checkBox.isChecked()) {
                selectedHousemates.add(housemateList.get(i));
            }
        }

        // Calculate total payments. If checkbox is split, split the payment amount
        if (checkBoxSplit.isChecked()) {
            paymentToEach = paymentToEach / selectedHousemates.size();
            paymentToEach = (double) Math.round(paymentToEach * 100) / 100;
        }

        // Update Balances for each selected housemate
        for (int i = 0; i < selectedHousemates.size(); i++) {

            // Get details of housemate
            housemateFirstName = selectedHousemates.get(i).getFirst_name();
            housemateLastName = selectedHousemates.get(i).getLast_name();
            balanceList = selectedHousemates.get(i).getCurrent_balances();

            // Parse through balanceList and check for existing balance between user and housemate
            for (int j = 0; j < balanceList.size(); j++) {
                currBalance = balanceList.get(j);

                if (currBalance.getHousemateFirstName().equals(housemateFirstName) &&
                    currBalance.getHousemateLastName().equals(housemateLastName)) {
                    currBalance.setBalance(currBalance.getBalance() + paymentToEach);
                    break;
                }
            }

            // Create payment object for each payment
            payment = new Payment();
            payment.setPayment_name(paymentName);
            payment.setAmount(paymentToEach);

            // Set the payer and the receiver accordingly to whether it is a charge.
            if(isCharge) {
                payment.setPayer(selectedHousemates.get(i));
                // TODO: payment.setReceiver(DATABASE);
            }
            else {
                // TODO: payment.setPayer(DATABASE)
                payment.setReceiver(selectedHousemates.get(i));
            }
            // TODO Add Payment object to database

            //  TODO: Update current balances of each selected housemate in the Database
        }

        // TODO: UPDATE BALANCE FOR SELF (CURRENT USER) (?) DATABASE NEEDED

        // TODO: Bring user back to MainActivity.class after successful task creation.
    }


    // TODO: For testing purposes. Remove later after database implementation
    private void loadSampleHousemates() {
        Uri imageUri = Uri.parse("android.resource://com.example.teh_k.ChoreMate/" +
                R.drawable.john_emmons_headshot);

        User user1 = new User("John", "Emmons", imageUri);
        housemateList.add(user1);

        User user2 = new User("John", "Emmons", imageUri);
        housemateList.add(user2);

        User user3 = new User("John", "Emmons", imageUri);
        housemateList.add(user3);
    }
}
