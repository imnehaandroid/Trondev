package com.example.trondev;

public class SignUpData {
    private String name,email,boxId;


    public SignUpData(){

    }

    public SignUpData(String name,String email,String boxId) {
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
