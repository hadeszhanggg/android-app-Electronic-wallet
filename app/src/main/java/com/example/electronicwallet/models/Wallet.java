package com.example.electronicwallet.models;

import java.io.Serializable;

public class Wallet implements Serializable {
    protected String id;
    protected int prestige_score;
    protected Double account_balance;

    public Wallet(String id, int prestige_score, Double account_balance) {
        this.id = id;
        this.prestige_score = prestige_score;
        this.account_balance = account_balance;
    }

    public Wallet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrestige_score() {
        return prestige_score;
    }

    public void setPrestige_score(int prestige_score) {
        this.prestige_score = prestige_score;
    }

    public Double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(Double account_balance) {
        this.account_balance = account_balance;
    }
}
