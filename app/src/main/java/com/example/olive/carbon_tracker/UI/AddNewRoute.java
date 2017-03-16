package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.SuperUltraInfoDataBaseHelper;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.Singleton;

import java.util.ArrayList;
import java.util.List;

public class AddNewRoute extends AppCompatActivity {
    private List<Route> RouteList = new ArrayList<Route>();

    private String currentRouteName;
    private long position;
    private Singleton singleton  = Singleton.getInstance();
    private Vehicle vehicle = singleton.getVehicle();
    private String oldRouteName;
    private Route _RouteToBeEdit;
    private SQLiteDatabase RouteDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        SuperUltraInfoDataBaseHelper RouteDBhelper = new SuperUltraInfoDataBaseHelper(this);
        RouteDB = RouteDBhelper.getWritableDatabase();

        getSupportActionBar().hide();
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
            RouteDB.close();
            Route RouteToBeEdit = new Route(RouteName,CityDistance,HwyDistance,TotalDistance,_id);
            _RouteToBeEdit = RouteToBeEdit;


            oldRouteName = _RouteToBeEdit.getName();
            int Route_city_dis = _RouteToBeEdit.getCityDistance();
            int Route_hWay_dis = _RouteToBeEdit.getHighwayDistance();
            EditText Name = (EditText)findViewById(R.id.RouteNameInput);
            EditText cityDst = (EditText)findViewById(R.id.CityDstInput);
            EditText hWayDst = (EditText)findViewById(R.id.HwayDstInput);
            Name.setText(oldRouteName);
            cityDst.setText(""+Route_city_dis);
            hWayDst.setText(""+Route_hWay_dis);
        }else{
            position = singleton.getAddPosition();
        }
        checkButton(position);
        delButton(position);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RouteDB.close();
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
                currentRouteName = name;
                String temp_cityDst = CityDst.getText().toString();
                String temp_highWayDst = HighWayDst.getText().toString();
                if (!name.matches("") && !temp_cityDst.matches("") && !temp_highWayDst.matches("")) {

                    int cityDst = Integer.parseInt(temp_cityDst);
                    int highWayDst = Integer.parseInt(temp_highWayDst);
                    int totalDst = CalculateTotalDistance(cityDst,highWayDst);
                    if (singleton.checkEdit() == 1) {
                        long DBID = _RouteToBeEdit.getRouteDBId();
                        //RouteList.set(position, userInput);
                        ContentValues cv = new ContentValues();
                        cv.put(SuperUltraInfoDataBaseHelper.Route_Name, name);
                        cv.put(SuperUltraInfoDataBaseHelper.Route_City_Dst, cityDst);
                        cv.put(SuperUltraInfoDataBaseHelper.Route_HWY_Dst, highWayDst);
                        cv.put(SuperUltraInfoDataBaseHelper.Route_total_Dst,totalDst);
                        long idPassBack = RouteDB.update(SuperUltraInfoDataBaseHelper.Route_Table,cv,"_id="+DBID, null);
                        RouteDB.close();
                        //singleton.setRouteList(RouteList);
                        singleton.userFinishEdit();
                        //String newUserInputRouteName = userInput.getName();
                        //singleton.UserEnterNewRouteName(newUserInputRouteName,oldRouteName);
                        Intent userEditRoute = DisplayRouteList.makeIntent(AddNewRoute.this);
                        startActivity(userEditRoute);
                    } else {
                        //RouteList.add(userInput);

                        ContentValues cv = new ContentValues();
                        cv.put(SuperUltraInfoDataBaseHelper.Route_Name,name);
                        cv.put(SuperUltraInfoDataBaseHelper.Route_City_Dst,cityDst);
                        cv.put(SuperUltraInfoDataBaseHelper.Route_HWY_Dst,highWayDst);
                        cv.put(SuperUltraInfoDataBaseHelper.Route_total_Dst,totalDst);
                        long idPassedBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Route_Table,null,cv);
                        Route userInput = new Route(name, cityDst, highWayDst, totalDst,idPassedBack);

                        RouteDB.close();
                        //singleton.setRouteList(RouteList);
                        singleton.userFinishAdd();
                        calculateCO2(userInput);
                    }
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Please fill all blanks",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //
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
                                //RouteList.remove(position);
                                //singleton.setRouteList(RouteList);
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
                                finish();
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

        if(singleton.checkTransportationMode() == 1) { // Walk/Bike
            double totalCO2 = 0;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();

            createNewJourney(cityDistance,HwyDistance,totalCO2, 1);
            Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
            startActivity(ConfirmRoute);
            finish();
        }
        else if(singleton.checkTransportationMode() == 2){ //Bus
            double totalCO2 = (cityDistance+HwyDistance)*0.089;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();

            createNewJourney(cityDistance,HwyDistance,totalCO2, 2);
            Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
            startActivity(ConfirmRoute);
            finish();
        }
        else if(singleton.checkTransportationMode() == 3){ //Skytrain
            double totalCO2 = (cityDistance+HwyDistance)*0.033;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();

            createNewJourney(cityDistance,HwyDistance,totalCO2, 3);
            Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
            startActivity(ConfirmRoute);
            finish();
        }

        else {
            singleton.getVehicle().setCityDistance(cityDistance);
            singleton.getVehicle().setHwyDistance(HwyDistance);

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
            double totalCO2 = fuelCost * totalGas;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "The CO2 you produced: " + TotalCO2, Toast.LENGTH_SHORT).show();

            createNewJourney(cityDistance, HwyDistance, totalCO2, 0);

            Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
            startActivity(ConfirmRoute);
            finish();
        }
    }
    public int CalculateTotalDistance(int cityDst, int hwyDst){
        int totalDst = cityDst + hwyDst;
        return totalDst;
    }
    public static Intent makeIntent(Context context) {
        return new Intent(context, AddNewRoute.class);
    }



    private void createNewJourney(int cityDistance,int hwyDistance,double co2, int TransMode){
        String day =   singleton.getUserDay();
        String month =  singleton.getUserMonth();
        String year =  singleton.getUserYear();
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        Date date = new Date();
        DecimalFormat Format = new DecimalFormat("#.##");
        double CO2 = Double.valueOf(Format.format(co2));

        String VehicleName = "";
        switch(TransMode){
            case 0:
                VehicleName = vehicle.getName();
                break;
            case 1:
                VehicleName = "Walk/Bike";
                break;
            case 2:
                VehicleName = "Bus";
                break;
            case 3:
                VehicleName = "Skytrain";
                break;
        }
        Journey journey = new Journey(day+"/"+month+"/"+year,currentRouteName,(cityDistance+hwyDistance), VehicleName, CO2);
        singleton.addUserJourney(journey);
    }
}
