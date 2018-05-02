package com.example.teh_k.ChoreMate;
// used to create a list of users
import java.util.List;

public class Task {
    //name of the task, set by the user
    private String task_name;
    //details of the task, set by the user
    private String task_detail;
    //whether the task is "triggered" by another user or occurs at set times
    private boolean trigger;
    //recurrence? TODO: figure out what status is
    private boolean status;
    //list of users that the task will be accomplished by
    private List<User> LUser;
    // TODO: what is time's type?
    //private time

    //getter for task name
    public String getTask_name() {
        return task_name;
    }
    //setter for task name
    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    //getter for task details
    public String getTask_detail() {
        return task_detail;
    }
    //setter for task details
    public void setTask_detail(String task_detail) {
        this.task_detail = task_detail;
    }
    //getter for trigger
    public boolean isTrigger() {
        return trigger;
    }
    //setter for trigger
    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }
    //getter for status
    public boolean isStatus() {
        return status;
    }
    //setter for status
    public void setStatus(boolean status) {
        this.status = status;
    }

    //getter for the list of user
    public List<User> getLUser() {
        return LUser;
    }
    public void setLUser(List<User> LUser) {
        this.LUser = LUser;
    }
}
