package com.example.teh_k.ChoreMate;
// used to keep track of the time of tasks
import java.util.Calendar;
// used to create a list of users
import java.util.List;

// Class that will handle the logic of tasks
public class Task {
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

    // time that the task is to occur
    private Calendar time;

    // whether or not the task recurs
    // 0 for non recurring, 1 for recurring
    private boolean recur;
    //TODO: how should we handle when the task recurs, for daily, weekly, monthly, yearly

    //getter and setter for task name and details
    public String getTask_name() {
        return task_name;
    }
    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }
    public String getTask_detail() {
        return task_detail;
    }
    public void setTask_detail(String task_detail) {
        this.task_detail = task_detail;
    }

    //getter and setter for trigger
    public boolean isTrigger() {
        return trigger;
    }
    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    //getter and setter for status
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    //getter and setter for the list of user
    public List<User> getUser_list() {
        return user_list;
    }
    public void setUser_list(List<User> user_list) {
        this.user_list = user_list;
    }

    //getter and setter for the time the task occurs
    public Calendar getTime() {
        return time;
    }
    public void setTime(Calendar time) {
        this.time = time;
    }

    //getter and setter for whether the task recurs
    public boolean isRecur() {
        return recur;
    }
    public void setRecur(boolean recur) {
        this.recur = recur;
    }
}
