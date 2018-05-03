package com.example.teh_k.ChoreMate;

// class that handles the logic of payments
public class Payment {
    // strings representing the names and details of payments
    private String payment_name;
    private String payment_detail;

    // the amount of the payment
    private double amount;

    // the two users involved in the payment
    private User payer;
    private User receiver;

    // whether the payment has been paid or not
    private boolean status;

    // getters and setters for the payment names and details
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
    public User getPayer() {
        return payer;
    }
    public void setPayer(User payer) {
        this.payer = payer;
    }
    public User getReceiver() {
        return receiver;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    // getter and setter for the status of the payment(whether the payment has been made or not)
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
