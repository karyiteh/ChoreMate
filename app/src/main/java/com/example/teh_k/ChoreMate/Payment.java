package com.example.teh_k.ChoreMate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Object class for payments in the app.
 * Implements Parcelable for passing payment object between activities.
 */
public class Payment implements Parcelable {

    private String household;

    // strings representing the names and details of payments
    private String payment_name;
    private String payment_detail;

    // the amount of the payment
    private double amount;

    // the two users involved in the payment
    private String payer;
    private String receiver;

    // whether the payment has been paid or not
    private boolean status;

    public Payment() {
        // Default empty constructor.
    }

    // getters and setters for the payment names and details
    public String getHousehold() {
        return household;
    }
    public void setHousehold(String household) {
        this.household = household;
    }
    public String getPayment_name() {
        return payment_name;
    }
    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }
    public String getPayment_detail() {
        return payment_detail;
    }
    public void setPayment_detail(String payment_detail) {
        this.payment_detail = payment_detail;
    }

    // getter and setter for the amount of the payment
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // getters and setters of the users of the payment
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    // getter and setter for the status of the payment(whether the payment has been made or not)
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    // Code generated from www.parcelabler.com
    // Following section of code used for implementing Parcelable.

    /**
     * Constructor method for when passing object through activities.
     */
    protected Payment(Parcel in) {
        household = in.readString();
        payment_name = in.readString();
        payment_detail = in.readString();
        amount = in.readDouble();
        payer = in.readString();
        receiver = in.readString();
        status = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens the payment object to a Parcelable.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(household);
        dest.writeString(payment_name);
        dest.writeString(payment_detail);
        dest.writeDouble(amount);
        dest.writeString(payer);
        dest.writeString(receiver);
        dest.writeByte((byte) (status ? 0x01 : 0x00));
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of your Parcelable class from a Parcel.
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("household", household);
        result.put("payment_name", payment_name);
        result.put("payment_detail", payment_detail);
        result.put("amount", amount);
        result.put("payer", payer);
        result.put("receiver", receiver);
        result.put("status", status);

        return result;
    }
}
