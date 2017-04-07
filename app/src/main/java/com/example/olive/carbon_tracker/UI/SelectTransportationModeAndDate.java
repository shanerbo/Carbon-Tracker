package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.SuperUltraInfoDataBaseHelper;
import com.example.olive.carbon_tracker.R;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * lets user to pick transportation mode and set travel date
 */

public class SelectTransportationModeAndDate extends AppCompatActivity {
    private static final long NUM_DAYS_REMINDER = 43;
    Singleton singleton = Singleton.getInstance();
    private static int UserTransportationMode;
    private SQLiteDatabase RouteDB;
    private long JourneyPosition = singleton.getEditPostion_Journey();
    private enum databaseCountMode {
        NoRecentJourneys,
        MoreJourneys,
        NoRecentUtilities,
        MoreUtilities
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SuperUltraInfoDataBaseHelper RouteDBhelper = new SuperUltraInfoDataBaseHelper(this);
        RouteDB = RouteDBhelper.getWritableDatabase();
        setContentView(R.layout.activity_select_transportation_mode_and_date);

        populateTransportationSpinner();
        viewCurrentDate();
        checkNotifications();
        setToolBar();
    }
    public void onRestart() {
        super.onRestart();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        currentDate.setText(day + "/" + month + "/" + year);
        singleton.setIsDateChanged(false);
    }
    private void viewCurrentDate(){
        boolean isDateChanged = singleton.getIsDateChanged();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate);
        if(!isDateChanged && !singleton.isEditingJourney()) {
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
                else if(UserMode.matches("Skytrain")){ //23.48g of CO2 emissions per km of skytrain travel.
                    UserTransportationMode = 3;
                    singleton.ModeSkytrain();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void setButton(final int id) {
        switch (id) {
            case R.id.tool_check:
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
            case R.id.tool_delete:
                if (!singleton.isEditingJourney()){
                    Intent goBackToMainMenu = MainMenu.makeIntent(SelectTransportationModeAndDate.this);
                    startActivity(goBackToMainMenu);
                    finish();
                }else {
                    new AlertDialog.Builder(SelectTransportationModeAndDate.this)
                            .setTitle("Delete MoreJourneys")
                            .setMessage(R.string.DeleteJourneyWarning)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent del_intent = new Intent();
                                    RouteDB.delete(SuperUltraInfoDataBaseHelper.Journey_Table,
                                            "_id" + "=" + JourneyPosition, null);
                                    RouteDB.close();
                                    singleton.userFinishEditJourney();
                                    setResult(Activity.RESULT_OK, del_intent);
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
                }
                break;
        }

    }
    public void onBackPressed() {
        if (singleton.isEditingJourney()){
            singleton.userFinishEditJourney();
            Intent goBackToJourneyList = DisplayJourneyList.makeIntent(SelectTransportationModeAndDate.this);
            startActivity(goBackToJourneyList);
        }else {
            checkNotifications();
            Intent goBackToMainMenu = MainMenu.makeIntent(SelectTransportationModeAndDate.this);
            startActivity(goBackToMainMenu);
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SelectTransportationModeAndDate.class);
    }

    // Remake notifications when necessary
    private void checkNotifications() {
        int journeys = getDatabaseCount(databaseCountMode.MoreJourneys);
        int utilities = getDatabaseCount(databaseCountMode.MoreUtilities);
        long dateDiff = getDateDifference(singleton.getLatestBill(), singleton.getCurrentDate());
        if (!singleton.isAddJourneyToday()) {
            singleton.setNotification(makeNotification(databaseCountMode.NoRecentJourneys));
        } else if (dateDiff >= NUM_DAYS_REMINDER) {
            singleton.setNotification(makeNotification(databaseCountMode.NoRecentUtilities));
        } else if (journeys > utilities) {
            singleton.setNotification(makeNotification(databaseCountMode.MoreUtilities, utilities));
        } else {
            singleton.setNotification(makeNotification(databaseCountMode.MoreJourneys, journeys));
        }
    }

    private int getDatabaseCount(databaseCountMode mode) {
        int count;
        if (mode == databaseCountMode.MoreJourneys) {
            String countQuery = "SELECT  * FROM " + "JourneyInfoTable";
            Cursor cursor = RouteDB.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } else if (mode == databaseCountMode.MoreUtilities) {
            String countQuery = "SELECT  * FROM " + "UtilityInfoTable";
            Cursor cursor = RouteDB.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } else {
            count = -1;
        }
        return count;
    }

    private Notification makeNotification(databaseCountMode mode) {
        Notification.Builder builder = new Notification.Builder(this);
        String msg;
        if (mode == databaseCountMode.NoRecentJourneys) {
            msg = getString(R.string.no_recent_journeys_notification);
        } else {
            msg = getString(R.string.no_utilities_in_a_month_and_a_half);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(makeNotificationIntent(mode));
        builder.setStyle(new Notification.BigTextStyle().bigText(msg));
        return builder.build();
    }

    private Notification makeNotification(databaseCountMode mode, int count) {
        Notification.Builder builder = new Notification.Builder(this);
        String msg;
        if (mode == databaseCountMode.MoreUtilities) {
            msg = getString(R.string.more_utilities_notification, count);
        } else {
            msg = getString(R.string.more_journeys_notification, count);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(makeNotificationIntent(mode));
        builder.setStyle(new Notification.BigTextStyle().bigText(msg));
        return builder.build();
    }

    private PendingIntent makeNotificationIntent(databaseCountMode mode) {
        Intent intent;
        if (mode == databaseCountMode.MoreUtilities
                || mode == databaseCountMode.NoRecentUtilities) {
            intent = new Intent(this, DisplayMonthlyUtilities.class);
        } else {
            intent = new Intent(this, SelectTransportationModeAndDate.class);
        }
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private long getDateDifference(String StartDate, String EndDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(StartDate);
            Date end = sdf.parse(EndDate);

            long dateDifference = end.getTime() - start.getTime();
            return dateDifference / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            Toast.makeText(
                    SelectTransportationModeAndDate.this,
                    getString(R.string.date_difference_error, "SelectTransportationAndDate"),
                    Toast.LENGTH_LONG
            ).show();
        }
        return -1;
    }

    private void setToolBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_mode);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_transmode, menu);
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
            startActivity(new Intent(SelectTransportationModeAndDate.this, AboutActivity.class));
            return true;
        }
        if(id == R.id.tool_date){
            startActivity(new Intent(SelectTransportationModeAndDate.this, DisplayCalendar.class));
            return true;
        }
        if(id == R.id.tool_check){
            setButton(R.id.tool_check);
            return true;
        }
        if(id == R.id.tool_delete){
            setButton(R.id.tool_delete);
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