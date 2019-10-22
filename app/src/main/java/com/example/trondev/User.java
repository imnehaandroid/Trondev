package com.example.trondev;

public class User {
    private String name,email,phone,boxId;


    public User(){

    }

    public User(String name, String email,String phone, String boxId) {
        this.name = name;
        this.email = email;
        this.phone=phone;
        this.boxId=boxId;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBoxId() {
        return boxId;
    }
}
