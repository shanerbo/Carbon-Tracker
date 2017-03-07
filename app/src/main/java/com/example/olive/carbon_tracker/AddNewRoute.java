package com.example.olive.carbon_tracker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddNewRoute extends AppCompatActivity {
    private RouteCollection allRoutes = new RouteCollection();
    private List<Route> RouteList = new ArrayList<Route>();
    private int position;
    Singleton singleton  = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_new_route);
        allRoutes = singleton.getUserRoutes();
        RouteList = singleton.getRouteList();
        if(singleton.checkEdit() == 1){
            position = singleton.getEditPosition();
            Route RouteToBeEdited = RouteList.get(position);
            String RouteName = RouteToBeEdited.getName();
            int Route_city_dis = RouteToBeEdited.getCityDistance();
            int Route_hWay_dis = RouteToBeEdited.getHighwayDistance();
            int Route_total_dis = RouteToBeEdited.getTotalDistance();
            EditText Name = (EditText)findViewById(R.id.RouteNameInput);
            EditText cityDst = (EditText)findViewById(R.id.CityDstInput);
            EditText hWayDst = (EditText)findViewById(R.id.HwayDstInput);
            EditText totalDst = (EditText)findViewById(R.id.TotalDstCal);
            Name.setText(RouteName);
            cityDst.setText(""+Route_city_dis);
            hWayDst.setText(""+Route_hWay_dis);
            totalDst.setText(""+Route_total_dis);
        }else{
            position = singleton.getAddPosition();
        }
        checkButton(position);
        //delButton();
    }

    private void checkButton(final int position) {
        FloatingActionButton check = (FloatingActionButton) findViewById(R.id.comfirm_add);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Name = (EditText)findViewById(R.id.RouteNameInput);
                EditText CityDst = (EditText)findViewById(R.id.CityDstInput);
                EditText HighWayDst = (EditText)findViewById(R.id.HwayDstInput);
                EditText TotalDst = (EditText)findViewById(R.id.TotalDstCal);

//                int Route_index = passedInData.getIntExtra("route index",0);
                String name = Name.getText().toString();
                String temp_cityDst = CityDst.getText().toString();
                String temp_highWayDst = HighWayDst.getText().toString();
                String temp_totalDst = TotalDst.getText().toString();
                if(!name.matches("") && !temp_cityDst.matches("") && !temp_highWayDst.matches("") && !temp_totalDst.matches("")){
                    int cityDst = Integer.parseInt(temp_cityDst);
                    int highWayDst = Integer.parseInt(temp_highWayDst);
                    int totalDst = Integer.parseInt(temp_totalDst);
                    if (cityDst <= 0 || highWayDst <=0 || totalDst <=0){
                        Snackbar.make(v, "The Distance Cannot Be Smaller Than 0", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                        return;
                    }
                    Route userInput = new Route(name,cityDst,highWayDst,totalDst);
                    if (singleton.checkEdit() == 1){
                        allRoutes.changeRoute(userInput,position);
                        singleton.setUserRoutes(allRoutes);
            //changing Route to Route list
                        RouteList.set(position,userInput);
                        singleton.setRouteList(RouteList);
                        singleton.userFinishEdit();
                    }else{
                        allRoutes.addRoute(userInput);
                        singleton.setUserRoutes(allRoutes);
                        RouteList.add(userInput);
                        singleton.setRouteList(RouteList);
                    }

                    finish();
                }
            }
        });
    }
//
//    private void delButton() {
//        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.comfirm_delete);
//        if (passedInData.getStringExtra("edit or add").matches("add")){
//            delete.setVisibility(View.INVISIBLE);
//            //hide the delete button
//            return;
//        }
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(AddNewRoute.this)
//                        .setTitle("Delete Route")
//                        .setMessage(R.string.Warning)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent del_intent = new Intent();
//                                del_intent.putExtra("action","del");
//                                del_intent.putExtra("theIndex",passedInData.getIntExtra("pot index",0));
//                                setResult(Activity.RESULT_OK,del_intent);
//                                Toast.makeText(AddNewRoute.this,getString(R.string.UserDeletePot),Toast.LENGTH_LONG).show();
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                finish();
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert).show();
//            }
//        });
//    }



    public static Intent makeIntent(Context context) {
            return new Intent(context, AddNewRoute.class);
    }
}
