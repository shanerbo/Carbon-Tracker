// This class contains the Route object

package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;

/**
 * Saves route information
 */
public class Route {
    private String name;
    private int cityDistance;
    private int highwayDistance;
    private int totalDistance;
    private int iconId = R.drawable.routesign;
    private long routeDBId;

    public Route(String name, int cityDistance, int highwayDistance, int totalDistance, long routeDBId) {
        setName(name);
        setCityDistance(cityDistance);
        setHighwayDistance(highwayDistance);
        setTotalDistance(totalDistance);
        setRouteDBId(routeDBId);

    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public int getCityDistance() {
        return cityDistance;
    }

    private void setCityDistance(int cityDistance) {
        this.cityDistance = cityDistance;
    }

    public int getHighwayDistance() {
        return highwayDistance;
    }

    private void setHighwayDistance(int highwayDistance) {
        this.highwayDistance = highwayDistance;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    private void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getIconId() {
        return iconId;
    }

    public long getRouteDBId() {
        return routeDBId;
    }

    public void setRouteDBId(long routeDBId) {
        this.routeDBId = routeDBId;
    }
}
