package com.appsnipp.modernlogin;

public class User_Data {
    public String name;
    public String email;
    public String phone;



    public User_Data(){

    }

    public User_Data(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;

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


}
