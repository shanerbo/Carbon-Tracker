package com.example.olive.carbon_tracker;


public class Singleton {
    private static Singleton currInstance;

    private Singleton() {
        currInstance = this;
    }

    public static Singleton getCurrInstance() {
        return currInstance;
    }
}
