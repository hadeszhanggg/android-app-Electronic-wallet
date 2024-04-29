package com.example.electronicwallet.models;

import java.util.Date;

public class Bill {
    String id, description;
    Double total;
    Boolean paid;
    Date expiryDay, paid_date;
    String type;
    public Bill() {
    }

    public Bill(String id, String description, Double total, Boolean paid, Date expiryDay, Date paid_date,String Type) {
        this.id = id;
        this.description = description;
        this.total = total;
        this.paid = paid;
        this.expiryDay = expiryDay;
        this.paid_date = paid_date;
        this.type=Type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Date getExpiryDay() {
        return expiryDay;
    }

    public void setExpiryDay(Date expiryDay) {
        this.expiryDay = expiryDay;
    }

    public Date getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(Date paid_date) {
        this.paid_date = paid_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
