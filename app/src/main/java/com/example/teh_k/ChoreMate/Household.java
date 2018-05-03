package com.example.teh_k.ChoreMate;

import java.util.List;

//class that handles the logic of the household
public class Household {
    // household name
    private String house_name;
    // household code that will be used to invite other users
    private String house_code;

    // lists for users, tasks, and payments
    private List<User> user_list;
    private List<Task> task_list;
    private List<Payment> payment_list;

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
}
