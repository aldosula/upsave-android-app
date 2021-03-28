package com.appsnipp.modernlogin.Database;

public class Budget_Data {

    private double initial_budget;
    private double actual_budget;
    private String end_date;
    private double needs;
    private double wants;
    private double savings;

    public Budget_Data(){
        super();

    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public double getNeeds() {
        return needs;
    }

    public void setNeeds(double needs) {
        this.needs = needs;
    }

    public double getWants() {
        return wants;
    }

    public void setWants(double wants) {
        this.wants = wants;
    }

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }

    public double getInitial_budget() {
        return initial_budget;
    }

    public void setInitial_budget(double initial_budget) {
        this.initial_budget = initial_budget;
    }

    public double getActual_budget() {
        return actual_budget;
    }

    public void setActual_budget(double actual_budget) {
        this.actual_budget = actual_budget;
    }
}
