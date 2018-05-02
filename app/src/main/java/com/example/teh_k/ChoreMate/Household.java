package com.example.teh_k.ChoreMate;

import java.util.List;

public class Household {
    private String house_name;
    private List<User> LUser;
    private List<Task> LTask;
    private List<Payment> LPayment;

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }

    public List<User> getLUser() {
        return LUser;
    }

    public void setLUser(List<User> LUser) {
        this.LUser = LUser;
    }

    public List<Task> getLTask() {
        return LTask;
    }

    public void setLTask(List<Task> LTask) {
        this.LTask = LTask;
    }

    public List<Payment> getLPayment() {
        return LPayment;
    }

    public void setLPayment(List<Payment> LPayment) {
        this.LPayment = LPayment;
    }
}
