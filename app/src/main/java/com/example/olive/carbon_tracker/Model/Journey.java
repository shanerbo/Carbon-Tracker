// This class contains the Journey object

package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;
/**
 * Stores journey information
 */
public class Journey {
    private String dateOfTrip;
    private String routeName;
    private int totalDistance;
    private String vehicleName;
    private String mode;
    private double carbonEmitted;
    private int iconID = R.drawable.map;
    private long journeyID;

    public Journey(String dateOfTrip, String mode, String routeName, int totalDistance,
                   String vehicleName, double carbonEmitted, long journeyID) {
        this.dateOfTrip = dateOfTrip;
        this.routeName = routeName;
        this.mode = mode;
        this.totalDistance = totalDistance;
        this.vehicleName = vehicleName;
        this.carbonEmitted = carbonEmitted;
        this.journeyID = journeyID;
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

    public String getMode() {
        return mode;
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

    public long getJourneyID() {
        return journeyID;
    }

    public void setJourneyID(long journeyID) {
        this.journeyID = journeyID;
    }
}
