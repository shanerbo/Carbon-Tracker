package com.example.olive.carbon_tracker.Model;


import com.example.olive.carbon_tracker.R;

public class Route {
    private String Name;
    private int CityDistance;
    private int HighwayDistance;
    private int TotalDistance;
    private int iconId = R.drawable.routesign;

    public Route(String name, int cityDistance, int highwayDistance, int totalDistance) {
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

    public int getIconId(){
        return iconId;
    }

    public int getCityDistance() {
        return CityDistance;
    }

    private void setCityDistance(int cityDistance) {
        if (cityDistance == 0) {
            throw new IllegalArgumentException();
        }
        this.CityDistance = cityDistance;
    }

    public int getHighwayDistance() {
        return HighwayDistance;
    }

    private void setHighwayDistance(int highwayDistance) {
        if (highwayDistance == 0) {
            throw new IllegalArgumentException();
        }
        this.HighwayDistance = highwayDistance;
    }

    public int getTotalDistance(){
        return TotalDistance;
    }

    private void setTotalDistance(int totalDistance) {
        if (totalDistance == 0) {
            throw new IllegalArgumentException();
        }
        this.TotalDistance = totalDistance;
    }
}
