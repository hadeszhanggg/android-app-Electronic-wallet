package com.example.electronicwallet.models;

import java.util.Date;

public class Transaction {
    protected String id,content;
    protected Double amount;
    protected String date;
    protected int tranTypeId;
    public Transaction(String id,String content, Double amount, String date,int type) {
        this.id=id;
        this.content = content;
        this.amount = amount;
        this.date = date;
        this.tranTypeId=type;
    }

    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getType() {
        return tranTypeId;
    }

    public void setType(int type) {
        this.tranTypeId = type;
    }
}
