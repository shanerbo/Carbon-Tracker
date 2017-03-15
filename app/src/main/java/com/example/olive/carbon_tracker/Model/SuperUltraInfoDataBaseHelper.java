package com.example.olive.carbon_tracker.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



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




    public static String Route_Table = "RouteInfoTable";
    public static String Route_Id = "_id";
    public static String Route_Name = "RouteName";
    public static String Route_City_Dst = "RouteCityDistance";
    public static String Route_HWY_Dst = "RouteHwyDistance";
    public static String Route_total_Dst = "RouteTotalDistance";


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
                + Car_FuelType + " text"
                + ");";

        CarDB.execSQL(createCarDB);

        String createRouteDB = "create table if not exists" + Route_Table
                + "("
                + Route_Id + " integer primary key autoincrement not null,"
                + Route_Name + " text not null,"
                + Route_City_Dst + " integer,"
                + Route_HWY_Dst + " integer,"
                + Route_total_Dst + " integer"
                + ");";

        CarDB.execSQL(createRouteDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
