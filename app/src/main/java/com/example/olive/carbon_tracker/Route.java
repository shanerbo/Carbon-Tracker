package com.example.olive.carbon_tracker;


public class Route {
    private String Name;
    private int CityDistance;
    private int HighwayDistance;
    private int TotalDistance;
    private int iconId = R.drawable.routesign;
    public Route(String name, int cityDistance, int highwayDistance, int totalDistance) {
        Name = name;
        CityDistance = cityDistance;
        HighwayDistance = highwayDistance;
        TotalDistance = totalDistance;
    }

    public String getName() {
        return Name;
    }


    public int getIconId(){
        return iconId;
    }
    public int getCityDistance() {
        return CityDistance;
    }



    public int getHighwayDistance() {
        return HighwayDistance;
    }
    public int getTotalDistance(){
        return TotalDistance;
    }
    public void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.Name = name;
    }

    public void setCityDistance(int cityDistance) {
        if (cityDistance == 0) {
            throw new IllegalArgumentException();
        }
        this.CityDistance = cityDistance;
    }

    public void setHighwayDistance(int highwayDistance) {
        if (highwayDistance == 0) {
            throw new IllegalArgumentException();
        }
        this.HighwayDistance = highwayDistance;
    }

    public void setTotalDistance(int totalDistance) {
        if (totalDistance == 0) {
            throw new IllegalArgumentException();
        }
        this.TotalDistance = totalDistance;
    }
}
