package com.example.olive.carbon_tracker.Model;


import com.example.olive.carbon_tracker.R;

public class Journey {

    private String dateOfTrip;
    private String routeName;
    private int totalDistance;
    private String vehicleName;
    private double carbonEmitted;
    private int iconID = R.mipmap.ic_launcher;


    public Journey(String dateOfTrip, String routeName, int totalDistance, String vehicleName, double carbonEmitted) {
        this.dateOfTrip = dateOfTrip;
        this.routeName = routeName;
        this.totalDistance = totalDistance;
        this.vehicleName = vehicleName;
        this.carbonEmitted = carbonEmitted;
    }

    public String getDateOfTrip() {
        return dateOfTrip;
    }

    public void setDateOfTrip(String dateOfTrip) {
        this.dateOfTrip = dateOfTrip;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public double getCarbonEmitted() {
        return carbonEmitted;
    }

    public void setCarbonEmitted(double carbonEmitted) {
        this.carbonEmitted = carbonEmitted;
    }

    public int getIconID() {
        return iconID;
    }
}
