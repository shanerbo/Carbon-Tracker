package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.Singleton;

import java.util.ArrayList;
import java.util.List;

public class AddNewRoute extends AppCompatActivity {
    private List<Route> RouteList = new ArrayList<Route>();
    String currentRouteName;
    private int position;
    Singleton singleton  = Singleton.getInstance();
    Vehicle vehicle = singleton.getVehicle();
    String oldRouteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_new_route);
        RouteList = singleton.getRouteList();
        if(singleton.checkEdit() == 1){
            position = singleton.getEditPosition();
            Route RouteToBeEdited = RouteList.get(position);
            oldRouteName = RouteToBeEdited.getName();
            int Route_city_dis = RouteToBeEdited.getCityDistance();
            int Route_hWay_dis = RouteToBeEdited.getHighwayDistance();
            EditText Name = (EditText)findViewById(R.id.RouteNameInput);
            EditText cityDst = (EditText)findViewById(R.id.CityDstInput);
            EditText hWayDst = (EditText)findViewById(R.id.HwayDstInput);
            Name.setText(oldRouteName);
            cityDst.setText(""+Route_city_dis);
            hWayDst.setText(""+Route_hWay_dis);
        } else{
            position = singleton.getAddPosition();
        }
        checkButton(position);
        delButton(position);
    }

    private void checkButton(final int position) {
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
                    Route userInput = new Route(name, cityDst, highWayDst, totalDst);
                    if (singleton.checkEdit() == 1) {
                        RouteList.set(position, userInput);
                        singleton.setRouteList(RouteList);
                        singleton.userFinishEdit();
                        String newUserInputRouteName = userInput.getName();
                        singleton.UserEnterNewRouteName(newUserInputRouteName, oldRouteName);
                        Intent userEditRoute = DisplayRouteList.makeIntent(AddNewRoute.this);
                        startActivity(userEditRoute);
                    } else if (singleton.isEditingJourney()) {
                        calculateCO2(userInput);
                        singleton.userFinishEditJourney();
                        Intent userEditJourney = DisplayJourneyList.makeIntent(AddNewRoute.this);
                        startActivity(userEditJourney);
                    } else {
                        RouteList.add(userInput);
                        singleton.setRouteList(RouteList);
                        singleton.userFinishAdd();
                        calculateCO2(userInput);
                        Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
                        startActivity(ConfirmRoute);
                    }
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Please fill all blanks",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void delButton(final int position) {
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
                                RouteList.remove(position);
                                singleton.setRouteList(RouteList);
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
//        Intent goBackToDisplayRoute = DisplayRouteList.makeIntent(AddNewRoute.this);
//        startActivity(goBackToDisplayRoute);
        finish();
    }

    public void calculateCO2(Route userInput){
        int cityDistance = userInput.getCityDistance();
        int HwyDistance = userInput.getHighwayDistance();
        int TransportMode = singleton.checkTransportationMode();
        double totalCO2 = 0;

        if(TransportMode == 1) { // Walk/Bike
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();
        } else if(TransportMode == 2){ //Bus
            totalCO2 = (cityDistance+HwyDistance)*0.089;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();
        } else if(TransportMode == 3){ //Skytrain
            totalCO2 = (cityDistance+HwyDistance)*0.033;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();
        } else {
            singleton.getVehicle().setCityDistance(cityDistance);
            singleton.getVehicle().setHwyDistance(HwyDistance);

            int cityConsume = singleton.getVehicle().getCity08();
            int HwyConsume = singleton.getVehicle().getHighway08();
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
            Toast.makeText(getApplicationContext(), "The CO2 you produced: " + TotalCO2, Toast.LENGTH_SHORT).show();
        }

        createNewJourney(cityDistance,HwyDistance,totalCO2, TransportMode);
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

        if (singleton.isEditingJourney()) {
            singleton.changeJourney(journey);
        } else {
            singleton.addUserJourney(journey);
        }
    }
}
