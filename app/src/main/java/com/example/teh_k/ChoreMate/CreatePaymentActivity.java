package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        // TODO: populate housemates
        DatabaseReference mUser = mDatabase.child("Users").child(mCurrentUser.getUid());
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                householdKey = user.getHousehold();
                Query mQueryHousemateMatch = mDatabase.child("Users").orderByChild("household").equalTo(householdKey);

                mQueryHousemateMatch.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot housemate: dataSnapshot.getChildren()){
                            User user =  housemate.getValue(User.class);
                            if(!user.getUid().equals(mCurrentUser.getUid())){
                                housemateList.add(user);
                                Log.d("CreatePaymentAvtivity", "roommate populated: " + user.getLast_name());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(CreatePaymentActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
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
        Log.d("CreatePaymentAvtivity", "paymentToEach: " + String.valueOf(paymentToEach));

        // TODO: Loading current housemates from DB into housemateList goes here
        // Update Balances for each selected housemate
        for (int i = 0; i < selectedHousemates.size(); i++) {

            // Get uid from housemate
            housemateUid = selectedHousemates.get(i).getUid();

            // TODO: UPDATE BALANCE FOR HOUSEMATE
            Query mQueryHousemateBalances = mDatabase.child("Balances").orderByChild("uid").equalTo(housemateUid);
            mQueryHousemateBalances.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){

                        HousemateBalance myBalance =  balanceSnapshot.getValue(HousemateBalance.class);

                        if (myBalance.getHousemate_uid().equals(mCurrentUser.getUid()))
                        {
                            String key = balanceSnapshot.getKey();
                            mDatabase.child("Balances").child(key).child("balance").setValue(myBalance.getBalance() + paymentToEach);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CreatePaymentActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });

            // TODO: UPDATE BALANCE FOR MY SELF
            Query mQueryUserBalances = mDatabase.child("Balances").orderByChild("housemate_uid").equalTo(housemateUid);
            mQueryUserBalances.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){

                        HousemateBalance housemateBalance =  balanceSnapshot.getValue(HousemateBalance.class);

                        if (housemateBalance.getUid().equals(mCurrentUser.getUid()))
                        {
                            String key = balanceSnapshot.getKey();
                            mDatabase.child("Balances").child(key).child("balance").setValue(housemateBalance.getBalance() - paymentToEach);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CreatePaymentActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });

            // Create payment object for each payment
            payment = new Payment();
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

            // TODO: Add Payment object to database
            DatabaseReference mPayment = mDatabase.child("Payments").push();
            mPayment.setValue(payment.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("CreatePaymentAvtivity", "Create Balance: success");
                    Toast.makeText(CreatePaymentActivity.this, "Payment Created",
                            Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("CreatePaymentAvtivity", "Create Balance:failure");
                    Toast.makeText(CreatePaymentActivity.this, "Error",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        // TODO: Bring user back to MainActivity.class after successful task creation.
        Intent mainIntent = new Intent(CreatePaymentActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}
