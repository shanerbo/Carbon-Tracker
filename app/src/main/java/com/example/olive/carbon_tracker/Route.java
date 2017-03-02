package com.example.olive.carbon_tracker;

import java.security.InvalidParameterException;

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
            throw new InvalidParameterException();
        }
        Name = name;
    }

    public int getCityDistance() {
        return CityDistance;
    }

    public void setCityDistance(int cityDistance) {
        if (cityDistance == 0) {
            throw new InvalidParameterException();
        }
        CityDistance = cityDistance;
    }

    public int getHighwayDistance() {
        return HighwayDistance;
    }

    public void setHighwayDistance(int highwayDistance) {
        if (highwayDistance == 0) {
            throw new InvalidParameterException();
        }
        HighwayDistance = highwayDistance;
    }
}
