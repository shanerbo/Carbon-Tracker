package com.example.olive.carbon_tracker.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
Connect android studio to sql lite
get readable and writable database from internal memory
 */
public class SuperUltraInfoDataBaseHelper extends SQLiteOpenHelper {
    public static final String DB_Name = "CarInfo.sqlite";
    public static final int DB_Version = 1;
    public static String Car_Table = "CarInfoTable";
    public static String Car_Id = "_id";
    public static String Car_Name = "CarName";
    public static String Car_Make = "CarMake";
    public static String Car_Model = "CarModel";
    public static String Car_Year = "CarYear";
    public static String Car_City_08 = "CarCity08";
    public static String Car_Hwy_08 = "CarHwy08";
    public static String Car_FuelType = "CarFuelType";
    public static String Car_displ = "CarDispl";
    public static String Car_Trany = "CarTrany";
    public static String Car_Drive = "CarDrive";
    public static String Car_Image = "CarImage";




    public static String Route_Table = "RouteInfoTable";
    public static String Route_Id = "_id";
    public static String Route_Name = "RouteName";
    public static String Route_City_Dst = "RouteCityDistance";
    public static String Route_HWY_Dst = "RouteHwyDistance";
    public static String Route_total_Dst = "RouteTotalDistance";


    public static String Journey_Table = "JourneyInfoTable";
    public static String Journey_Id = "_id";
    public static String Journey_Date = "JourneyDate";
    public static String Journey_CarId = "JourneyCarId";
    public static String Journey_CarMode = "JourneyMode";
    public static String Journey_CarName = "JourneyCarName";
    public static String Journey_CarMake = "JourneyCarMake";
    public static String Journey_CarModel = "JourneyCarModel";
    public static String Journey_CarYear = "JourneyCarYear";
    public static String Journey_CarCity = "JourneyCarCity";
    public static String Journey_CarHwy = "JourneyCarHwy";
    public static String Journey_CarFuelType = "JourneyCarFuelType";
    public static String Journey_CarDispl = "JourneyCarDispl";
    public static String Journey_CarTrany = "JourneyCarTrany";
    public static String Journey_CarDrive = "JourneyCarDrive";
    public static String Journey_RouteId = "JourneyRouteId";
    public static String Journey_RouteName = "JourneyRouteName";
    public static String Journey_RouteCityDist = "JourneyRouteCity";
    public static String Journey_RouteHwyDist = "JourneyRouteHwy";
    public static String Journey_RouteTotalDist = "JourneyRouteTotal";
    public static String Journey_CO2Emitted = "JourneyCO2Emitted";
    public static String Journey_Image = "JourneyImage";

    public static String Utility_Table = "UtilityInfoTable";
    public static String Utility_Id = "_id";
    public static String Utility_StartDate = "UtilityStartDate";
    public static String Utility_EndDate = "UtilityEndDate";
    public static String Utility_TotalDay = "UtilityTotalDay";
    public static String Utility_Electricy = "UtilityElectricity";
    public static String Utility_Gas = "UtilityEGas";
    public static String Utility_NumberOfSharing = "UtilityNumberOfSharing";
    public static String Utility_AverageCO2 = "UtilityAverageCO2";
    public static String Utility_CreationDate = "UtilityCreationDate";


    public SuperUltraInfoDataBaseHelper(Context context) {
        super(context, DB_Name,
                null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase CarDB) {
        String createCarDB = "create table if not exists " + Car_Table
                + "("
                + Car_Id + " integer primary key autoincrement not null,"
                + Car_Name + " text not null,"
                + Car_Make + " text,"
                + Car_Model + " text,"
                + Car_Year + " integer,"
                + Car_City_08 + " real,"
                + Car_Hwy_08 + " real,"
                + Car_FuelType + " text,"
                + Car_displ + " real,"
                + Car_Trany + " text,"
                + Car_Drive + " text"
                + Car_Image + " integer"
                + ");";

        CarDB.execSQL(createCarDB);

        String createRouteDB = "create table if not exists " + Route_Table
                + "("
                + Route_Id + " integer primary key autoincrement not null,"
                + Route_Name + " text not null,"
                + Route_City_Dst + " integer,"
                + Route_HWY_Dst + " integer,"
                + Route_total_Dst + " integer"
                + ");";

        CarDB.execSQL(createRouteDB);

        String createJourneyDB = "create table if not exists " + Journey_Table
                + "("
                + Journey_Id + " integer primary key autoincrement not null,"
                + Journey_Date + " integer,"
                + Journey_CarId + " integer ,"
                + Journey_CarMode + " text ,"
                + Journey_CarName +" text ,"
                + Journey_CarMake + " text,"
                + Journey_CarModel + " text,"
                + Journey_CarYear + " integer,"
                + Journey_CarCity + " real,"
                + Journey_CarHwy + " real,"
                + Journey_CarFuelType + " text,"
                //+ Journey_CarDispl + " real,"
                //+ Journey_CarTrany + " text,"
                //+ Journey_CarDrive + " text,"

                + Journey_RouteId + " integer,"
                + Journey_RouteName + " text,"
                + Journey_RouteCityDist + " integer,"
                + Journey_RouteHwyDist  + " integer,"
                + Journey_RouteTotalDist+ " integer,"
                + Journey_CO2Emitted+ " real"
                + Journey_Image+ " integer"
                + ");";
        CarDB.execSQL(createJourneyDB);



        String createUtility = "create table if not exists " + Utility_Table
                +"("
                + Utility_Id + " integer primary key autoincrement not null, "
                + Utility_StartDate + " integer,"
                + Utility_EndDate + " integer,"
                + Utility_Electricy + " real,"
                + Utility_Gas + " real,"
                + Utility_TotalDay + " integer,"
                + Utility_NumberOfSharing + " integer, "
                + Utility_AverageCO2 + " real,"
                + Utility_CreationDate + " real"
                + ");";
        CarDB.execSQL(createUtility);
    }


//    public void editCar(long DBID, String CarName, String CarMake,
//                        String CarModel, int CarYearFromString, double city,double highWay,
//                        String fuelType, String displ,
//                        String trany,String drive){
//        ContentValues cv = new ContentValues();
//        cv.put(SuperUltraInfoDataBaseHelper.Car_Name,CarName);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_Make,CarMake);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_Model,CarModel);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_Year,CarYearFromString);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_City_08,city);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_Hwy_08,highWay);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_FuelType,fuelType);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_displ,Double.parseDouble(displ));
//        cv.put(SuperUltraInfoDataBaseHelper.Car_Trany,trany);
//        cv.put(SuperUltraInfoDataBaseHelper.Car_Drive,drive);
//
//        long idPassBack = CarDB.update(SuperUltraInfoDataBaseHelper.Car_Table, cv, "_id="+DBID, null);
//
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
