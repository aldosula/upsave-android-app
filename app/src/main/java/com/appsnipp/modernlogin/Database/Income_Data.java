package com.appsnipp.modernlogin.Database;

public class Income_Data {
    private String income = "0.00";
    private String income_cat= "N/A";
    private  String income_nr;
    private String income_date= " ";

    public void Income_Data(){


    }

    public String getIncome_date() {
        return income_date;
    }

    public void setIncome_date(String income_date) {
        this.income_date = income_date;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getIncome_cat() {
        return income_cat;
    }

    public void setIncome_cat(String income_cat) {
        this.income_cat = income_cat;
    }

    public String getIncome_nr() {
        return income_nr;
    }

    public void setIncome_nr(String income_nr) {
        this.income_nr = income_nr;
    }
}
