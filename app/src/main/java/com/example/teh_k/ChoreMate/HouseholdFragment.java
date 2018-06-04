package com.example.teh_k.ChoreMate;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseholdFragment extends Fragment {

    // UI elements in the fragment.
    private TextView mHouseholdName;
    private RecyclerView mHousemateList;
    private HousemateAdapter housemateListAdapter;
    private RecyclerView.LayoutManager housemateListManager;

    private String householdKey;
    private ArrayList<User> housemates;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public HouseholdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Redirect login screen
                if(firebaseAuth.getCurrentUser() == null){
                }
            }
        };

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
        mHouseholdName = (TextView) getView().findViewById(R.id.txt_household_name);
        mHousemateList = (RecyclerView) getView().findViewById(R.id.housemate_list);

        // Retrieve user list from database.
        populateHouseholdDb();
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
        // Borrowed some of the AlertDialog code from a stack overflow user
        // https://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter New Household Name");

        // For user Input
        final EditText input = new EditText(getContext());

        // What to expect
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Update the name in the database.
                renameHouseholdDb(input.getText().toString());

                // Updates the UI to reflect name change.
                mHouseholdName.setText(input.getText().toString());

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog.
        builder.show();

    }

    /**
     * Starts the delete household activity
     */
    private void deleteHousehold() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure you want to Delete this Household?");


        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Deletes the household from the database.
                deleteHouseholdDb();

                // Brings the user to NoHousehold activity.
                Intent deletedHousehold = new Intent(getContext(), NoHouseholdActivity.class);
                startActivity(deletedHousehold);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog.
        builder.show();
    }

    /**
     * Updates the name of the household in the database.
     * @param newHouseholdName  The new name of the household.
     */
    private void renameHouseholdDb(final String newHouseholdName){

        // Update the new household name to the database.
        final String user_id = mCurrentUser.getUid();
        DatabaseReference mUser = mDatabase.child("Users").child(user_id);
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the user object and the household key.
                User user = dataSnapshot.getValue(User.class);
                String householdKey = user.getHousehold();

                // Get the household reference.
                final DatabaseReference mHousehold = mDatabase.child("Households");
                mHousehold.child(householdKey).child("house_name").setValue(newHouseholdName)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Show confirm message.
                                Toast.makeText(getActivity(), "Household Renamed",
                                        Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show confirm message.
                        Toast.makeText(getActivity(), "Error",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error in renaming household.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Gets the housemates that are in that household.
     */
    private void populateHouseholdDb(){
        String user_id = mCurrentUser.getUid();
        DatabaseReference mUser = mDatabase.child("Users");
        mUser.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                householdKey = user.getHousehold();
                Log.d("HouseholdFregment", "Applying household: " + householdKey );

                // Getting the household name from db.
                DatabaseReference mHousehold = mDatabase.child("Households").child(householdKey);
                mHousehold.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Getting the household object from the database and setting it.
                        Household household = dataSnapshot.getValue(Household.class);
                        mHouseholdName.setText(household.getHouse_name());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                });

                // Getting housemates from household.
                Query mQueryUserHousemates = mDatabase.child("Users").orderByChild("household").equalTo(householdKey);
                mQueryUserHousemates.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        housemates = new ArrayList<User>();

                        for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                            User user = userSnapshot.getValue(User.class);
                            housemates.add(user);
                            Log.d("HouseholdFragment", "Adding housemate: " + user.getLast_name() );
                        }

                        // Creates the adapter.
                        housemateListAdapter = new HousemateAdapter(housemates);

                        // Attaches the adapter to the view.
                        mHousemateList.setAdapter(housemateListAdapter);

                        // Sets layout manager.
                        housemateListManager = new LinearLayoutManager(getContext());
                        mHousemateList.setLayoutManager(housemateListManager);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Deletes the household and all the data related to the household from the database.
     */
    private void deleteHouseholdDb(){

        // delete that household
        DatabaseReference mCurrHousehold = mDatabase.child("Households").child(householdKey);
        mCurrHousehold.removeValue();

        // do in parallel.
        deleteHouseholdUsersDb(householdKey);
        deleteHouseholdTaskDb(householdKey);
        deleteHouseholdPaymentsDb(householdKey);

    }

    /**
     * Deletes all the housemates in the household.
     * @param householdKey  The household key in the database.
     */
    private void deleteHouseholdUsersDb(String householdKey){

        // set all the user from that household reference to ''.
        Query mQueryUsers = mDatabase.child("Users").orderByChild("household").equalTo(householdKey);

        mQueryUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Users").child(userSnapshot.getKey()).child("household").setValue("");

                    // clear balances between all the users.
                    deleteHouseholdBalanceDb(userSnapshot.getKey());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Deletes all the tasks in the household from the database.
     * @param householdKey  The key of the household in the database.
     */
    private void deleteHouseholdTaskDb(String householdKey){

        // delete all the tasks from that household
        Query mQueryUserTasks = mDatabase.child("Tasks").orderByChild("household").equalTo(householdKey);

        mQueryUserTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Tasks").child(taskSnapshot.getKey()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Deletes all payments in the household from the database.
     * @param householdKey  The key of the household in the database.
     */
    private void deleteHouseholdPaymentsDb(String householdKey){

        // delete all the tasks from that household
        Query mQueryUserPayments = mDatabase.child("Payments").orderByChild("household").equalTo(householdKey);

        mQueryUserPayments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot paymentSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Payments").child(paymentSnapshot.getKey()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Deletes all the balances of the housemates from the database.
     * @param uid   The user ids of the housemates in the household.
     */
    private void deleteHouseholdBalanceDb(String uid){

        // delete all the tasks from that household
        Query mQueryUserBalances = mDatabase.child("Balances").orderByChild("uid").equalTo(uid);

        mQueryUserBalances.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Balances").child(balanceSnapshot.getKey()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
