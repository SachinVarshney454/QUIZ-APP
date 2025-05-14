package com.example.test1.databinder;

public class holdername {
    private static holdername instance;
    private String data;

    private holdername() {}

    public static holdername getInstance() {
        if (instance == null) {
            instance = new holdername();
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
