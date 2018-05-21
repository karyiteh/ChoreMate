package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

        // Get the task list from the database.
        housematePayment = initializePayments();

        // Creates the adapter.
        paymentListAdapter = new PaymentAdapter(housematePayment);
        mPaymentList.setAdapter(paymentListAdapter);

        // Creates the layout manager.
        paymentListManager = new LinearLayoutManager(getContext());
        mPaymentList.setLayoutManager(paymentListManager);
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

    /**
     * Gets the housemates and their respective balances.
     * @return  The list of housemates together with their balances.
     */
    private ArrayList<HousemateBalance> initializePayments() {
        ArrayList<HousemateBalance> housemateList = new ArrayList<HousemateBalance>();
        Uri imageUri = Uri.parse("android.resource://com.example.teh_k.ChoreMate/" +
                R.drawable.john_emmons_headshot);
        HousemateBalance housemate1 = new HousemateBalance("John", "Eammons", imageUri, 20);
        housemateList.add(housemate1);
        return housemateList;
    }

    /**
     * Starts intent to create new payment.
     */
    private void createNewPayment() {
        // TODO: Method to create the new payment.
    }
}
