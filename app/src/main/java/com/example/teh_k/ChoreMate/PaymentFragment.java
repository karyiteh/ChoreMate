package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Display the payments in MainActivity.java
 */
public class PaymentFragment extends Fragment {

    // UI elements in the task fragment.
    private RecyclerView mPaymentList;
    private PaymentAdapter paymentListAdapter;
    private RecyclerView.LayoutManager paymentListManager;

    // Actual data of the payment list
    private ArrayList<HousemateBalance> housematePayment;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    public PaymentFragment() {
        // Required empty constructor.
    }

    /**
     * Creates the fragment instance to be loaded.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tells Android that it has its own options menu on the appbar.
        setHasOptionsMenu(true);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();
    }


    /**
     * Parses the layout from the layout file and sets it as the fragment.
     * @return  The view of the fragment that is inflated from the layout file.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_view, container, false);
    }


    /**
     * Does final initializing of the items on the fragment.
     * Sets up listeners for items in the fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Gets UI elements from the view.
        if(getView()!= null) {
            mPaymentList = getView().findViewById(R.id.housemate_balances);
        }

        // TODO: get payment list form database.
        housematePayment = initializePayments();

        Query mQueryHouseholdPayments = mDatabase.child("Balances").orderByChild("uid").equalTo(mCurrentUser.getUid());
        mQueryHouseholdPayments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){

                    HousemateBalance housemateBalance = balanceSnapshot.getValue(HousemateBalance.class);
                    Log.d("PaymentFregment", "Housemate balances: " + housemateBalance.getHousemate_first_name());
                    housematePayment.add(housemateBalance);

                }

                // Creates the adapter.
                paymentListAdapter = new PaymentAdapter(housematePayment);
                mPaymentList.setAdapter(paymentListAdapter);

                // Creates the layout manager.
                paymentListManager = new LinearLayoutManager(getContext());
                mPaymentList.setLayoutManager(paymentListManager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Creates the options menu for tasks.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasks_payment, menu);
    }

    /**
     * Handles click events on the menu item selected.
     * @param item  The menu item that is selected by the user.
     * @return  true if item is processed accordingly.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch(item.getItemId()) {
            case R.id.action_create:
                // Method to create task.
                createNewPayment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        paymentListAdapter.clear();
        housematePayment.clear();
    }

    /**
     * Gets the housemates and their respective balances.
     * @return  The list of housemates together with their balances.
     */
    private ArrayList<HousemateBalance> initializePayments() {
        ArrayList<HousemateBalance> housemateList = new ArrayList<HousemateBalance>();
        return housemateList;
    }

    /**
     * Starts intent to create new payment.
     */
    private void createNewPayment() {
        // TODO: Method to create the new payment.
        // Redirects to CreatePaymentActivity.
        Intent createPaymentIntent = new Intent(getActivity(), CreatePaymentActivity.class);
        startActivity(createPaymentIntent);
    }
}
