package com.example.olive.carbon_tracker.Model;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton currInstance = new Singleton();
    //    private String currenMake;
    private List<Vehicle> VehiclesList = new ArrayList<>();
    private List<Route> RouteList = new ArrayList<>();
    private VehicleData vehicleData = new VehicleData();
    private List<String> getVehicleMakeArray = new ArrayList<>();
    private List<String> vehicleModelArray = new ArrayList<>();

String userDay = null;
    String userMonth = null;
    String userYear = null;
    boolean isDateChanged = false;



    ///////WHAT I NEED FOR MY CARBON CLASS /////////////
    private List<Journey> journeyList = new ArrayList<>();


    private static int editRoute;
    private static int editVehicle;
    private static int editPosition;
    private static int editPosition_car;
    private static int addRoute;
    private static int addVehicle;
    private static int deleteRoute;
    private static int TransportationMode;
    private static Singleton instance = new Singleton();
    private static Vehicle userPickVehicle;

    public static Singleton getInstance() {
        return instance;
    }


    private Singleton() {

    }

    public List<Journey> getUsersJourneys() {
        return journeyList;
    }

    public void addUserJourney(Journey journey) {
        journeyList.add(journey);
    }


public boolean getIsDateChanged(){

    return isDateChanged;

}

    public void setIsDateChanged(boolean change){

        isDateChanged = change;

    }

    public void setVehicleData(Context context) {
        vehicleData.ExtractVehicleData(context);
    }

    public VehicleData getVehicleData() {
        return vehicleData;
    }

    public void setVehicleMakeArray() {
        getVehicleMakeArray = vehicleData.getUniqueVehicleMakeArray();
    }

    public List<String> getVehicleMakeArray() {
        return getVehicleMakeArray;
    }

    public List<String> getVehicleModelArray()

    {
        return vehicleModelArray;
    }
//    public List<Integer> getVehicleYearArray(){
//        return vehicleData.uniqueVehicleYearArray();
//    }


    public void setEditPosition_car(int Position) {
        editPosition_car = Position;
    }

    public int getEditPosition_car() {
        return editPosition_car;
    }

    public int getAddPosition_car() {
        int position = VehiclesList.size() - 1;
        return position;
    }

    public void userEditRoute_car() {
        editVehicle = 1;
    }

    public int checkEdit_car() {
        return editVehicle;
    }

    public void userFinishEdit_car() {
        editVehicle = 0;
    }

    public void userAddVehicle() {
        addVehicle = 1;
    }

    public void userFinishAdd_car() {
        addVehicle = 0;
    }

    public int checkAdd_car() {
        return addVehicle;
    }

    public void setVehiclesList(List<Vehicle> newVehicle) {
        VehiclesList = newVehicle;
    }

    public List<Vehicle> getVehicleList() {
        return VehiclesList;
    }


    public List<String> updateModels(String vehicleMake) {
        List<String> vehicleModelArray = vehicleData.getModelsForAMake(vehicleMake);
        return vehicleModelArray;
    }

    public List<String> updateDispl(String model, int year) {
        List<String> vehicleDispl = vehicleData.getDisplForVehicle(model, year);

        return vehicleDispl;
    }

    public List<Integer> updateYears(String vehicleModel) {
        List<Integer> vehicleYearArray = vehicleData.getYearsForAModel(vehicleModel);

        return vehicleYearArray;

    }

    public int getCityData(int position) {

        return vehicleData.getGiveCity(position);

    }

    public int getHwayData(int position) {
        return vehicleData.getGiveHway(position);

    }

    public String getFuelType(int position) {
        return vehicleData.getGiveFuel(position);
    }

    public void setUserPickVehicleItem(Vehicle vehicle) {
        userPickVehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return userPickVehicle;
    }

    public List<String> getMake(Context context) {

        vehicleData.ExtractVehicleData(context);
        List<String> make = vehicleData.getUniqueVehicleMakeArray();
        return make;
    }





    private void validateIndex(List list, int index) {
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException();
        }
    }

    public String getUserDay() {
        return userDay;
    }

    public void setUserDay(String userDay) {
        this.userDay = userDay;
    }

    public String getUserMonth() {
        return userMonth;
    }

    public void setUserMonth(String userMonth) {
        this.userMonth = userMonth;
    }

    public String getUserYear() {
        return userYear;
    }

    public void setUserYear(String userYear) {
        this.userYear = userYear;
    }

    //-----------------------------------Route's function-------------------------------------------

    public List<Route> getRouteList() {
        return RouteList;
    }

    public void setRouteList(List<Route> newRoute) {
        RouteList = newRoute;
    }

    public int checkEdit() {
        return editRoute;
    }

    public void setEditPosition(int Position) {
        editPosition = Position;
    }

    public int getAddPosition() {
        int position = RouteList.size() - 1;
        return position;
    }

    public int getEditPosition() {
        return editPosition;
    }

    public void userEditRoute() {
        editRoute = 1;
    }

    public void userFinishEdit() {
        editRoute = 0;
    }

    public void userAddRoute() {
        addRoute = 1;
    }

    public void userFinishAdd() {
        addRoute = 0;
    }

    public int checkAdd() {
        return addRoute;
    }

    public void UserEnterNewCarName(String newCarName,String oldName) {
        for (int i=0; i<journeyList.size();i++){
            if(journeyList.get(i).getVehicleName() ==  oldName){
                journeyList.get(i).setVehicleName(newCarName);
            }
        }

    }

    public void UserEnterNewRouteName(String newRouteName, String oldName) {
        for (int i=0; i<journeyList.size();i++){
            if(journeyList.get(i).getRouteName() ==  oldName){
                journeyList.get(i).setRouteName(newRouteName);
            }
        }
    }

//-----------------------------------Route's function-------------------------------------------

    public int checkTransportationMode() {
        return TransportationMode;
    }

    public void ModeCar() {
        TransportationMode = 0;
    }

    public void ModeWalkBike() {
        TransportationMode = 1;
    }

    public void ModeBus() {
        TransportationMode = 2;
    }

    public void ModeSkytrain() {
        TransportationMode = 3;
    }



}
