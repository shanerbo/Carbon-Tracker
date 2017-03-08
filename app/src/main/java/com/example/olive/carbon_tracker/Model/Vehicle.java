package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;


public class Vehicle {
    private String Name;
    private String Make;
    private String Model;
    private int Year;
    private int iconId = R.drawable.old_vintage_car;
    private int Highway08;
    private int City08;

    private String FuelType;
    private float displ;
    private String trany;
    private int cityDistance = 0;
    private int hwyDistance = 0;

    public Vehicle(String name, String make,String model, int year,int city08, int highway08, String fuelType ){
        this.Name = name;
        this.Make = make;
        this.Model = model;
        this.Year = year;
        this.City08 = city08;
        this.Highway08 = highway08;
        this.FuelType = fuelType;

    }

    public int getHighway08() {
        return Highway08;
    }

    public int getCity08() {
        return City08;
    }

    public String getFuelType() {
        return FuelType;
    }

    public int getCityDistance() {
        return cityDistance;
    }

    public void setCityDistance(int cityDistance) {
        this.cityDistance = cityDistance;
    }

    public int getHwyDistance() {
        return hwyDistance;
    }

    public void setHwyDistance(int hwyDistance) {
        this.hwyDistance = hwyDistance;
    }

    public Vehicle(){}

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
