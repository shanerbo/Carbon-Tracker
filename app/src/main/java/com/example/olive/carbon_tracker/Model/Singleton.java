package com.example.olive.carbon_tracker.Model;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton currInstance = new Singleton();
//    private String currenMake;
    private List<Vehicle> VehiclesList = new ArrayList<>();
    private List<Route> RouteList = new ArrayList<>();
    private VehicleData vehicleData = new VehicleData();
    private List<String> vehicleModelArray = new ArrayList<>();
//    List<Vehicle> vehicleDataArray = new ArrayList<>();


    //private RouteCollection Routes = new RouteCollection();
    private static int editRoute ;
    private static int editVehicle ;
    private static int editPosition ;
    private static int editPosition_car ;
    private static int addRoute ;
    private static int addVehicle ;
    private static int deleteRoute ;
    private static Singleton instance = new Singleton();

    public static Singleton getInstance(){
        return instance;
    }


    private Singleton(){

    }


//    public void setVehicleArray(Context context){
//        vehicleData.ExtractVehicleData(context);
//    }
    public void setVehicleData(Context context){
        vehicleData.ExtractVehicleData(context);
    }
    public VehicleData getVehicleData(){
        return vehicleData;
    }

    public List<String> getVehicleMakeArray(){
        return vehicleData.getUniqueVehicleMakeArray();
    }
    public List<String> getVehicleModelArray()

    {
        return vehicleModelArray;
    }
    public List<Integer> getVehicleYearArray(){
        return vehicleData.uniqueVehicleYearArray();
    }


    public void setEditPosition_car(int Position){
        editPosition_car = Position;
    }
    public int getEditPosition_car(){
        return editPosition_car;
    }
    public int getAddPosition_car(){
        int position = VehiclesList.size()-1;
        return position;
    }
    public void userEditRoute_car(){
        editVehicle = 1;
    }
    public int checkEdit_car(){
        return editVehicle;
    }

    public void userFinishEdit_car(){
        editVehicle = 0;
    }
    public void userAddVehicle(){
        addVehicle = 1;
    }
    public void userFinishAdd_car(){
        addVehicle = 0;
    }
    public int checkAdd_car(){
        return addVehicle;
    }

    public void setVehiclesList(List<Vehicle> newVehicle){
        VehiclesList = newVehicle;
    }

    public List<Vehicle> getVehicleList(){
        return VehiclesList;
    }




public  List<String> updateModels(String vehicleMake){
    List<String> vehicleModelArray = vehicleData.getModelsForAMake(vehicleMake);

    return vehicleModelArray;

}

public List<Double> updateDispl(String model,int year){
    List<Double> vehicleDispl = vehicleData.getDisplForVehicle(model,year);

    return vehicleDispl;
}


    public  List<Integer> updateYears(String vehicleModel){
        List<Integer> vehicleYearArray = vehicleData.getYearsForAModel(vehicleModel);

        return vehicleYearArray;

    }
    public List<String> getMake(Context context){

        vehicleData.ExtractVehicleData(context);
        List<String> make = vehicleData.getUniqueVehicleMakeArray();
        return make;
    }


    public void addVehicle(Vehicle vehicle) {
        this.VehiclesList.add(vehicle);
    }

    public Vehicle getVehicle(int index) {
        validateIndex(this.VehiclesList, index);
        return this.VehiclesList.get(index);
    }



    private void validateIndex(List list, int index) {
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException();
        }
    }

//-----------------------------------Route's function-------------------------------------------
//public void setUserRoutes(RouteCollection userRoutes){
//    Routes = userRoutes;
//}
//    public RouteCollection getUserRoutes(){
//        return Routes;
//    }
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
    public void userFinishEdit(){
        editRoute = 0;
    }
    public void userAddRoute(){
        addRoute = 1;
    }
    public void userFinishAdd(){
        addRoute = 0;
    }
    public int checkAdd(){
        return addRoute;
    }
//-----------------------------------Route's function-------------------------------------------


}
