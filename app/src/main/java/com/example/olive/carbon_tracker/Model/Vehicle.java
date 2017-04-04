// This class contains the Vehicle object

package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;

/**
 * makes a vehicle object and stores vehicle information
 */
public class Vehicle {
    private String name;
    private String make;
    private String model;
    private int year;
    private double highway08;
    private double city08;
    private String fuelType;
    private long vehicleDBId;
    private int imageID;
    public Vehicle() {
    }
    private int image_1 = R.mipmap.car1;
    private int image_2 = R.mipmap.car2;
    private int image_3 = R.mipmap.car3;
    private int image_4 = R.mipmap.car4;
    private int image_5 = R.mipmap.car5;
    private int image_6 = R.mipmap.car6;



    public Vehicle(String name, String make, String model, int year,
                   double city08, double highway08, String fuelType, long vehicleDBId,int imageId) {
        this.name = name;
        this.make = make;
        this.model = model;
        this.year = year;
        this.city08 = city08;
        this.highway08 = highway08;
        this.fuelType = fuelType;
        this.vehicleDBId = vehicleDBId;
        this.imageID = convertIDtoMipmapID(imageId);
    }
    public int convertIDtoMipmapID(int id){
        if (id == 1){
            return image_1;
        }else if (id == 2){
            return image_2;
        }else if (id == 3){
            return image_3;
        }else if (id == 4){
            return image_4;
        }else if (id == 5){
            return image_5;
        }else{
            return image_6;
        }

    }
    public String getName() {
        return name;
    }
    public int getImageID() {
        return imageID;
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


    public double getCity08() {
        return city08;
    }

    public double getHighway08() {
        return highway08;
    }

    public String getFuelType() {
        return fuelType;
    }

    public long getVehicleDBId() {
        return vehicleDBId;
    }
}
