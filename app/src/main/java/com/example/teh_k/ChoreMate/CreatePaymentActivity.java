package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

public class CreatePaymentActivity extends AppCompatActivity {
    // Initialize UI elements
    private EditText editPaymentName;
    private CheckBox checkBoxSplit;
    private Button createPayment;

    // Intialize field variables
    private ArrayList<User> housemateList = new ArrayList<>();
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

        // Initialize the views
        editPaymentName = (EditText) findViewById(R.id.edit_payment_name);
        checkBoxSplit = (CheckBox) findViewById(R.id.check_split);
        createPayment = (Button) findViewById(R.id.btn_create_payment);

        // Add click listener for create payment button
        createPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment = new Payment();
                createPayment(payment);
            }
        });
    }

    /**
     * Creates the payment and saves it to the database.
     * @param payment Payment object to save in database
     */
    private void createPayment(Payment payment) {
        // Get the currently selected housemates by checking CheckBox state of each housemate
        // and add housemate to array if true
        ArrayList<User> selectedHousemates = new ArrayList<>();
        CheckBox checkBox;
        for (int i = 0; i < housemateList.size(); i++) {
            checkBox = recyclerView.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.checkBox);
            if (checkBox.isChecked()) {
                selectedHousemates.add(housemateList.get(i));
            }
        }

        payment.setPayment_name(editPaymentName.getText().toString().trim());
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
