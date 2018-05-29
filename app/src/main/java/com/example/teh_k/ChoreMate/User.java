package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Object class for the user of the app.
 * Implements Parcelable to be able to be passed through activities.
 */
public class User implements Parcelable{
    private String uid;

    // Strings representing the user's first and last name
    private String first_name;
    private String last_name;

    // strings representing the user's email and password
    private String email;
    private String password;

    // The avatar of the user.
    private Uri avataruri;

    // household that the user belongs to
    private String household;

    // The current balances of the user and their housemates.
    private ArrayList<HousemateBalance> current_balances;

    public User() {
        // Default empty constructor.
    }

    // TODO: Constructor used for testing. Remove later
    public User(String first_name, String last_name, Uri avatar) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.avataruri = avatar;
    }
    // getters and setters for users' uid
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
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
        return avataruri;
    }

    public void setAvatar(Uri avatar) {
        this.avataruri = avatar;
    }

    // getters and setters for the household
    public String getHousehold() {
        return household;
    }
    public void setHousehold(String household) {
        this.household = household;
    }

    // Getters and setters for the current balances.
    public ArrayList<HousemateBalance> getCurrent_balances() { return current_balances;}
    public void setCurrent_balances(ArrayList<HousemateBalance> housemateBalances) {
        current_balances = housemateBalances;
    }

    // Code generated from www.parcelabler.com
    // Following section of code used for implementing Parcelable.

    /**
     * Constructor method for when passing object through activities.
     */
    protected User(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        password = in.readString();
        avataruri = (Uri) in.readValue(Uri.class.getClassLoader());
        household = in.readString();
        if (in.readByte() == 0x01) {
            current_balances = new ArrayList<HousemateBalance>();
            in.readList(current_balances, HousemateBalance.class.getClassLoader());
        } else {
            current_balances = null;
        }
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
        dest.writeValue(avataruri);
        dest.writeValue(household);
        if (current_balances == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(current_balances);
        }
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("email", email);
        result.put("first_name", first_name);
        result.put("last_name", last_name);
        result.put("password", password);
        result.put("avataruri", avataruri);
        result.put("household", password);

        return result;
    }

}
