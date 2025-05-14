package com.example.test1.databinder;

public class holderemail {
    private static holderemail instance;
    private String data;

    private holderemail() {}

    public static holderemail getInstance() {
        if (instance == null) {
            instance = new holderemail();
        }
        return instance;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
