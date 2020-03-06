package com.appsnipp.modernlogin.Database;

public class UserTransactionManager {

    public float budget = 0.00f;


    public UserTransactionManager(){


    }
    public UserTransactionManager(float budget){

        this.budget = budget;


    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }
}
