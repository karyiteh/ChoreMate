package com.example.teh_k.ChoreMate;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Class that controls the elements on the housemate profile page.
 */
public class HousemateProfileActivity extends AppCompatActivity {

    // Appbar on the screen.
    private Toolbar appbar;

    // UI elements on the screen.
    private CircleImageView avatar;
    private TextView mHousemateName;
    private Button mBtnRemoveHousemate;

    // The list view for the tasks.
    private RecyclerView mTaskList;
    private TaskAdapter taskListAdapter;
    private RecyclerView.LayoutManager taskListManager;

    // The housemate to be displayed.
    private User currentHousemate;
    private ArrayList<Task> currentHousemateTask;
    private ArrayList<User> currentHousemateList = new ArrayList<User>();

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mFirestore;

    /**
     * Database listeners
     */
    private Query mQueryUserTask;
    private ValueEventListener mUserTaskListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Redirect login screen
                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(HousemateProfileActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
                    Intent loginIntent = new Intent(HousemateProfileActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }

            }
        };

        setContentView(R.layout.activity_housemate_profile);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_displayhousemateprofile);
        setSupportActionBar(appbar);

        // Linking the UI elements.
        avatar = findViewById(R.id.avatar);
        mHousemateName = findViewById(R.id.housemate_username);
        mTaskList = findViewById(R.id.user_task_scroll);
        mBtnRemoveHousemate = findViewById(R.id.btn_remove_housemate);

        // Getting the intent and therefore, the housemate.
        Intent intent = getIntent();
        currentHousemate = intent.getParcelableExtra(MainActivity.HOUSEMATE);

        // Update the fields.
        Picasso.get().load(Uri.parse(currentHousemate.getAvatar())).into(avatar);
        String housemateName = currentHousemate.getFirst_name() + " " + currentHousemate.getLast_name();
        mHousemateName.setText(housemateName);

        // Getting the user task list from the database.
        getTasksFromDatabase();

        // Set up the listener for remove housemate button.
        mBtnRemoveHousemate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRemoveHousemate();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();

        if(this.mUserTaskListener != null) {
            mQueryUserTask.removeEventListener(this.mUserTaskListener);
        }

    }

    @Override
    public void onStop(){
        super.onStop();

        if(this.mUserTaskListener != null) {
            mQueryUserTask.removeEventListener(this.mUserTaskListener);
        }

    }

    /**
     * Gets the tasks for the current user.
     * @return  A list of tasks that is assigned to the current user.
     */
    private void getTasksFromDatabase() {
        currentHousemateTask = new ArrayList<Task>();

        // Gets housemate's tasks from database.
        mQueryUserTask = mDatabase.child("Tasks").orderByChild("indexUid").startAt(currentHousemate.getUid()).endAt(currentHousemate.getUid()+ "\uf8ff");

        mQueryUserTask.addValueEventListener( initializeHouseholdListener() );
    }

    /**
     * Sets up prompt to remove the housemate.
     */
    private void attemptRemoveHousemate() {
        // Get the user name.
        String housemateName = currentHousemate.getFirst_name();

        // Set up the dialog prompt.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove " + housemateName);

        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteHousemateDb();

                // Send the toast message as confirmation.
                String message = "Removed " + currentHousemate.getFirst_name();

                Toast confirmation = Toast.makeText(HousemateProfileActivity.this, message, Toast.LENGTH_LONG);
                confirmation.show();

                if(currentHousemate.getUid().equals(mCurrentUser.getUid())) {

                    // redirect user to noHouseholdActivity.
                    Intent noHouseholdIntent = new Intent(HousemateProfileActivity.this, NoHouseholdActivity.class);
                    startActivity(noHouseholdIntent);

                } else {
                    // redirect user to household fragment.
                    Intent householdIntent = new Intent(HousemateProfileActivity.this, MainActivity.class);
                    householdIntent.putExtra(MainActivity.FRAGMENT, 'h');
                    startActivity(householdIntent);
                }


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

    /**
     * Deletes the housemate from the database.
     */
    private void deleteHousemateDb(){

        Query mQueryUsers = mDatabase.child("Users").orderByChild("household").equalTo(currentHousemate.getHousehold());

        mQueryUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){

                    Log.d("HousemateProfAvtivity", "found user " + userSnapshot.getValue(User.class).getLast_name());
                    currentHousemateList.add(userSnapshot.getValue(User.class));

                }

                // Delete this household from the housemate
                deleteHousemateTaskDb(currentHousemate.getUid());
                deleteHousemateBalanceDb(currentHousemate.getUid());
                deleteHousematePaymentDb(currentHousemate.getUid());
                mDatabase.child("Users").child(currentHousemate.getUid()).child("household").setValue("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HousemateProfileActivity.this, "Error housemate profile", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Deletes all the housemate's balance in the database.
     * @param uid   The housemate's uid.
     */
    private void deleteHousemateBalanceDb(String uid){

        // delete our balances to the housemate
        Query mQueryUserBalances = mDatabase.child("Balances").orderByChild("housemate_uid").equalTo(uid);

        mQueryUserBalances.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Balances").child(balanceSnapshot.getKey()).removeValue();
                    removeHousemateBalanceListDb(balanceSnapshot.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HousemateProfileActivity.this, "Error  housemate profile", Toast.LENGTH_LONG).show();
            }
        });

        // delete the housemate's balances to the us
        Query mQueryHousemateBalances = mDatabase.child("Balances").orderByChild("uid").equalTo(uid);

        mQueryHousemateBalances.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot balanceSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Balances").child(balanceSnapshot.getKey()).removeValue();
                    removeHousemateBalanceListDb(balanceSnapshot.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HousemateProfileActivity.this, "Error  housemate profile", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Deletes all the housemate's task in the database.
     * @param uid   The housemate's uid.
     */
    private void deleteHousemateTaskDb(String uid){

        // delete all the tasks from that household
        Query mQueryUserTasks = mDatabase.child("Tasks").orderByChild("uid").equalTo(uid);

        mQueryUserTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Tasks").child(taskSnapshot.getKey()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HousemateProfileActivity.this, "Error  housemate profile", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Delete all the payments related to this housemate in the database.
     * @param uid   The uid of the housemate.
     */
    private void deleteHousematePaymentDb(String uid){

        // delete all payment from the housemate
        Query mQueryPayerPayments = mDatabase.child("Payments").orderByChild("payer").equalTo(uid);

        mQueryPayerPayments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot paymentSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Payments").child(paymentSnapshot.getKey()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HousemateProfileActivity.this, "Error  housemate profile", Toast.LENGTH_LONG).show();
            }
        });

        // delete all payment to the housemate
        Query mQueryReceiverPayments = mDatabase.child("Payments").orderByChild("receiver").equalTo(uid);

        mQueryReceiverPayments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot paymentSnapshot: dataSnapshot.getChildren()){
                    mDatabase.child("Payments").child(paymentSnapshot.getKey()).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HousemateProfileActivity.this, "Error  housemate profile", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Removes all the housemate balances related to the housemate.
     * @param balanceKey The balance key of the user in the database.
     */
    private void removeHousemateBalanceListDb(final String balanceKey){

        for( User user : currentHousemateList) {

            List<String> balance_list = user.getCurrent_balances();

            for (int i = 0; i < user.getCurrent_balances().size(); i++) {

                if (balance_list.get(i).equals(balanceKey)) {
                    Log.d("HousemateProfAvtivity", "remove balance Key " + balanceKey);
                    balance_list.remove(i);
                    break;
                }

            }

            mDatabase.child("Users").child(user.getUid()).child("current_balances").setValue(balance_list);
        }
    }

    private ValueEventListener initializeHouseholdListener() {
        mUserTaskListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                    currentHousemateTask.add(taskSnapshot.getValue(Task.class));

                }

                // Set up the adapter for the recycler view.
                taskListAdapter = new TaskAdapter(currentHousemateTask);
                mTaskList.setAdapter(taskListAdapter);
                taskListManager = new LinearLayoutManager(HousemateProfileActivity.this);
                mTaskList.setLayoutManager(taskListManager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HousemateProfileActivity.this, "Error  housemate profile", Toast.LENGTH_LONG).show();
            }
        };
        return mUserTaskListener;
    }
}
