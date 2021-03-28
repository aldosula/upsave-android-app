package com.appsnipp.modernlogin.Database;


import java.text.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class Expense_Data {

    private String category= "N/A";
    private String expense= "0.00";
    private String expense_nr;
    private Calendar calendar ;
    private String expense_date=" ";


    public String getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }

    public Expense_Data(){
    }

    public String getCategory() {
        return category;
    }

    public String getExpense() {
        return expense;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getExpense_nr() {
        return expense_nr;
    }

    public void setExpense_nr(String expense_nr) {
        this.expense_nr = expense_nr;
    }
}
