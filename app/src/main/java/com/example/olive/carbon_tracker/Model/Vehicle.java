package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;

/**
 * Created by rdhol on 2017-03-05.
 */

public class Vehicle {
    private String Name;
    private String Make;
    private String Model;
    private int Year;
    private int iconId = R.drawable.routesign;

    public Vehicle(String name, String make,String model, int year){
        Name = name;
        Make = make;
        Model = model;
        Year = year;
    }
    public int getIconId(){
        return iconId;
    }

    public String getName(){
        return Name;
    }
    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        this.Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        this.Model = model;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        this.Year = year;
    }
}
