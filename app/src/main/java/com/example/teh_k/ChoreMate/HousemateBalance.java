package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Object class for storing current balances between user and housemates.
 * Used in User objects.
 */
public class HousemateBalance implements Parcelable{

    // String representing the name of the housemate.
    private String housemateFirstName;
    private String housemateLastName;

    // Avatar of the housemate.
    private Uri housemateAvatar;

    // The balance between the user and the housemate.
    private double balance;

    /**
     * Default empty constructor.
     */
    public HousemateBalance() {
    }

    /**
     * Constructor for housemate balance.
     * @param firstName Housemate's first name.
     * @param lastName  Housemate's last name.
     * @param avatar    Housemate's avatar.
     * @param balance   Housemate's balance.
     */
    public HousemateBalance(String firstName, String lastName, Uri avatar, double balance) {
        housemateFirstName = firstName;
        housemateLastName = lastName;
        housemateAvatar = avatar;
        this.balance = balance;
    }

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

    /**
     * Gets the balance in the currency format.
     * @return  The string that is the balance in the currency format.
     */
    public String getBalanceString() {
        // Formatter to format the string.
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        // Format the string to currency format.
        String balanceString = format.format(Math.abs(balance));

        // Determine whether to put plus or minus in front of the string.
        if(balance >= 0) {
            balanceString = "+ " + balanceString;
        }
        else {
            balanceString = "- " + balanceString;
        }

        return balanceString;
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