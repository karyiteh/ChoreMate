package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Object class for the user of the app.
 * Implements Parcelable to be able to be passed through activities.
 */
public class User implements Parcelable{
    // Strings representing the user's first and last name
    private String first_name;
    private String last_name;

    // strings representing the user's email and password
    private String email;
    private String password;

    // The avatar of the user.
    private Uri avatar;

    // household that the user belongs to
    private Household household;

    public User() {
        // Default empty constructor.
    }

    // getters and setters for users' first and last names
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    // getters and setters for email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // getters and setters for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getters and setters for the avatar.
    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    // getters and setters for the household
    public Household getHousehold() {
        return household;
    }
    public void setHousehold(Household household) {
        this.household = household;
    }

    // Code generated from www.parcelable.com
    // Following section of code used for implementing Parcelable.

    /**
     * Constructor method for when passing object through activities.
     */
    protected User(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        password = in.readString();
        avatar = (Uri) in.readValue(Uri.class.getClassLoader());
        household = (Household) in.readValue(Household.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens the user object to a Parcelable.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeValue(avatar);
        dest.writeValue(household);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of your Parcelable class from a Parcel.
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
