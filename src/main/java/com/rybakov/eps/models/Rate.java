package com.rybakov.eps.models;

public class Rate{
    private String accountType;
    private float price;

    public Rate(String accountType, float price) {
        this.accountType = accountType;
        this.price = price;
    }
    public Rate(){}

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
