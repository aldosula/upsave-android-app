package com.appsnipp.modernlogin.Database;

public class User_Data {
    public String name;
    public String email;
    public String phone;
    public float budget = 0.00f;



    public User_Data(){

    }

    public User_Data(String name, String email, String phone, float budget) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.budget = budget;

    }

    public User_Data(String name, String email) {
        this.name = name;
        this.email = email;


    }

    public User_Data(float budget){
        this.budget = budget;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBudget(float budget) { this.budget = budget;}
}
