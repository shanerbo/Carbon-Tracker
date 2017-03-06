package com.example.olive.carbon_tracker;


import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton currInstance = new Singleton();
    private List<Vehicle> Vehicles = new ArrayList<>();
    private List<Route> Routes = new ArrayList<>();

    private Singleton() {
        currInstance = this;
    }

    public static Singleton getCurrInstance() {
        return currInstance;
    }

    public void addVehicle(Vehicle vehicle) {
        this.Vehicles.add(vehicle);
    }

    public Vehicle getVehicle(int index) {
        validateIndex(this.Vehicles, index);
        return this.Vehicles.get(index);
    }

    public void addRoute(Route route) {
        this.Routes.add(route);
    }

    public Route getRoute(int index) {
        validateIndex(this.Routes, index);
        return this.Routes.get(index);
    }

    private void validateIndex(List list, int index) {
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException();
        }
    }
}
