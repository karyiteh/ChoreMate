package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object class for storing current balances between user and housemates.
 */
public class HousemateBalance implements Parcelable{

    // String representing the name of the housemate.
    private String housemateFirstName;
    private String housemateLastName;

    // Avatar of the housemate.
    private Uri housemateAvatar;

    // The balance between the user and the housemate.
    private double balance;

    // Getter and setter methods.
    public String getHousemateFirstName() {
        return housemateFirstName;
    }

    public void setHousemateFirstName(String firstName) {
        housemateFirstName = firstName;
    }

    public String getHousemateLastName() {
        return housemateLastName;
    }

    public void setHousemateLastName(String lastName) {
        housemateLastName = lastName;
    }

    public Uri getHousemateAvatar() {
        return housemateAvatar;
    }

    public void setHousemateAvatar(Uri avatar) {
        housemateAvatar = avatar;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Following code generated from www.parcelabler.com
    // Used to pass object between activities.

    /**
     * Constructor method for when passing object through activities.
     */
    protected HousemateBalance(Parcel in) {
        housemateFirstName = in.readString();
        housemateLastName = in.readString();
        housemateAvatar = (Uri) in.readValue(Uri.class.getClassLoader());
        balance = in.readDouble();
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
        dest.writeString(housemateFirstName);
        dest.writeString(housemateLastName);
        dest.writeValue(housemateAvatar);
        dest.writeDouble(balance);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of your Parcelable class from a Parcel.
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HousemateBalance> CREATOR = new Parcelable.Creator<HousemateBalance>() {
        @Override
        public HousemateBalance createFromParcel(Parcel in) {
            return new HousemateBalance(in);
        }

        @Override
        public HousemateBalance[] newArray(int size) {
            return new HousemateBalance[size];
        }
    };


}
