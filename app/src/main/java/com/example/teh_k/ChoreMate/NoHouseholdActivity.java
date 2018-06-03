package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

/**
 * An Activity that allows user to create/join a household.
 */
public class NoHouseholdActivity extends AppCompatActivity {

    // UI elements on the screen.
    private Button mCreateButton;
    private Button joinButton;
    private EditText inviteCode;

    // Appbar of the screen.
    private Toolbar appbar;

    // Private variables for the activity.
    private boolean correctCode = false; // This has to be FALSE because if not, any code will pass. and the user can see all the task form the entire database.
    private ArrayList<User> housemateList;
    private List<String> userHousemateBalance;

    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mCurrUser;

    /**
     * Creates the main screen. Called when main page is loaded.
     * @param savedInstanceState    The last instance state that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_household);

        // Set up user database reference.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mCurrUser = mDatabase.child("Users").child(mCurrentUser.getUid());

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_no_household);
        setSupportActionBar(appbar);

        mCreateButton = (Button) findViewById(R.id.btn_create_household);
        joinButton = (Button) findViewById(R.id.btn_join_household);
        inviteCode = (EditText) findViewById(R.id.edit_invite_code);


        // Set up listener for the Create Household button
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Brings the user to the Create Household screen.
                Intent registerIntent = new Intent(view.getContext(), CreateHouseholdActivity.class);
                startActivity(registerIntent);

            }

        });


        // Set up listener for the Join Button
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inviteCode.getText().toString();

                if (checkCode(code)) {

                    // If checkCode is successful, join the household.
                    joinHousehold(code);

                    // Redirects users back to the main sign in page.
                    Intent mainIntent = new Intent(NoHouseholdActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }

            }
        });
    }


    /**
     * Function that handles the code inserted by the user in the text field
     * @param code: Code entered by the user.
     * @return true if the code is correct, false otherwise
     */
    private boolean checkCode(String code){
        // Reset the error message
        inviteCode.setError(null);

        // Check if the code is correct
        if (isCorrectCode(code)){
            return true;
        }

        else {
            // Set the error
            inviteCode.setError("Please Enter a Valid Code");
            return false;
        }
    }


    /**
     * String compare the code entered with the actual household code
     * @param code: Code entered by user
     * @return true if the string matches, false otherwise.
     */
    private boolean isCorrectCode(String code){
        // TODO: Just check whether there is such code in the database.


        return true;
    }

    /**
     * Database code to join the household.
     * @param code  The code that is entered by the user.
     */
    private void joinHousehold(String code) {
        // Search for matching household code in database.
        Query mQueryHouseholdMatch = mDatabase.child("Households").orderByChild("house_code").equalTo(code);
        final DatabaseReference mUser = mDatabase.child("Users");

        mQueryHouseholdMatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot household: dataSnapshot.getChildren()){
                    Household addHousehold =  household.getValue(Household.class);
                    Toast.makeText(NoHouseholdActivity.this, "Added household: " + addHousehold.getHouse_name() , Toast.LENGTH_LONG).show();
                    Log.d("NoHouseholdActivity", "added household: " + addHousehold.getHouse_name());
                    mUser.child(mCurrentUser.getUid()).child("household").setValue(household.getKey());
                }
                updateUserBalances();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NoHouseholdActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Updates the current_balances field of every User in the current Household to include
     * the new User.
     * @return void
     */
    private void updateUserBalances() {
        housemateList = new ArrayList<>();
        userHousemateBalance = new ArrayList<>();

        // Get housemateList from database
        mCurrUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                Query mQueryHousemateMatch = mDatabase.child("Users").orderByChild("household").equalTo(user.getHousehold());

                mQueryHousemateMatch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get current user object.
                        User currUser = new User();
                        for(DataSnapshot housemate: dataSnapshot.getChildren()){
                            User user =  housemate.getValue(User.class);
                            if(user.getUid().equals(mCurrentUser.getUid())){
                                currUser = user;
                            }
                        }

                        for(DataSnapshot housemate: dataSnapshot.getChildren()){
                            User user =  housemate.getValue(User.class);
                            Log.d("NoHouseholdAvtivity", "roommate detected: " + user.getLast_name());

                            // database references
                            DatabaseReference mUserBalances = mDatabase.child("Users").child(user.getUid()).child("current_balances");
                            DatabaseReference mBalances = mDatabase.child("Balances").push();

                            if(!user.getUid().equals(mCurrentUser.getUid())){
                                // create new balance for housemates
                                // generated Balance key
                                String key = mBalances.getKey();

                                List<String> housemateBalance = user.getCurrent_balances();
                                HousemateBalance balance = new HousemateBalance(currUser.getFirst_name(), currUser.getLast_name(),
                                        currUser.getAvatar(), 0);
                                balance.setHousemate_avatar(currUser.getAvatar());
                                balance.setHousemate_uid(currUser.getUid());
                                balance.setUid(user.getUid());
                                balance.setKey(key);

                                if(housemateBalance == null){
                                    housemateBalance = new ArrayList<String>();
                                    housemateBalance.add(key);

                                } else {
                                    housemateBalance.add(key);
                                }

                                // add balance to database
                                mBalances.setValue(balance.toMap());
                                // add current user to balance list
                                mUserBalances.setValue(housemateBalance);

                                // create new balance for current user
                                DatabaseReference mCurrUserBalances = mDatabase.child("Balances").push();
                                // generated Balance key
                                String currUserBalancesKey = mCurrUserBalances.getKey();

                                HousemateBalance currUserBalance = new HousemateBalance(user.getFirst_name(), user.getLast_name(),
                                        user.getAvatar(), 0);
                                balance.setHousemate_avatar(user.getAvatar());
                                currUserBalance.setHousemate_uid(user.getUid());
                                currUserBalance.setUid(mCurrentUser.getUid());
                                currUserBalance.setKey(currUserBalancesKey);

                                // add balance to database
                                mCurrUserBalances.setValue(currUserBalance.toMap());
                                // add key to current user's balance list
                                userHousemateBalance.add(currUserBalancesKey);
                            }
                        }

                        // add amount of #housemate balances key to current user balance list.
                        mCurrUser.child("current_balances").setValue(userHousemateBalance);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(NoHouseholdActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NoHouseholdActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
