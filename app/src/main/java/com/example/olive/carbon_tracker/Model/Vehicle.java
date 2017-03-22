package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;


public class Vehicle {
    private String name;
    private String make;
    private String model;
    private int year;
    private int iconId = R.drawable.old_vintage_car;
    private double highway08;
    private double city08;
    private String fuelType;
    private long vehicleDBId;
    private int cityDistance = 0;
    private int hwyDistance = 0;

    public Vehicle(String name, String make, String model, int year,
                   double city08, double highway08, String fuelType, long vehicleDBId) {
        this.name = name;
        this.make = make;
        this.model = model;
        this.year = year;
        this.city08 = city08;
        this.highway08 = highway08;
        this.fuelType = fuelType;
        this.vehicleDBId = vehicleDBId;

    }

    public long getVehicleDBId() {
        return vehicleDBId;
    }

    public double getHighway08() {
        return highway08;
    }

    public double getCity08() {
        return city08;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setCityDistance(int cityDistance) {
        this.cityDistance = cityDistance;
    }

    public void setHwyDistance(int hwyDistance) {
        this.hwyDistance = hwyDistance;
    }

    public int getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getMake() {
        return make;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (model.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.model = model;
    }

    public int getYear() {
        return year;
    }


}
