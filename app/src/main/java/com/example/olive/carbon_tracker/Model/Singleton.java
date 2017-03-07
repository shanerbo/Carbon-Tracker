package com.example.olive.carbon_tracker.Model;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton currInstance = new Singleton();
    private List<Vehicle> Vehicles = new ArrayList<>();
    private List<Route> RouteList = new ArrayList<>();
    private RouteCollection Routes = new RouteCollection();
    private static int editRoute ;
    private static int editPosition ;
    private static int addRoute ;
    private static int deleteRoute ;
    private static Singleton instance = new Singleton();

    public static Singleton getInstance(){
        return instance;
    }


    private Singleton(){

    }


    public List<String> getMake(Context context){
        VehicleData vehicleData = new VehicleData();
        vehicleData.ExtractVehicleData(context);
        List<String> make = vehicleData.getUniqueVehicleMakeArray();
        return make;
    }





    public void addVehicle(Vehicle vehicle) {
        this.Vehicles.add(vehicle);
    }

    public Vehicle getVehicle(int index) {
        validateIndex(this.Vehicles, index);
        return this.Vehicles.get(index);
    }



    private void validateIndex(List list, int index) {
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException();
        }
    }

//-----------------------------------Route's function-------------------------------------------
public void setUserRoutes(RouteCollection userRoutes){
    Routes = userRoutes;
}
    public RouteCollection getUserRoutes(){
        return Routes;
    }
    public List<Route> getRouteList(){
        return RouteList;
    }
    public void setRouteList(List<Route> newRoute){
        RouteList = newRoute;
    }

    public int checkEdit(){
        return editRoute;
    }
    public void setEditPosition(int Position){
        editPosition = Position;
    }
    public int getAddPosition(){
        int position = RouteList.size()-1;
        return position;
    }
    public int getEditPosition(){
        return editPosition;
    }
    public void userEditRoute(){
        editRoute = 1;
    }
    public int userFinishEdit(){
        editRoute = 0;
        return editRoute;
    }
    public void userAddRoute(){
        addRoute = 1;
    }
    public int userFinishAdd(){
        addRoute = 0;
        return addRoute;
    }
    public int checkAdd(){
        return addRoute;
    }
//-----------------------------------Route's function-------------------------------------------


}
