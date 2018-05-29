package com.example.teh_k.ChoreMate;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseholdFragment extends Fragment {

    // UI elements in the fragment.
    private RecyclerView mHousemateList;
    private HousemateAdapter housemateListAdapter;
    private RecyclerView.LayoutManager housemateListManager;

    private String newGroupName = "";

    private String householdKey;
    private ArrayList<String> housemateKeys;
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
        mHousemateList = (RecyclerView) getView().findViewById(R.id.housemate_list);

        // TODO: Method to retrieve user list from database.

        String user_id = mCurrentUser.getUid();
        DatabaseReference mUser = mDatabase.child("Users");
        mUser.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                householdKey = user.getHousehold();
                Log.d("HouseholdFregment", "Applying household: " + householdKey );

                Query mQueryUserHousemates = mDatabase.child("Users").orderByChild("household").equalTo(householdKey);

                housemates = new ArrayList<User>();
                mQueryUserHousemates.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                            User user = userSnapshot.getValue(User.class);
                            housemates.add(user);
                            Log.d("HouseholdFregment", "Adding housemate: " + user.getLast_name() );
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
                newGroupName = input.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();

        // TODO: The new group name is stored in newGroupName

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
                // TODO: Delete the household in the database
                final String user_id = mCurrentUser.getUid();
                DatabaseReference mUser = mDatabase.child("Users").child(user_id);
                mUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String householdKey = user.getHousehold();

                        DatabaseReference mHousehold = mDatabase.child("Households").child(householdKey);
                        mHousehold.removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
