package com.example.olive.carbon_tracker;

public class Car {
    private String Name;
    private String Make;
    private String Model;
    private int Year;

    public Car(String name, String make, String model, int year) {
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
        this.Name = name;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        if (make.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        if (model.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.Model = model;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        if (year == 0) {
            throw new IllegalArgumentException();
        }
        this.Year = year;
    }
}
