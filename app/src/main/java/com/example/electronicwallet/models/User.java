package com.example.electronicwallet.models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private String address;
    private boolean gender;
    private String date_of_birth;
    private String accesssToken;
    private String refreshToken;
    public User() {
    }
    public User(String id, String username, String password, String email, String address, boolean gender, String date_of_birth, String  accesssToken, String refreshToken) {
        this.id =id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.accesssToken = accesssToken;
        this.refreshToken=refreshToken;
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
    public String getAccesssToken() {
        return accesssToken;
    }
    public void setAccessToken(String accesssToken) {
        this.accesssToken = accesssToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getID() {
        return this.id;
    }
    public void setId(String ID) {
        this.id = ID;
    }
    public String getUser()
    {
        return "ID: "+ this.id+", Username: " +this.username+", Email: "+this.email+", Gender: "+this.gender+", Address: "+this.address+", Password: "+this.password+ ", Date of birth: "+this.date_of_birth+ ", AccessToken: "+this.accesssToken+", refreshToken: "+this.refreshToken;
    }
}

