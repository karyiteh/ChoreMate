package com.example.teh_k.ChoreMate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object class for the household that contains the users.
 * Implements Parcelable to be able to be passed through activities.
 */
public class Household implements Parcelable{
    // household name
    private String house_name;
    // household code that will be used to invite other users
    private String house_code;

    // lists for users, tasks, and payments
    private List<User> user_list;
    private List<Task> task_list;
    private List<Payment> payment_list;

    public Household() {
        // Default empty constructor.
    }

    // getters and setters for household name
    public String getHouse_name() {
        return house_name;
    }
    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }

    // getters and setters for household code
    public String getHouse_code() {
        return house_code;
    }
    public void setHouse_code(String house_code) {
        this.house_code = house_code;
    }

    // getters and setters for lists of users, tasks, and payments
    public List<User> getUser_list() {
        return user_list;
    }
    public void setUser_list(List<User> user_list) {
        this.user_list = user_list;
    }
    public List<Task> getTask_list() {
        return task_list;
    }
    public void setTask_list(List<Task> task_list) {
        this.task_list = task_list;
    }
    public List<Payment> getPayment_list() {
        return payment_list;
    }
    public void setPayment_list(List<Payment> payment_list) {
        this.payment_list = payment_list;
    }

    // Code generated from www.parcelable.com
    // Following section of code used for implementing Parcelable.

    /**
     * Constructor method for when passing object through activities.
     */
    protected Household(Parcel in) {
        house_name = in.readString();
        house_code = in.readString();
        if (in.readByte() == 0x01) {
            user_list = new ArrayList<User>();
            in.readList(user_list, User.class.getClassLoader());
        } else {
            user_list = null;
        }
        if (in.readByte() == 0x01) {
            task_list = new ArrayList<Task>();
            in.readList(task_list, Task.class.getClassLoader());
        } else {
            task_list = null;
        }
        if (in.readByte() == 0x01) {
            payment_list = new ArrayList<Payment>();
            in.readList(payment_list, Payment.class.getClassLoader());
        } else {
            payment_list = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens the household object to a Parcelable.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(house_name);
        dest.writeString(house_code);
        if (user_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(user_list);
        }
        if (task_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(task_list);
        }
        if (payment_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(payment_list);
        }
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of your Parcelable class from a Parcel.
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Household> CREATOR = new Parcelable.Creator<Household>() {
        @Override
        public Household createFromParcel(Parcel in) {
            return new Household(in);
        }

        @Override
        public Household[] newArray(int size) {
            return new Household[size];
        }
    };

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("HouseholdName", house_name);
        result.put("HouseholdCode", house_code);
        result.put("MemberUidList", user_list);
        result.put("TaskList", task_list);
        result.put("PaymentList", payment_list);

        return result;
    }
}
