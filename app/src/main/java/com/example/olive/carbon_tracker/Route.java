package com.example.olive.carbon_tracker;


public class Route {
    private String Name;
    private int CityDistance;
    private int HighwayDistance;

    public Route(String name, int cityDistance, int highwayDistance) {
        setName(name);
        setCityDistance(cityDistance);
        setHighwayDistance(highwayDistance);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }
        Name = name;
    }

    public int getCityDistance() {
        return CityDistance;
    }

    public void setCityDistance(int cityDistance) {
        if (cityDistance == 0) {
            throw new IllegalArgumentException();
        }
        CityDistance = cityDistance;
    }

    public int getHighwayDistance() {
        return HighwayDistance;
    }

    public void setHighwayDistance(int highwayDistance) {
        if (highwayDistance == 0) {
            throw new IllegalArgumentException();
        }
        HighwayDistance = highwayDistance;
    }
}
