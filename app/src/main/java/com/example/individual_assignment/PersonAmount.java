package com.example.individual_assignment;


import java.io.Serializable;

public class PersonAmount implements Serializable {
    private String name;
    private double amount;

    public PersonAmount(){

    };
    public PersonAmount(String name, double amount){
        this.name = name;
        this.amount = amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    public double getAmount(){
        return this.amount;
    }
}
