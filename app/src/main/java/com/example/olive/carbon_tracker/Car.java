package com.example.olive.carbon_tracker;

public class Car {
    private String Name;
    private String Make;
    private int Model;
    private int Year;

    public Car(String name, String make, int model, int year) {
        setName(name);
        setMake(make);
        setModel(model);
        setYear(year);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }
        Name = name;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        if (make.length() == 0) {
            throw new IllegalArgumentException();
        }
        Make = make;
    }

    public int getModel() {
        return Model;
    }

    public void setModel(int model) {
        if (model == 0) {
            throw new IllegalArgumentException();
        }
        Model = model;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        if (year == 0) {
            throw new IllegalArgumentException();
        }
        Year = year;
    }
}
