package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;


public class Vehicle {
    private String Name;
    private String Make;
    private String Model;
    private int Year;
    private int highway08;
    private int fuelType;
    private float displ;
    private String trany;

    private int iconId = R.drawable.routesign;

    public Vehicle(String name, String make,String model, int year){
        setName(name);
        setMake(make);
        setModel(model);
        setYear(year);
    }
    public int getIconId(){
        return iconId;
    }

    public String getName(){
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

    private void setMake(String make) {
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

    private void setYear(int year) {
        if (year == 0) {
            throw new IllegalArgumentException();
        }
        this.Year = year;
    }
}
