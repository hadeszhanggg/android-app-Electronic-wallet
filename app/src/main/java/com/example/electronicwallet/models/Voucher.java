package com.example.electronicwallet.models;

import java.util.Date;

public class Voucher {
    String id, voucherName, description,type;
    Double discount;
    Date exp, usedDated;
    Boolean used;

    public Voucher() {
    }

    public Voucher(String id, String voucherName, String description, Double discount, Date exp, Date usedDated, Boolean used,String type) {
        this.id = id;
        this.voucherName = voucherName;
        this.description = description;
        this.discount = discount;
        this.exp = exp;
        this.usedDated = usedDated;
        this.used = used;
        this.type=type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public Date getUsedDated() {
        return usedDated;
    }

    public void setUsedDated(Date usedDated) {
        this.usedDated = usedDated;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
