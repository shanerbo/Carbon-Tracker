package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;

public class MonthlyUtilitiesData {
    private String Name;
    private int CityDistance;
    private int HighwayDistance;
    private int TotalDistance;

    public MonthlyUtilitiesData(String name, int cityDistance, int highwayDistance, int totalDistance) {
        setName(name);
        setCityDistance(cityDistance);
        setHighwayDistance(highwayDistance);
        setTotalDistance(totalDistance);


    }

    public String getName() {
        return Name;
    }

    private void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.Name = name;
    }


    public int getCityDistance() {
        return CityDistance;
    }

    private void setCityDistance(int cityDistance) {

        this.CityDistance = cityDistance;
    }

    public int getHighwayDistance() {
        return HighwayDistance;
    }

    private void setHighwayDistance(int highwayDistance) {

        this.HighwayDistance = highwayDistance;
    }

    public int getTotalDistance(){
        return TotalDistance;
    }

    private void setTotalDistance(int totalDistance) {

        this.TotalDistance = totalDistance;
    }
}
