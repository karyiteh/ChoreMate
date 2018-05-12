package com.example.teh_k.ChoreMate;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseholdFragment extends Fragment {

    // UI elements in the fragment.
    private RecyclerView mHousemateList;
    private HousemateAdapter housemateListAdapter;
    private RecyclerView.LayoutManager housemateListManager;

    public HouseholdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Telling Android that this fragment has an option menu.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_household, container, false);
    }

    /**
     * Do final initializing of the items in the fragment here.
     * Sets up listeners for elements in the fragment.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get UI elements from the view.
        mHousemateList = (RecyclerView) getView().findViewById(R.id.housemate_list);

        // TODO: Method to retrieve user list from database.

        // Dummy dataset.
        ArrayList<User> housemates = new ArrayList<User>();
        User randomUser = new User();
        randomUser.setFirst_name("John");
        housemates.add(randomUser);

        // Creates the adapter.
        housemateListAdapter = new HousemateAdapter(housemates);

        // Attaches the adapter to the view.
        mHousemateList.setAdapter(housemateListAdapter);

        // Sets layout manager.
        housemateListManager = new LinearLayoutManager(getContext());
        mHousemateList.setLayoutManager(housemateListManager);

    }

    /**
     * Creates the fragment menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_household, menu);
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
            case R.id.action_rename_household:
                renameHousehold();
                return true;
            case R.id.action_delete_household:
                deleteHousehold();
                return true;
            case R.id.action_invite_housemate:
                inviteHousemate();
                return true;
            case R.id.action_remove_housemate:
                // TODO: Method to remove housemate.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts the invite housemate activity.
     */
    private void inviteHousemate() {
        Intent inviteIntent = new Intent(getContext(), InviteHousemateActivity.class);
        startActivity(inviteIntent);
    }

    /**
     * Starts the rename household activity
     */
    private void renameHousehold() {
        // Display pop up menu upon clicking of rename household button
        Intent renameIntent = new Intent(getContext(), RenameGroup.class);
        startActivity(renameIntent);
    }

    private void deleteHousehold() {
        Intent deleteIntent = new Intent(getContext(), DeleteActivity.class);
        startActivity(deleteIntent);
    }
}
