package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.RouteCollection;
import com.example.olive.carbon_tracker.Model.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class AddNewRoute extends AppCompatActivity {
    //private RouteCollection allRoutes = new RouteCollection();
    private List<Route> RouteList = new ArrayList<Route>();
  //  List<Journey> journeysList = new ArrayList<>();

    String currentRouteName;
    private int position;
    Singleton singleton  = Singleton.getInstance();
    Vehicle vehicle = singleton.getVehicle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();


        Toast.makeText(AddNewRoute.this,"car clicked is " + vehicle.getName()   , Toast.LENGTH_LONG).show();


        setContentView(R.layout.activity_add_new_route);
       // allRoutes = singleton.getUserRoutes();
        RouteList = singleton.getRouteList();
        if(singleton.checkEdit() == 1){
            position = singleton.getEditPosition();
            Route RouteToBeEdited = RouteList.get(position);
            String RouteName = RouteToBeEdited.getName();
            int Route_city_dis = RouteToBeEdited.getCityDistance();
            int Route_hWay_dis = RouteToBeEdited.getHighwayDistance();
            EditText Name = (EditText)findViewById(R.id.RouteNameInput);
            EditText cityDst = (EditText)findViewById(R.id.CityDstInput);
            EditText hWayDst = (EditText)findViewById(R.id.HwayDstInput);
            Name.setText(RouteName);
            cityDst.setText(""+Route_city_dis);
            hWayDst.setText(""+Route_hWay_dis);
        }else{
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


//                EditText TotalDst = (EditText) findViewById(R.id.TotalDstCal);
//                String temp_totalDst = TotalDst.getText().toString();

                String name = Name.getText().toString();
                currentRouteName = name;
                String temp_cityDst = CityDst.getText().toString();
                String temp_highWayDst = HighWayDst.getText().toString();
                if (!name.matches("") && !temp_cityDst.matches("") && !temp_highWayDst.matches("")) {
                    int cityDst = Integer.parseInt(temp_cityDst);
                    int highWayDst = Integer.parseInt(temp_highWayDst);
//                    int totalDst = Integer.parseInt(temp_totalDst);
                    if (cityDst <= 0 || highWayDst <= 0) {
                        Snackbar.make(v, "The Distance Cannot Be Smaller Than 0", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                    }
                    int totalDst = CalculateTotalDistance(cityDst,highWayDst);
                    Route userInput = new Route(name, cityDst, highWayDst, totalDst);
                    if (singleton.checkEdit() == 1) {
                        //allRoutes.changeRoute(userInput,position);
                        //singleton.setUserRoutes(allRoutes);
                        //changing Route to Route list
                        RouteList.set(position, userInput);
                        singleton.setRouteList(RouteList);
                        singleton.userFinishEdit();
                    } else {
                        //allRoutes.addRoute(userInput);
                        //singleton.setUserRoutes(allRoutes);
                        RouteList.add(userInput);
                        singleton.setRouteList(RouteList);
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
    private void delButton(final int position) {
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.comfirm_delete);
        if (singleton.checkAdd() == 1){
            delete.setVisibility(View.INVISIBLE);
            //hide the delete button
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
                                //allRoutes.delRoute(position);
                                //singleton.setUserRoutes(allRoutes);
                                //deleting pot form list
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
        Intent goBackToDisplayRoute = DisplayRouteList.makeIntent(AddNewRoute.this);
        startActivity(goBackToDisplayRoute);
    }

    public void calculateCO2(Route userInput){
        int cityDistance = userInput.getCityDistance();
        int HwyDistance = userInput.getHighwayDistance();
        singleton.getVehicle().setCityDistance(cityDistance);
        singleton.getVehicle().setHwyDistance(HwyDistance);

        int cityConsume = singleton.getVehicle().getCity08();
        int HwyConsume = singleton.getVehicle().getHighway08();
        String fuelType = singleton.getVehicle().getFuelType();
        double fuelCost;
        if (fuelType.toLowerCase() == "diesel") {
            fuelCost = 10.16;
        } else if (fuelType.toLowerCase() == "electricity") {
            fuelCost = 0;
        } else {
            fuelCost = 8.89;
        }
        double  cityGas = (cityDistance*1.00 / cityConsume);
        double hwyGas = HwyDistance*1.00  / HwyConsume;
        double totalGas = cityGas+hwyGas;
        double totalCO2 = fuelCost * totalGas;
        String TotalCO2 = String.format("%.2f", totalCO2);
        Toast.makeText(getApplicationContext(), "The CO2 you produced: " + TotalCO2, Toast.LENGTH_SHORT).show();

        createNewJourney(cityDistance,HwyDistance,totalCO2);



        Intent ConfirmRoute = MainMenu.makeIntent(AddNewRoute.this);
        startActivity(ConfirmRoute);
        finish();
    }
    public int CalculateTotalDistance(int cityDst, int hwyDst){
        int totalDst = cityDst + hwyDst;
        return totalDst;
    }
    public static Intent makeIntent(Context context) {
        return new Intent(context, AddNewRoute.class);
    }



    private void createNewJourney(int cityDistance,int hwyDistance,double co2){


        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();

        Journey journey = new Journey(date.toString(),currentRouteName,(cityDistance+hwyDistance), vehicle.getName(),co2);
                singleton.addUserJourney(journey);
    }
}
