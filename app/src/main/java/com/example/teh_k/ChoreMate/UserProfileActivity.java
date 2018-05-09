package com.example.teh_k.ChoreMate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sun.mail.imap.Utility;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Class that controls the UI elements on the User Profile page.
 */
public class UserProfileActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 123;

    /**
     * Constants for user choices.
     */
    private final int OPTION_BROWSE = 0;
    private final int OPTION_USE_CAMERA = 1;
    private final int OPTION_CANCEL = 2;

    private int REQUEST_CAMERA = 0;
    private int BROWSE = 1;

    // UI elements on the screen.
    private Toolbar appbar;
    private CircleImageView mAvatar;
    private TextView mUserName;

    // Current user of the app.
    private User currentUser;
    // Choice made by user.
    private int userChoice;
    private String imageFilePath = "";

    /**
     * Loads the user profile screen when the user profile tab is clicked on.
     * @param savedInstanceState    The last instance that the activity is in.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Loads the view onto the screen.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Creates the app bar.
        appbar = findViewById(R.id.appbar_displayprofile);
        setSupportActionBar(appbar);

        // Links the UI elements to code.
        mAvatar = findViewById(R.id.avatar);
        mUserName = findViewById(R.id.username);

        // Gets the user profile from the database.
        currentUser = getUserFromDatabase();

        // Updates the UI elements to the user profile obtained.
        mAvatar.setImageURI(currentUser.getAvatar());
        mUserName.setText(currentUser.getFirst_name());

        // Set up listener for the avatar to change avatar.
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAvatar();
            }
        });

        // TODO: Implement the recycler view code when tasks are ready.

    }

    /**
     * Creates the options menu in the appbar.
     * @param menu  The menu item to be passed in.
     * @return true if options menu is created successfully.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_user_profile, menu);
        return true;
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
            case R.id.action_changePassword:
                // Call method to start change password activity.
                changePassword();
                return true;
            case R.id.action_notificationSettings:
                // Call method to start notification settings activity.
                changeNotificationSettings();
                return true;
            case R.id.action_logout:
                // Call method to logout.
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Gets the user information from the database.
     * @return  The user data of the current user logged in the app.
     */
    private User getUserFromDatabase() {
        // TODO: Implement obtaining user data from the database.
        // TODO: Replace dummyUser with actual user data from database.
        User dummyUser = new User();
        dummyUser.setFirst_name("TestingJohn");
        Uri imageUri = Uri.parse("android.resource://com.example.teh_k.ChoreMate/" + R.drawable.john_emmons_headshot);
        dummyUser.setAvatar(imageUri);
        return dummyUser;
    }

    /**
     * Changes the avatar of the user.
     */
    private void changeAvatar() {
        // Borrowed code from tutorial:
        // http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample

        // Dialog item list.
        final CharSequence[] options = {"Browse", "Use Camera", "Cancel"};


        // Create a pop-up dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Checks if permission is granted.
                boolean allowed = mayRequestStoragePermission();
                // Store which user choice.
                userChoice = which;

                // Calls the appropriate intents based on the options chosen by the user.
                if (which == OPTION_BROWSE) {
                    // User wants to browse their gallery.
                    if(allowed) {
                        browseIntent();
                    }
                }
                else if(which == OPTION_USE_CAMERA) {
                    // User wants to open their camera.
                    if(allowed) {
                        cameraIntent();
                    }
                }
                else if(which == OPTION_CANCEL) {
                    dialog.dismiss();
                }
            }
        });

        // Display the pop-up dialog.
        builder.show();
    }

    /**
     * Starts the change password activity.
     */
    private void changePassword() {
        // Creates the change password intent and starts the activity.
        Intent changePasswordIntent = new Intent(this, ChangePasswordActivity.class);
        startActivity(changePasswordIntent);
    }

    /**
     * Starts the change notification settings activity.
     */
    private void changeNotificationSettings() {
        // Creates the change notification settings intent and starts the activity.
        Intent changeNotificationSettingsIntent = new Intent(this, NotificationSettingsActivity.class);
        startActivity(changeNotificationSettingsIntent);
    }

    /**
     * Logs the user out of the app.
     */
    private void logout () {
        // TODO: Implement user log out here.
    }

    /**
     * Checks if app has permission to read/write from the gallery.
     * @return true if permission is allowed.
     */
    private boolean mayRequestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            Snackbar.make(mAvatar, "Permission required for selecting avatar.", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
                                    REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    /**
     * Callback received when a permissions request is completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check for what permission is requested.
        if(requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            // Checks whether permission is granted.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Launches appropriate intent.
                if(userChoice == OPTION_BROWSE) {
                    browseIntent();
                }
                else if(userChoice == OPTION_USE_CAMERA) {
                    cameraIntent();
                }
            }
        }
    }

    /**
     * Opens up user gallery for avatar change.
     */
    private void browseIntent() {
        // Implicit intent to open the gallery on the phone.
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//

        // Returns the image chosen in the gallery.
        startActivityForResult(Intent.createChooser(intent, "Select Avatar"), BROWSE);
    }

    /**
     * Opens up camera for avatar change.
     */
    private void cameraIntent() {
        // Create intent to open camera.
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create file location to store the image taken.
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Open the camera to take the picture.
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName()
                    +".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_CAMERA);
        }
    }

    /**
     * Method to create the image file to store in the phone.
     * @return  The File object for the image captured.
     * @throws IOException Exception thrown when there is an error with creating the file.
     */
    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;

    }

    /**
     * Obtain the result from both intents to be processed.
     * @param requestCode   What is passed into startActivityForResult.
     * @param resultCode    RESULT_OK if the operation was successful.
     * @param data          The intent that carries the resulting image.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if operation is successful.
        if(resultCode == Activity.RESULT_OK) {
            // Check to see if image is from camera or from gallery.
            if(requestCode == BROWSE) {
                onBrowseResult(data);
            }
            else if(requestCode == REQUEST_CAMERA) {
                onCameraResult(data);
            }
        }
    }

    /**
     * Handles the request from obtaining image from the gallery.
     * @param data  The intent that carries the resulting image.
     */
    @SuppressWarnings("deprecation")
    private void onBrowseResult(Intent data) {
        // Get the image URI.
        Uri image = null;
        if(data != null) {
            image = data.getData();
        }

        // Updates the current user.
        currentUser.setAvatar(image);
        mAvatar.setImageURI(image);

        // TODO: Upload the image to the database.

    }

    /**
     * Handles the request from obtaining image from the camera.
     * @param data  The intent that carries the resulting image.
     */
    private void onCameraResult(Intent data) {
        // Get the image URI.
        Uri image = Uri.parse(imageFilePath);

        // Updates the current user.
        currentUser.setAvatar(image);
        mAvatar.setImageURI(image);

        // TODO: Upload the image into the database.
    }
}
