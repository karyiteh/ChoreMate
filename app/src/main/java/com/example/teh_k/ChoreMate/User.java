package com.example.teh_k.ChoreMate;

import android.net.Uri;

// This class will handle the logic of the user
public class User {
    // Strings representing the user's first and last name
    private String first_name;
    private String last_name;

    // strings representing the user's email and password
    private String email;
    private String password;

    // The avatar of the user.
    private Uri avatar;

    // household that the user belongs to
    private Household household;

    // getters and setters for users' first and last names
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    // getters and setters for email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // getters and setters for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getters and setters for the avatar.
    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    // getters and setters for the household
    public Household getHousehold() {
        return household;
    }
    public void setHousehold(Household household) {
        this.household = household;
    }
}
