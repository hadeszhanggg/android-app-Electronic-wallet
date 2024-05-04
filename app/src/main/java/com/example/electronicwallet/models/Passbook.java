package com.example.electronicwallet.models;

import java.io.Serializable;

public class Passbook implements Serializable {
    private int id;
    private String passbook_name;
    private String description;
    private double interest_rate;
    private int period;

    public Passbook() {
    }

    public Passbook(int id, String passbook_name, String description, double interest_rate, int period) {
        this.id = id;
        this.passbook_name = passbook_name;
        this.description = description;
        this.interest_rate = interest_rate;
        this.period = period;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassbook_name() {
        return passbook_name;
    }

    public void setPassbook_name(String passbook_name) {
        this.passbook_name = passbook_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
