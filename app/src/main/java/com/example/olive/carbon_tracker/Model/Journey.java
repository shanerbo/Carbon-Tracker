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

    private int imageID;
    private long journeyID;
    private int image_1 = R.mipmap.car1;
    private int image_2 = R.mipmap.car2;
    private int image_3 = R.mipmap.car3;
    private int image_4 = R.mipmap.car4;
    private int image_5 = R.mipmap.car5;
    private int image_6 = R.mipmap.car6;
    private int image_50 = R.mipmap.bus;
    private int image_60 = R.mipmap.skytrain;
    private int image_70 = R.mipmap.walk;
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
        }else if (id == 6){
            return image_6;
        }else if (id == 50){
            return image_50;
        }else if (id == 60){
            return image_60;
        }else{
            return image_70;
        }
    }
    public Journey(String dateOfTrip, String mode, String routeName, int totalDistance,
                   String vehicleName, double carbonEmitted, long journeyID,int imageid) {
        this.dateOfTrip = dateOfTrip;
        this.routeName = routeName;
        this.mode = mode;
        this.totalDistance = totalDistance;
        this.vehicleName = vehicleName;
        this.carbonEmitted = carbonEmitted;
        this.journeyID = journeyID;
        this.imageID = convertIDtoMipmapID(imageid);
    }

    public int getImageID() {
        return imageID;
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


    public long getJourneyID() {
        return journeyID;
    }

    public void setJourneyID(long journeyID) {
        this.journeyID = journeyID;
    }
}
