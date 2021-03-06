package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private RecyclerView recyclerView;
    private AssignHousemateAdapter assignHousemateAdapter;

    public Payment payment;

    private String householdKey;
    private double paymentToEach;
    private String housemateUid;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    /**
     * Database listeners
     */
    private Query mQueryHousemateMatch;
    private ValueEventListener mHousemateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_payment);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();

        // Set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_housemates);

        assignHousemateAdapter = new AssignHousemateAdapter(housemateList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(assignHousemateAdapter);

        // populate housemates from the database.
        DatabaseReference mUser = mDatabase.child("Users").child(mCurrentUser.getUid());
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                householdKey = user.getHousehold();
                mQueryHousemateMatch = mDatabase.child("Users").orderByChild("household").equalTo(householdKey);

                mQueryHousemateMatch.addValueEventListener( initializeHouseholdListener() );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreatePaymentActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });

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
                MyUtils.HideSoftKeyboard(CreatePaymentActivity.this);
                createPayment(false);
            }
        });

        // Add click listener for create charge button
        createCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.HideSoftKeyboard(CreatePaymentActivity.this);
                createPayment(true);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();

        if(this.mHousemateListener != null) {
            mQueryHousemateMatch.removeEventListener(this.mHousemateListener);
        }

    }

    @Override
    public void onStop(){
        super.onStop();

        if(this.mHousemateListener != null) {
            mQueryHousemateMatch.removeEventListener(this.mHousemateListener);
        }

    }

    /**
     * Creates the payment and saves it to the database.
     * @param isCharge  Whether the payment is a charge.
     */
    private void createPayment(boolean isCharge) {
        View focusView;

        String paymentName = editPaymentName.getText().toString().trim();
        String paymentToEachStr = editPaymentAmount.getText().toString().trim();

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
        if (!isCharge) {
            paymentToEach = - paymentToEach;
        }

        // Getting the housemate array.
        SparseBooleanArray selectedHousematesArray = assignHousemateAdapter.getItemStateArray();

        // Debugging purposes. printing out contents of selectedHousematesArray
        Log.d("CreatePaymentActivity", "Size of selectedHousematesArray: " +
                selectedHousematesArray.size());
        for(int i = 0; i < selectedHousematesArray.size(); i++) {
            Log.d("CreatePaymentActivity", "selectedHousematesArray[" + i + "] = " +
                    selectedHousematesArray.get(i));
        }

        /* Get the currently selected housemates by checking CheckBox state of each housemate
           and add housemate to array if true */
        ArrayList<User> selectedHousemates = new ArrayList<>();
        for (int i = 0; i < housemateList.size(); i++) {
            // If the housemate is selected, add housemate to selected housemates.
            if (selectedHousematesArray.get(i)) {
                selectedHousemates.add(housemateList.get(i));
            }
        }

        // Makes sure that at least one housemate is selected.
        if(selectedHousemates.size() == 0){
            // Set error and focus view
            focusView = recyclerView;
            focusView.requestFocus();
            Toast.makeText(CreatePaymentActivity.this, "Please select at least one housemate.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Calculate total payments. If checkbox is split, split the payment amount
        if (checkBoxSplit.isChecked()) {
            paymentToEach = paymentToEach / selectedHousemates.size();
            paymentToEach = (double) Math.round(paymentToEach * 100) / 100;
        }
        Log.d("CreatePaymentAvtivity", "paymentToEach: " + String.valueOf(paymentToEach));

        // Loading current housemates from DB into housemateList goes here
        // Update Balances for each selected housemate
        for (int i = 0; i < selectedHousemates.size(); i++) {

            // Get uid from housemate
            housemateUid = selectedHousemates.get(i).getUid();

            // Update balance for current user in the database.
            Query mQueryHousemateBalances = mDatabase.child("Balances").orderByChild("uid").equalTo(housemateUid);
            mQueryHousemateBalances.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){

                        HousemateBalance myBalance =  balanceSnapshot.getValue(HousemateBalance.class);

                        if (myBalance.getHousemate_uid().equals(mCurrentUser.getUid()))
                        {
                            String key = balanceSnapshot.getKey();
                            mDatabase.child("Balances").child(key).child("balance").setValue(myBalance.getBalance() - paymentToEach);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CreatePaymentActivity.this, "Error Create payment", Toast.LENGTH_LONG).show();
                }
            });

            // Update balance for housemates in the database.
            Query mQueryUserBalances = mDatabase.child("Balances").orderByChild("housemate_uid").equalTo(housemateUid);
            mQueryUserBalances.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){

                        HousemateBalance housemateBalance =  balanceSnapshot.getValue(HousemateBalance.class);

                        if (housemateBalance.getUid().equals(mCurrentUser.getUid()))
                        {
                            String key = balanceSnapshot.getKey();
                            mDatabase.child("Balances").child(key).child("balance").setValue(housemateBalance.getBalance() + paymentToEach);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CreatePaymentActivity.this, "Error Create payment", Toast.LENGTH_LONG).show();
                }
            });

            // Create payment object for each payment
            payment = new Payment();
            payment.setHousehold(householdKey);
            payment.setPayment_name(paymentName);
            payment.setAmount(paymentToEach);

            // Set the payer and the receiver accordingly to whether it is a charge.
            if(isCharge) {
                payment.setPayer(selectedHousemates.get(i).getUid());
                payment.setReceiver(mCurrentUser.getUid());
            }
            else {
                payment.setPayer(mCurrentUser.getUid());
                payment.setReceiver(selectedHousemates.get(i).getUid());
            }

            // Add Payment object to database
            DatabaseReference mPayment = mDatabase.child("Payments").push();
            mPayment.setValue(payment.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("CreatePaymentActivity", "Create Balance: success");
                    Toast.makeText(CreatePaymentActivity.this, "Payment Created",
                            Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("CreatePaymentActivity", "Create Balance:failure");
                    Toast.makeText(CreatePaymentActivity.this, "Error Create payment",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Bring user back to MainActivity.class after successful task creation.
        Intent mainIntent = new Intent(CreatePaymentActivity.this, MainActivity.class);
        mainIntent.putExtra(MainActivity.FRAGMENT, 'p');
        startActivity(mainIntent);
    }

    private ValueEventListener initializeHouseholdListener() {

        mHousemateListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get the housemate value and add it to the list.
                for(DataSnapshot housemate: dataSnapshot.getChildren()){
                    User user =  housemate.getValue(User.class);
                    housemateList.add(user);
                    Log.d("CreateTaskActivity", "roommate populated: " + user.getLast_name());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreatePaymentActivity.this, "Error Create payment", Toast.LENGTH_LONG).show();
            }
        };

        return mHousemateListener;
    }
}
