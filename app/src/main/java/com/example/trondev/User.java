package com.example.trondev;

public class User {
    private String name,email,boxId;


    public User(){

    }

    public User(String name, String email, String boxId) {
        this.name = name;
        this.email = email;
        this.boxId=boxId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBoxId() {
        return boxId;
    }
}
