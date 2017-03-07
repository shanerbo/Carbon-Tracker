package com.example.olive.carbon_tracker;

import java.util.ArrayList;
import java.util.List;


public class RouteCollection {
    private List<Route> routes = new ArrayList<>();


    public void addRoute(Route route) {
        routes.add(route);
    }
    public void delRoute(int del_index){
        routes.remove(del_index);
    }

    public void changeRoute(Route pot, int indexOfPotEditing) {
        validateIndexWithException(indexOfPotEditing);
        routes.remove(indexOfPotEditing);
        routes.add(indexOfPotEditing, pot);
    }

    public int countRoutes() {
        return routes.size();
    }

    public Route getRoute(int index) {
        validateIndexWithException(index);
        return routes.get(index);
    }
    // Useful for integrating with an ArrayAdapter
    public String[] getRouteDescriptions() {
        String[] descriptions = new String[countRoutes()];
        for (int i = 0; i < countRoutes(); i++) {
            Route route = getRoute(i);
            descriptions[i] = route.getName()  + route.getCityDistance() + "KM" + route.getHighwayDistance()+"KM" + route.getTotalDistance();
        }
        return descriptions;
    }

    public List<Route> allRouteList(){
        return routes;
    }
    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countRoutes()) {
            throw new IllegalArgumentException();
        }
    }
}
