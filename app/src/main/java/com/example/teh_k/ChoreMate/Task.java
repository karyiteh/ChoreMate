package com.example.teh_k.ChoreMate;
// used to keep track of the time of tasks
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
// used to create a list of users
import java.util.List;

/**
 * The object class for the tasks.
 * Implements Parcelable to be able to be passed between activities.
 */
public class Task implements Parcelable {
    //name of task name and details, set by the user
    private String task_name;
    private String task_detail;

    //whether the task is "triggered" by another user or occurs at set times
    // 0 for no trigger, 1 for trigger
    private boolean trigger;

    //whether the task is standby or in progress
    // 0 for standby, 1 for in progress
    private boolean status;

    //list of users that the task will be accomplished by
    private List<User> user_list;

    // Date that the task is to occur
    // NOTE: MONTH STARTS FROM 0. 0 - January, 1 - February, etc.
    private Calendar deadline;

    // whether or not the task recurs
    // 0 for non recurring, 1 for recurring
    private boolean recur;

    private int amountOfTime;
    private String unitOfTime;
    //TODO: how should we handle when the task recurs, for daily, weekly, monthly, yearly

    public Task() {
        // Default empty constructor.
    }

    //getter and setter for task name and details
    public String getTask_name() { return task_name; }
    public void setTask_name(String task_name) { this.task_name = task_name; }

    public String getTask_detail() { return task_detail; }
    public void setTask_detail(String task_detail) { this.task_detail = task_detail; }

    //getter and setter for trigger
    public boolean isTrigger() { return trigger; }
    public void setTrigger(boolean trigger) { this.trigger = trigger; }

    //getter and setter for status
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    //getter and setter for the list of user
    public List<User> getUser_list() { return user_list; }
    public void setUser_list(ArrayList<User> user_list) { this.user_list = user_list; }

    //getters and setters for the task deadline
    public Calendar getDeadline() { return deadline; }
    public void setDeadline(int year, int month, int date) { deadline.set(year, month, date); }

    //getter and setter for whether the task recurs
    public boolean isRecur() { return recur; }
    public void setRecur(boolean recur) { this.recur = recur; }

    //getter and setter for amount of time
    public int getAmountOfTime() { return amountOfTime; }
    public void setAmountOfTime(int amountOfTime) { this.amountOfTime = amountOfTime; }

    //getter and setter for unit of time
    public String getUnitOfTime() { return unitOfTime; }
    public void setUnitOfTime(String unitOfTime) { this.unitOfTime = unitOfTime;
    }

    // Code generated from www.parcelabler.com
    // Following section of code used for implementing Parcelable.
    protected Task(Parcel in) {
        task_name = in.readString();
        task_detail = in.readString();
        trigger = in.readByte() != 0x00;
        status = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            user_list = new ArrayList<User>();
            in.readList(user_list, User.class.getClassLoader());
        } else {
            user_list = null;
        }
        deadline = (Calendar) in.readValue(Calendar.class.getClassLoader());
        recur = in.readByte() != 0x00;
        amountOfTime = in.readInt();
        unitOfTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task_name);
        dest.writeString(task_detail);
        dest.writeByte((byte) (trigger ? 0x01 : 0x00));
        dest.writeByte((byte) (status ? 0x01 : 0x00));
        if (user_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(user_list);
        }
        dest.writeValue(deadline);
        dest.writeByte((byte) (recur ? 0x01 : 0x00));
        dest.writeInt(amountOfTime);
        dest.writeString(unitOfTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}