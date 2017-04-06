package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.SuperUltraInfoDataBaseHelper;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds / edits a route
 */

import static java.lang.Math.round;

public class AddNewRoute extends AppCompatActivity {
    private List<Route> RouteList = new ArrayList<>();
    private String _currentRouteName;
    private long position;
    private Singleton singleton  = Singleton.getInstance();
    private Vehicle _vehicle = singleton.getVehicle();
    private String oldRouteName;
    private Route _RouteToBeEdit;
    private SQLiteDatabase RouteDB;
    private String _day =   checkDayIsSingleDIgit(singleton.getUserDay());
    private String _month =  ChangeMonthInInt(singleton.getUserMonth());
    private String _year =  singleton.getUserYear();
    private String _date = _year+"-"+_month+"-"+_day;
    private long _EditedJourneyID = singleton.getEditPostion_Journey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SuperUltraInfoDataBaseHelper RouteDBhelper = new SuperUltraInfoDataBaseHelper(this);
        RouteDB = RouteDBhelper.getWritableDatabase();

        //getSupportActionBar().hide();
        setContentView(R.layout.activity_add_new_route);
        RouteList = singleton.getRouteList();
        if(singleton.checkEdit() == 1){
            position = singleton.getEditPosition_Route();
            //Route RouteToBeEdited = RouteList.get(position);
            String RouteName = new String();
            int CityDistance = 0;
            int HwyDistance = 0;
            int TotalDistance = 0;
            long _id = 0;
            Cursor cursor = RouteDB.rawQuery("select * from RouteInfoTable" +
                    " where _id = " + position,
                    null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                RouteName = cursor.getString(1);
                CityDistance = cursor.getInt(2);
                HwyDistance = cursor.getInt(3);
                TotalDistance = cursor.getInt(4);
                _id = cursor.getLong(0);
                cursor.moveToNext();
            }
            cursor.close();
            Route RouteToBeEdit = new Route(RouteName,CityDistance,HwyDistance,TotalDistance,_id);
            _RouteToBeEdit = RouteToBeEdit;

            oldRouteName = _RouteToBeEdit.getName();
            int Route_city_dis = _RouteToBeEdit.getCityDistance();
            int Route_hWay_dis = _RouteToBeEdit.getHighwayDistance();
            EditText Name = (EditText)findViewById(R.id.RouteNameInput);
            EditText cityDst = (EditText)findViewById(R.id.CityDstInput);
            EditText hWayDst = (EditText)findViewById(R.id.HwayDstInput);
            Name.setText(oldRouteName);
            cityDst.setText(""+Route_city_dis+"");
            hWayDst.setText(""+Route_hWay_dis+"");
        } else {
            position = singleton.getAddPosition();
        }
        checkButton(position);
        delButton(position);

        setToolBar();
    }

    private void checkButton(final long position) {
        FloatingActionButton check = (FloatingActionButton) findViewById(R.id.comfirm_add);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Name = (EditText) findViewById(R.id.RouteNameInput);
                EditText CityDst = (EditText) findViewById(R.id.CityDstInput);
                EditText HighWayDst = (EditText) findViewById(R.id.HwayDstInput);
                String name = Name.getText().toString();
                _currentRouteName = name;
                String temp_cityDst = CityDst.getText().toString();
                String temp_highWayDst = HighWayDst.getText().toString();
                if (!name.matches("") && !temp_cityDst.matches("") && !temp_highWayDst.matches("")) {
                    int cityDst = Integer.parseInt(temp_cityDst);
                    int highWayDst = Integer.parseInt(temp_highWayDst);
                    int totalDst = CalculateTotalDistance(cityDst, highWayDst);
                    if (totalDst == 0) {
                        String msg = "The total distance must not add up to zero.";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    ContentValues cv = new ContentValues();
                    cv.put(SuperUltraInfoDataBaseHelper.Route_Name, name);
                    cv.put(SuperUltraInfoDataBaseHelper.Route_City_Dst, cityDst);
                    cv.put(SuperUltraInfoDataBaseHelper.Route_HWY_Dst, highWayDst);
                    cv.put(SuperUltraInfoDataBaseHelper.Route_total_Dst, totalDst);
                    if (singleton.checkEdit() == 1) {
                        long DBID = _RouteToBeEdit.getRouteDBId();
                        long idPassBack = RouteDB.update(SuperUltraInfoDataBaseHelper.Route_Table, cv, "_id=" + DBID, null);
                        RouteDB.close();
                        singleton.userFinishEdit();
                        Intent userEditRoute = DisplayRouteList.makeIntent(AddNewRoute.this);
                        startActivity(userEditRoute);
                    } else {
                        long idPassedBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Route_Table, null, cv);
                        Route userInput = new Route(name, cityDst, highWayDst, totalDst, idPassedBack);
                        if (singleton.isEditingJourney()) {
                            checkTransportationMode(name, cityDst, highWayDst, totalDst, idPassedBack);
                            singleton.userFinishEditJourney();
                            Intent userEditJourney = DisplayJourneyList.makeIntent(AddNewRoute.this);
                            startActivity(userEditJourney);
                            singleton.userFinishAdd();
                            finish();
                        } else {
                            singleton.userFinishAdd();
                            calculateCO2(userInput);
                            Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
                            startActivity(ConfirmRoute);
                        }
                    }
                    finish();
                } else if (equalsZero(temp_cityDst, temp_highWayDst)) {
                    String msg = "The total distance must not add up to zero.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } else if (temp_cityDst.matches("") || temp_highWayDst.matches("")) {
                    int cityDst;
                    if (temp_cityDst.matches("")) {
                        cityDst = 0;
                    } else {
                        cityDst = Integer.parseInt(temp_cityDst);
                    }

                    int highWayDst;
                    if (temp_highWayDst.matches("")) {
                        highWayDst = 0;
                    } else {
                        highWayDst = Integer.parseInt(temp_highWayDst);
                    }


                    int totalDst = CalculateTotalDistance(cityDst, highWayDst);
                    if (totalDst == 0) {
                        String msg = "The total distance must not add up to zero.";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    ContentValues cv = new ContentValues();
                    cv.put(SuperUltraInfoDataBaseHelper.Route_Name, name);
                    cv.put(SuperUltraInfoDataBaseHelper.Route_City_Dst, cityDst);
                    cv.put(SuperUltraInfoDataBaseHelper.Route_HWY_Dst, highWayDst);
                    cv.put(SuperUltraInfoDataBaseHelper.Route_total_Dst, totalDst);
                    if (singleton.checkEdit() == 1) {
                        long DBID = _RouteToBeEdit.getRouteDBId();
                        long idPassBack = RouteDB.update(SuperUltraInfoDataBaseHelper.Route_Table, cv, "_id=" + DBID, null);
                        RouteDB.close();
                        singleton.userFinishEdit();
                        Intent userEditRoute = DisplayRouteList.makeIntent(AddNewRoute.this);
                        startActivity(userEditRoute);
                    } else {
                        long idPassedBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Route_Table, null, cv);
                        Route userInput = new Route(name, cityDst, highWayDst, totalDst, idPassedBack);
                        if (singleton.isEditingJourney()) {
                            checkTransportationMode(name, cityDst, highWayDst, totalDst, idPassedBack);
                            singleton.userFinishEditJourney();
                            Intent userEditJourney = DisplayJourneyList.makeIntent(AddNewRoute.this);
                            startActivity(userEditJourney);
                            singleton.userFinishAdd();
                            finish();
                        } else {
                            singleton.userFinishAdd();
                            singleton.addedJourneyToday();
                            calculateCO2(userInput);
                            Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
                            startActivity(ConfirmRoute);
                        }
                    }
                } else {
                    String msg = "Please enter at least your name and one of the distance fields.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }

            private void checkTransportationMode(String name, int cityDst, int highWayDst, int totalDst, long idPassedBack) {
                String mode;
                if (singleton.checkTransportationMode() == 1) {
                    mode = "Walk/bike";//image id = 70
                    double co2 = 0;
                    editJoutneyDB(_date, 0, mode, mode, "N/A", "N/A", 0, 0, 0, "N/A", idPassedBack, name, cityDst,
                            highWayDst, totalDst, _EditedJourneyID, co2,70);
                } else if (singleton.checkTransportationMode() == 2) {
                    mode = "Bus";//image id = 50
                    double co2 = (cityDst + highWayDst) * 0.089;
                    editJoutneyDB(_date, 0, mode, mode, "N/A", "N/A", 0, 0, 0, "N/A", idPassedBack, name, cityDst,
                            highWayDst, totalDst, _EditedJourneyID, co2,50);

                } else if (singleton.checkTransportationMode() == 3) {
                    mode = "Skytrain";//image id = 60
                    double co2 = (cityDst + highWayDst) * 0.02348;
                    editJoutneyDB(_date, 0, mode, mode, "N/A", "N/A", 0, 0, 0, "N/A", idPassedBack, name, cityDst,
                            highWayDst, totalDst, _EditedJourneyID, co2,60);
                } else {
                    mode = "Car";
                    double fuelCost;
                    if (_vehicle.getFuelType().toLowerCase().matches("diesel")) {
                        fuelCost = 10.16;
                    } else if (_vehicle.getFuelType().toLowerCase().matches("electricity")) {
                        fuelCost = 0;
                    } else {
                        fuelCost = 8.89;
                    }
                    double cityGas = (cityDst * 0.621371192 / _vehicle.getCity08());
                    double hwyGas = highWayDst * 0.621371192 / _vehicle.getHighway08();
                    double totalGas = cityGas + hwyGas;
                    double co2 = fuelCost * totalGas;
                    editJoutneyDB(_date, _vehicle.getVehicleDBId(), _vehicle.getName(), mode, _vehicle.getMake()
                            , _vehicle.getModel(), _vehicle.getYear(), _vehicle.getCity08(),
                            _vehicle.getHighway08(), _vehicle.getFuelType(), idPassedBack, name, cityDst,
                            highWayDst, totalDst, _EditedJourneyID, co2,_vehicle.getImageID());
                }
            }

            private void editJoutneyDB(String date, long CarId,String CarName, String Mode, String CarMake,
                                       String CarModel, int CarYear, double CarCity08, double CarHwy08,
                                       String CarFuelTyep,
                                       long RouteId, String RouteName, int RouteCityDst,
                                       int RouteHwyDst, int TotalDst, long JourneyID, double totalCO2, int imageID) {
                ContentValues cv = new ContentValues();
                cv.put(SuperUltraInfoDataBaseHelper.Journey_Date,date);

                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarId,CarId);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarName, CarName);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMode, Mode);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMake,CarMake);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarModel,CarModel);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarYear,CarYear);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarCity,CarCity08);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarHwy,CarHwy08);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_CarFuelType,CarFuelTyep);

                cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteId, RouteId);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteName, RouteName);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteCityDist, RouteCityDst);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteHwyDist, RouteHwyDst);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteTotalDist, TotalDst);

                cv.put(SuperUltraInfoDataBaseHelper.Journey_CO2Emitted, totalCO2);
                cv.put(SuperUltraInfoDataBaseHelper.Journey_Image, imageID);

                long idPassBack = RouteDB.update(SuperUltraInfoDataBaseHelper.Journey_Table,cv,"_id="+JourneyID, null);
                RouteDB.close();

            }
        });
    }

    private boolean equalsZero(String cityDist, String highwayDist) {
        if (cityDist.matches("") && Integer.parseInt(highwayDist) == 0) {
            return true;
        } else if (highwayDist.matches("") && Integer.parseInt(cityDist) == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void delButton(final long position) {
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.comfirm_delete);
        if (singleton.checkAdd() == 1){
            delete.setVisibility(View.INVISIBLE);
            return;
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddNewRoute.this)
                        .setTitle("Delete Route")
                        .setMessage(R.string.Warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent del_intent = new Intent();
                                RouteDB.delete(SuperUltraInfoDataBaseHelper.Route_Table,
                                        "_id"+"="+position,null);
                                RouteDB.close();
                                singleton.userFinishEdit();
                                setResult(Activity.RESULT_OK,del_intent);
                                Toast.makeText(AddNewRoute.this,getString(R.string.UserDeleteRoute),Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                singleton.userFinishEdit();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        singleton.userFinishEdit();
        singleton.userFinishAdd();
        Intent goBackToDisplayRoute = DisplayRouteList.makeIntent(AddNewRoute.this);
        startActivity(goBackToDisplayRoute);
        finish();
    }

    public void calculateCO2(Route userInput){
        int cityDistance = userInput.getCityDistance();
        int HwyDistance = userInput.getHighwayDistance();
        int TransportMode = singleton.checkTransportationMode();
        double totalCO2 = 0;

        if (TransportMode == 1) { // Walk/Bike
            String TotalCO2 = String.format("%.2f", totalCO2);
            if(singleton.checkCO2Unit() == 0){
                Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "The CO2 emission you have produced is "+
                        "equivalent to producing 0.00 kg of regular garbage.", Toast.LENGTH_SHORT).show();
            }

            addJourneyToDBNotCar(_date,userInput,RouteDB,"Walk/Bike",totalCO2,70);

            createNewJourney(cityDistance,HwyDistance,totalCO2, 1);
        }
        else if (singleton.checkTransportationMode() == 2){ //Bus
            totalCO2 = (cityDistance+HwyDistance)*0.089;
            String TotalCO2 = String.format("%.2f", totalCO2);
            String HumanCO2 = String.format("%.2f", totalCO2/2.06);
            if(singleton.checkCO2Unit() == 0){
                Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "The CO2 emission you have produced is "+
                        "equivalent to producing "+HumanCO2+"kg of regular garbage.", Toast.LENGTH_SHORT).show();
            }

            Cursor cursor = RouteDB.rawQuery("select max(JourneyCO2Emitted) from JourneyInfoTable" +
                    " where JourneyMode = 'Bus'",null);
            double maxCO2 = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                maxCO2 = cursor.getDouble(0);
                cursor.moveToNext();
            }
            cursor.close();
            if (maxCO2 < totalCO2){
                singleton.setCarCO2Highest(true);
                singleton.setHighestCO2FromCar(totalCO2);
            }

            addJourneyToDBNotCar(_date,userInput,RouteDB,"Bus",totalCO2,50);

            createNewJourney(cityDistance,HwyDistance,totalCO2, 2);
        }
        else if (singleton.checkTransportationMode() == 3){ //Skytrain
            totalCO2 = (cityDistance+HwyDistance)*0.02348;
            String TotalCO2 = String.format("%.2f", totalCO2);
            String HumanCO2 = String.format("%.2f", totalCO2/2.06);
            if(singleton.checkCO2Unit() == 0){
                Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "The CO2 emission you have produced is "+
                        "equivalent to producing "+HumanCO2+"kg of regular garbage.", Toast.LENGTH_SHORT).show();
            }

            Cursor cursor = RouteDB.rawQuery("select max(JourneyCO2Emitted) from JourneyInfoTable" +
                    " where JourneyMode = 'Skytrain'",null);
            double maxCO2 = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                maxCO2 = cursor.getDouble(0);
                cursor.moveToNext();
            }
            cursor.close();
            if (maxCO2 < totalCO2){
                singleton.setCarCO2Highest(true);
                singleton.setHighestCO2FromCar(totalCO2);
            }
            addJourneyToDBNotCar(_date,userInput,RouteDB,"Skytrain",totalCO2,60);



            createNewJourney(cityDistance,HwyDistance,totalCO2, 3);
        }
        else {
            double cityConsume = singleton.getVehicle().getCity08();
            double HwyConsume = singleton.getVehicle().getHighway08();
            String fuelType = singleton.getVehicle().getFuelType();
            double fuelCost;
            if (fuelType.toLowerCase().matches("diesel")) {
                fuelCost = 10.16;
            } else if (fuelType.toLowerCase().matches("electricity")) {
                fuelCost = 0;
            } else {
                fuelCost = 8.89;
            }
            double cityGas = (cityDistance * 0.621371192 / cityConsume);
            double hwyGas = HwyDistance * 0.621371192 / HwyConsume;
            double totalGas = cityGas + hwyGas;
            totalCO2 = fuelCost * totalGas;
            String TotalCO2 = String.format("%.2f", totalCO2);
            String HumanCO2 = String.format("%.2f", totalCO2/2.06);
            if(singleton.checkCO2Unit() == 0){
                Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "The CO2 emission you have produced is "+
                        "equivalent to producing "+HumanCO2+"kg of regular garbage.", Toast.LENGTH_SHORT).show();
            }

            Cursor cursor = RouteDB.rawQuery("select max(JourneyCO2Emitted) from JourneyInfoTable" +
                    " where JourneyMode = 'Car'",null);
            double maxCO2 = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                maxCO2 = cursor.getDouble(0);
                cursor.moveToNext();
            }
            cursor.close();
            if (maxCO2 < totalCO2){
                singleton.setCarCO2Highest(true);
                singleton.setHighestCO2FromCar(totalCO2);
            }


            ContentValues cv = new ContentValues();
            cv.put(SuperUltraInfoDataBaseHelper.Journey_Date,_date);

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarId,_vehicle.getVehicleDBId());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarName, _vehicle.getName());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMode, "Car");
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMake,_vehicle.getMake());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarModel,_vehicle.getModel());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarYear,_vehicle.getYear());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarCity,_vehicle.getCity08());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarHwy,_vehicle.getHighway08());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarFuelType,_vehicle.getFuelType());

            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteId, userInput.getRouteDBId());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteName, userInput.getName());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteCityDist, userInput.getCityDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteHwyDist, userInput.getHighwayDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteTotalDist, userInput.getTotalDistance());

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CO2Emitted, totalCO2);
            cv.put(SuperUltraInfoDataBaseHelper.Journey_Image, _vehicle.getIndexID());

            long idPassBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Journey_Table,null,cv);
            RouteDB.close();



            createNewJourney(cityDistance, HwyDistance, totalCO2, 0);
        }

        if (singleton.isEditingJourney()) {

        } else {
            Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
            startActivity(ConfirmRoute);
        }
        finish();
    }

    private void addJourneyToDBNotCar(String date, Route route, SQLiteDatabase DB, String Mode,double CO2,int imageid) {
        ContentValues cv = new ContentValues();
        cv.put(SuperUltraInfoDataBaseHelper.Journey_Date,date);
        cv.put(SuperUltraInfoDataBaseHelper.Journey_CarName,Mode);
        cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMode,Mode);
        cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteId, route.getRouteDBId());
        cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteName, route.getName());
        cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteCityDist, route.getCityDistance());
        cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteHwyDist, route.getHighwayDistance());
        cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteTotalDist, route.getTotalDistance());
        cv.put(SuperUltraInfoDataBaseHelper.Journey_CO2Emitted, CO2);
        cv.put(SuperUltraInfoDataBaseHelper.Journey_Image, imageid);
        long idPassBack = DB.insert(SuperUltraInfoDataBaseHelper.Journey_Table,null,cv);
        DB.close();
    }
    
    public int CalculateTotalDistance(int cityDst, int hwyDst){
        int totalDst = cityDst + hwyDst;
        return totalDst;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddNewRoute.class);
    }

    private void createNewJourney(int cityDistance,int hwyDistance,double co2, int TransMode){
        DecimalFormat Format = new DecimalFormat("#.##");
        double CO2 = Double.valueOf(Format.format(co2));
        int imageid=0;
        String VehicleName = "";
        switch(TransMode){
            case 0:
                VehicleName = _vehicle.getName();
                imageid = _vehicle.getIndexID();
                break;
            case 1:
                VehicleName = "Walk/Bike";
                imageid = 70;
                break;
            case 2:
                VehicleName = "Bus";
                imageid = 50;
                break;
            case 3:
                VehicleName = "Skytrain";
                imageid = 60;
                break;
        }
        
        long temp = 9999;
        Journey journey = new Journey(_date, VehicleName, _currentRouteName,(cityDistance+hwyDistance), VehicleName, CO2, temp,imageid);
        if (singleton.isEditingJourney()) {
            singleton.changeJourney(journey);
        }
    }

    private String checkDayIsSingleDIgit(String userDay) {
        if (userDay.length() == 1){
            return "0"+userDay;
        }else{
            return userDay;
        }
    }

    private String ChangeMonthInInt(String _month) {
        if (_month.matches("January")){
            return "01";
        }
        if (_month.matches("February")){
            return "02";
        }
        if (_month.matches("March")){
            return "03";
        }
        if (_month.matches("April")){
            return "04";
        }
        if (_month.matches("May")){
            return "05";
        }
        if (_month.matches("June")){
            return "06";
        }
        if (_month.matches("July")){
            return "07";
        }
        if (_month.matches("August")){
            return "08";
        }
        if (_month.matches("September")){
            return "09";
        }
        if (_month.matches("October")){
            return "10";
        }
        if (_month.matches("November")){
            return "11";
        }
        else{
            return "12";
        }
    }

    private void setToolBar(){
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_add_route);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.tool_change_unit){
            if(singleton.checkCO2Unit() == 0)
                singleton.humanRelatableUnit();
            else
                singleton.originalUnit();
            saveCO2UnitStatus(singleton.checkCO2Unit());
            Toast.makeText(getApplicationContext(), "CO2 unit has been changed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.tool_about){
            startActivity(new Intent(AddNewRoute.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCO2UnitStatus(int status) {
        SharedPreferences prefs = this.getSharedPreferences("CO2Status", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("CO2 status", status);
        editor.apply();
    }

}
