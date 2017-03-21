package com.example.olive.carbon_tracker.Model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton currInstance = new Singleton();
    //    private String currenMake;
    private List<Vehicle> VehiclesList = new ArrayList<>();
    private List<Route> RouteList = new ArrayList<>();
    private List<MonthlyUtilitiesData> BillList = new ArrayList<>();
    private VehicleData vehicleData = new VehicleData();
    private List<String> getVehicleMakeArray = new ArrayList<>();
    private List<String> vehicleModelArray = new ArrayList<>();

    private String userDay = null;
    private String userMonth = null;
    private String userYear = null;
    private boolean isDateChanged = false;

    private String startDay = null;
    private String startMonth = null;
    private String startYear = null;
    private boolean isStartDateChanged = false;

    private String endDay = null;
    private String endMonth = null;
    private String endYear = null;
    private boolean isEndDateChanged = false;

    SQLiteDatabase CarInfoDB;



    ///////WHAT I NEED FOR MY CARBON CLASS /////////////
    private List<Journey> journeyList = new ArrayList<>();


    private static int editRoute;
    private static int editVehicle;
    private static int editMonthlyUtilities;
    private static long editPosition_Route;
    private static long editPosition_Car;

    public long getEditPostion_Journey() {
        return editPostion_Journey;
    }

    public void setEditPostion_Journey(long editPostion_Journey) {
        Singleton.editPostion_Journey = editPostion_Journey;
    }

    private static long editPostion_Journey;
    private static int editPosition_bill;
    private boolean editJourney = false;
    private int editJourneyPosition;
    private static int addRoute;
    private static int addVehicle;
    private static int addMonthlyUtilities;
    private static int deleteRoute;
    private static int TransportationMode;
    private static Singleton instance = new Singleton();
    private static Vehicle userPickVehicle;

    public static Singleton getInstance() {
        return instance;
    }


    private Singleton() {

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


    public void setEditPosition_car(long Position) {
        editPosition_Car = Position;
    }

    public long getEditPosition_car() {
        return editPosition_Car;
    }

    public int getAddPosition_car() {
        int position = VehiclesList.size() - 1;
        return position;
    }

    public void userEditRoute_car() { editVehicle = 1;  }

    public int checkEdit_car() {
        return editVehicle;
    }

    public void userFinishEdit_car() { editVehicle = 0;  }

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

    //--------------------------------Vehicle Date----------------------------------------------//

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

    //-------------------------------Monthly Utilities Dates------------------------------------//

    public List<MonthlyUtilitiesData> getBillList() {
        return BillList;
    }


    public String getStartDay() { return startDay;  }

    public void setStartDay(String startDay) { this.startDay = startDay; }

    public String getStartMonth() { return startMonth;  }

    public void setStartMonth(String startMonth) { this.startMonth = startMonth; }

    public String getStartYear() { return startYear; }

    public void setStartYear(String startYear) { this.startYear = startYear; }

    public String getEndDay() { return endDay; }

    public void setEndDay(String endDay) { this.endDay = endDay; }

    public String getEndMonth() { return endMonth; }

    public void setEndMonth(String endMonth) { this.endMonth = endMonth; }

    public String getEndYear() { return endYear; }

    public void setEndYear(String endYear) { this.endYear = endYear; }


    public boolean isStartDateChanged() { return isStartDateChanged; }

    public void setStartDateChanged(boolean startDateChanged) { isStartDateChanged = startDateChanged; }

    public boolean isEndDateChanged() { return isEndDateChanged; }

    public void setEndDateChanged(boolean endDateChanged) { isEndDateChanged = endDateChanged; }


    public void userAddMonthlyUtilities() {
        addMonthlyUtilities = 1;
    }

    public void userFinishAdd_MonthlyUtilities() {
        addMonthlyUtilities = 0;
    }

    public int checkAdd_MonthlyUtilities() {
        return addMonthlyUtilities;
    }

    public void userEditMonthlyUtilities() { editMonthlyUtilities = 1;  }

    public int checkEditMonthlyUtilities() {
        return editMonthlyUtilities;
    }

    public void userFinishEditMonthlyUtilities() { editMonthlyUtilities = 0;  }


    public void setEditPosition_bill(int Position) {
        editPosition_bill = Position;
    }

    public int getEditPosition_bill() {
        return editPosition_bill;
    }

    public void setBillList(List<MonthlyUtilitiesData> newBill) {
        BillList = newBill;
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

    public void setEditPosition_Route(long Position) {
        editPosition_Route = Position;
    }

    public int getAddPosition() {
        int position = RouteList.size() - 1;
        return position;
    }

    public long getEditPosition_Route() {
        return editPosition_Route;
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

    public void setCarInfoDb(SQLiteDatabase db) {
        CarInfoDB = db;
    }

    public SQLiteDatabase getCarInfoDb() {
        return CarInfoDB;
    }

    //-----------------------------------Journey functions-------------------------------------------
    public List<Journey> getUsersJourneys() {
        return journeyList;
    }

    public void setJourneyList(List<Journey> JourneyList) {
        journeyList = JourneyList;
    }

//    public void addUserJourney(Journey journey) {
//        journeyList.add(journey);
//    }

    public Journey getJourney(int position) {
        return journeyList.get(position);
    }

    public void setEditJourneyPosition(int position) {
        editJourneyPosition = position;
    }

    public boolean isEditingJourney() {
        return editJourney;
    }

    public void userEditJourney() {
        editJourney = true;
    }

    public void userFinishEditJourney() {
        editJourney = false;
    }

    public void changeJourney(Journey newJourney) {
        journeyList.remove(editJourneyPosition);
        journeyList.add(editJourneyPosition, newJourney);
    }

}
