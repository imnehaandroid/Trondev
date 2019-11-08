package com.example.trondev;

public class User {
    private String name,email,phone,boxId, boxStatus;


    public User(){

    }

    public User(String name, String email,String phone, String boxId, String boxStatus) {
        this.name = name;
        this.email = email;
        this.phone=phone;
        this.boxId=boxId;
        this.boxStatus = boxStatus;
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

    public String getBoxStatus() {
        return boxStatus;
    }
}
