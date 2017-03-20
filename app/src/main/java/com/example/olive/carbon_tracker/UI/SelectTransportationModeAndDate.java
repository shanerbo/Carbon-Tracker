package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.SuperUltraInfoDataBaseHelper;
import com.example.olive.carbon_tracker.R;

import java.nio.channels.SelectionKey;
import java.text.SimpleDateFormat;
import java.util.*;

public class SelectTransportationModeAndDate extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    private static int UserTransportationMode;
    private SQLiteDatabase RouteDB;
    private long JourneyPosition = singleton.getEditPostion_Journey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SuperUltraInfoDataBaseHelper RouteDBhelper = new SuperUltraInfoDataBaseHelper(this);
        RouteDB = RouteDBhelper.getWritableDatabase();
        setContentView(R.layout.activity_select_transportation_mode_and_date);

        populateTransportationSpinner();
        setButton(R.id.ID_button_OKmode);
        setButton(R.id.ID_button_mode_cancel);
        viewCurrentDate();
        setupCalendarButton();

    }
    public void onRestart() {
        super.onRestart();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        currentDate.setText(day + "/" + month + "/" + year);
        //TODO fix the problem that if i want to edit a journey like march 8th 2016,
        // the text shows is 08/March 2016 but the calender is pointing to the current date
        singleton.setIsDateChanged(true);
    }
    private void viewCurrentDate(){
        boolean isDateChanged = singleton.getIsDateChanged();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate);
        if(isDateChanged == false) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

            simpleDateFormat = new SimpleDateFormat("d");
            String day = simpleDateFormat.format(date);

            simpleDateFormat = new SimpleDateFormat("MMMM");
            String month = simpleDateFormat.format(date);

            simpleDateFormat = new SimpleDateFormat("yyyy");
            String year = simpleDateFormat.format(date);
            singleton.setUserDay(day);
            singleton.setUserMonth(month);
            singleton.setUserYear(year);
            currentDate.setText(day + "/" + month + "/" + year);
        } else {
            super.onRestart();
            String day = singleton.getUserDay();
            String month = singleton.getUserMonth();
            String year = singleton.getUserYear();
            currentDate.setText(day + "/" + month + "/" + year);
            singleton.setIsDateChanged(false);
        }
    }


    private void setupCalendarButton(){
            Button btn = (Button) findViewById(R.id.btnChangeDate);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(SelectTransportationModeAndDate.this,Calendar.class);
                    startActivity(intent);
                }
            });
    }

    private void populateTransportationSpinner() {
        String[] modes = {"Car", "Walk/bike", "Bus", "Skytrain"};
        ArrayAdapter<String> mode_adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, modes);
        Spinner mode_spinner = (Spinner) findViewById(R.id.ID_trans_mode_spinner);
        mode_spinner.setAdapter(mode_adapter);

        mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String UserMode = parent.getSelectedItem().toString();
                if(UserMode.matches("Car")){
                    UserTransportationMode = 0;
                    singleton.ModeCar();
                }
                else if(UserMode.matches("Walk/bike")){ //0g of CO2 emissions per km of walked / bike travel.
                    UserTransportationMode = 1;
                    singleton.ModeWalkBike();
                }
                else if(UserMode.matches("Bus")){ //89g of CO2 emissions per km of bus travel.
                    UserTransportationMode = 2;
                    singleton.ModeBus();
                }
                else if(UserMode.matches("Skytrain")){ //33g of CO2 emissions per km of skytrain travel.
                    UserTransportationMode = 3;
                    singleton.ModeSkytrain();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void setButton(final int id) {
        FloatingActionButton button = (FloatingActionButton) findViewById(id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id) {
                    case R.id.ID_button_OKmode:
                        if(UserTransportationMode == 0) { //user selects car
                            Intent showActivity = new Intent(SelectTransportationModeAndDate.this, DisplayCarList.class);
                            startActivity(showActivity);
                            break;
                        }
                        else { //user selects transportation modes other than cars
                            Intent showActivity = new Intent(SelectTransportationModeAndDate.this, DisplayRouteList.class);
                            startActivity(showActivity);
                            break;
                        }
                    case R.id.ID_button_mode_cancel:
                        new AlertDialog.Builder(SelectTransportationModeAndDate.this)
                                .setTitle("Delete Journey")
                                .setMessage(R.string.DeleteJourneyWarning)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent del_intent = new Intent();
                                        RouteDB.delete(SuperUltraInfoDataBaseHelper.Journey_Table,
                                                "_id"+"="+JourneyPosition,null);
                                        RouteDB.close();
                                        singleton.userFinishEdit();
                                        setResult(Activity.RESULT_OK,del_intent);
                                        Toast.makeText(SelectTransportationModeAndDate.this,getString(R.string.UserDeleteJourney),Toast.LENGTH_LONG).show();
                                        finish();
                                        Intent ShowNewJourneyList = DisplayJourneyList.makeIntent(SelectTransportationModeAndDate.this);
                                        startActivity(ShowNewJourneyList);
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
                        break;
                }
            }
        });
    }
    public void onBackPressed() {
        if (singleton.isEditingJourney()){
            singleton.userFinishEditJourney();
            Intent goBackToJourneyList = DisplayJourneyList.makeIntent(SelectTransportationModeAndDate.this);
            startActivity(goBackToJourneyList);
        }else {
            Intent goBackToMainMenu = MainMenu.makeIntent(SelectTransportationModeAndDate.this);
            startActivity(goBackToMainMenu);
        }
    }
    public static Intent makeIntent (Context context) {
        return new Intent(context, SelectTransportationModeAndDate.class);
    }

}
