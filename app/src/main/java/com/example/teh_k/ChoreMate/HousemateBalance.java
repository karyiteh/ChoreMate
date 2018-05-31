package com.example.teh_k.ChoreMate;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Object class for storing current balances between user and housemates.
 * Used in User objects.
 */
public class HousemateBalance implements Parcelable{

    private String key;
    private String uid;

    // String representing the name of the housemate.
    private String housemate_uid;
    private String housemate_first_name;
    private String housemate_last_name;

    // Avatar of the housemate.
    private String housemate_avatar;

    // The balance between the user and the housemate.
    private double balance;

    /**
     * Default empty constructor.
     */
    public HousemateBalance() {
    }

    /**
     * Constructor for housemate balance.
     * @param housemate_first_name Housemate's first name.
     * @param housemate_last_name  Housemate's last name.
     * @param housemate_avatar   Housemate's avatar.
     * @param balance   Housemate's balance.
     */
    public HousemateBalance(String housemate_first_name, String housemate_last_name, String housemate_avatar, double balance) {
        this.housemate_first_name = housemate_first_name;
        this.housemate_last_name = housemate_last_name;
        this.housemate_avatar = housemate_avatar;
        this.balance = balance;
    }

    // Getter and setter methods.
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHousemate_uid() {
        return housemate_uid;
    }

    public void setHousemate_uid(String housemate_uid) {
        this.housemate_uid = housemate_uid;
    }

    public String getHousemate_first_name() {
        return housemate_first_name;
    }

    public void setHousemate_first_name(String housemate_first_name) {
        this.housemate_first_name = housemate_first_name;
    }

    public String getHousemate_last_name() {
        return housemate_last_name;
    }

    public void setHousemate_last_name(String housemate_last_name) {
        this.housemate_last_name = housemate_last_name;
    }

    public String getHousemate_avatar() {
        return housemate_avatar;
    }

    public void setHousemate_avatar(String housemate_avatar) {
        this.housemate_avatar = housemate_avatar;
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
        key = in.readString();
        uid = in.readString();
        housemate_uid = in.readString();
        housemate_first_name = in.readString();
        housemate_last_name = in.readString();
        housemate_avatar = in.readString();
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
        dest.writeString(key);
        dest.writeString(uid);
        dest.writeString(housemate_uid);
        dest.writeString(housemate_first_name);
        dest.writeString(housemate_last_name);
        dest.writeString(housemate_avatar);
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

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uid", uid);
        result.put("housemate_uid", housemate_uid);
        result.put("housemate_firstname", housemate_first_name);
        result.put("housemate_lastname", housemate_last_name);
        result.put("housemate_avatar", housemate_avatar);
        result.put("balance", balance);

        return result;
    }
}
