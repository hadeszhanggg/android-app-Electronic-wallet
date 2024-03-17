package com.example.electronicwallet.models;

public class User {
    private String username;
    private String password;
    private String email;
    private String address;
    private boolean gender;
    private String date_of_birth;
    public User() {
    }
    public User(String username, String password, String email, String address, boolean gender, String date_of_birth) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public boolean isGender() {
        return gender;
    }
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    public String getDateOfBirth() {
        return date_of_birth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.date_of_birth = dateOfBirth;
    }
    public String getUser()
    {
        return "Username: " +this.username+", Email: "+this.email+", Gender: "+this.gender+", Address: "+this.address+", Password: "+this.password+ ", Date of birth: "+this.date_of_birth;
    }
}

