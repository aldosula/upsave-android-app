package com.appsnipp.modernlogin.Database;

public class User_Data {
    public String name = "user";
    public String email= "user";
    public Expense_Data expense_data;
    public  Income_Data income_data;
    public String phone = "0";
    public String budget = "0.00";



    public User_Data(){

    }

    public User_Data(String name, String email, String phone, String budget) {
        this.name = name;
        this.email = email;

        this.phone = phone;
        this.budget = budget;

    }

    public User_Data(String name, String email) {
        this.name = name;
        this.email = email;


    }



    public User_Data(String budget){
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

    public void setBudget(String budget) { this.budget = budget;}
}
