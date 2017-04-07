package com.example.olive.carbon_tracker.UI;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.AlarmReceiver;
import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * lets user navigate to add journey, edit journey, add utility and display carbon footprint
 */
public class MainMenu extends AppCompatActivity {
    private static final long NUM_DAYS_REMINDER = 43;
    Singleton singleton = Singleton.getInstance();
    public SQLiteDatabase myDataBase;

    private List<String> allRandomCarTips = new ArrayList<>();
    private List<String> allRandomEnegyTips = new ArrayList<>();
    private List<String> allRandomUnrelatedTips = new ArrayList<>();
    private int whichTipShowUP = (int)(Math.random() * 50 + 1);
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
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);
        getLatestBill();
        checkNotifications();
        setAlarm();
        if(singleton.checkCO2Unit() == 0) {
            generateTipsForCar(singleton.getHighestCO2FromCar());
            generateTipsForEnergy(singleton.getHighestCO2FromEnegy());
        }
        else {
            generateTipsForCarInHuman(singleton.getHighestCO2FromCar());
            generateTipsForEnergyInHuman(singleton.getHighestCO2FromEnegy());
        }
        generateTipsForUnrelated();


        HighestCO2FromCar();
        getJourneyList();
        setContentView(R.layout.activity_main_menu);
        setButton(R.id.btnCreateJourney);
        setButton(R.id.btnEditJourney);
        setButton(R.id.btnMonthlyUti);
        setButton( R.id.btnCurrentFootprint);
    }


    private void generateTipsForCar(double co2){
        allRandomCarTips.add(getString(R.string.vehicle_tip_1, co2));
        allRandomCarTips.add(getString(R.string.vehicle_tip_2, co2));
        allRandomCarTips.add(getString(R.string.vehicle_tip_3, co2));
        allRandomCarTips.add(getString(R.string.vehicle_tip_4, co2));
        allRandomCarTips.add(getString(R.string.vehicle_tip_5, co2));
        allRandomCarTips.add(getString(R.string.vehicle_tip_6, co2));
        allRandomCarTips.add(getString(R.string.vehicle_tip_7, co2));
        allRandomCarTips.add(getString(R.string.vehicle_tip_8, co2));
    }

    private void generateTipsForCarInHuman(double co2){
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_1, co2/2.06));
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_2, co2/2.06));
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_3, co2/2.06));
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_4, co2/2.06));
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_5, co2/2.06));
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_6, co2/2.06));
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_7, co2/2.06));
        allRandomCarTips.add(getString(R.string.vehicle_tip_human_8, co2/2.06));
    }


    private void generateTipsForEnergy(double co2) {
        allRandomEnegyTips.add(getString(R.string.energy_tip_1, co2));
        allRandomEnegyTips.add(getString(R.string.energy_tip_2, co2));
        allRandomEnegyTips.add(getString(R.string.energy_tip_3, co2));
        allRandomEnegyTips.add(getString(R.string.energy_tip_4, co2));
        allRandomEnegyTips.add(getString(R.string.energy_tip_5, co2));
        allRandomEnegyTips.add(getString(R.string.energy_tip_6, co2));
        allRandomEnegyTips.add(getString(R.string.energy_tip_7, co2));
        allRandomEnegyTips.add(getString(R.string.energy_tip_8, co2));
    }

    private void generateTipsForEnergyInHuman(double co2){
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_1, co2/2.06));
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_2, co2/2.06));
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_3, co2/2.06));
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_4, co2/2.06));
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_5, co2/2.06));
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_6, co2/2.06));
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_7, co2/2.06));
        allRandomEnegyTips.add(getString(R.string.energy_tip_human_8, co2/2.06));
    }


    private void generateTipsForUnrelated(){
        Cursor cursor = myDataBase.rawQuery("select count(*) from JourneyInfoTable where " +
                "JourneyMode = 'Car'",null);
        int CarTimes = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CarTimes = cursor.getInt(0);
            cursor.moveToNext();
        }
        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_1,CarTimes));
        cursor = myDataBase.rawQuery("select sum(UtilityAverageCO2) from " +
                "UtilityInfoTable  ",null);
        double totalUtilityCO2 = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            totalUtilityCO2 = cursor.getDouble(0);
            cursor.moveToNext();
        }
        if(singleton.checkCO2Unit() == 0)
            allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_2, totalUtilityCO2));
        else
            allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_human_2, totalUtilityCO2/2.06));

        cursor = myDataBase.rawQuery("select sum(JourneyRouteTotal) from JourneyInfoTable " +
                "where JourneyMode = 'Car'",null);
        int TotalofTotalDST = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TotalofTotalDST = cursor.getInt(0);
            cursor.moveToNext();
        }
        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_3,TotalofTotalDST));
        cursor = myDataBase.rawQuery("select count(*) from JourneyInfoTable where " +
                "JourneyCarModel = 'Suv' ",null);
        int dringSUVtimes = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            dringSUVtimes = cursor.getInt(0);
            cursor.moveToNext();
        }
        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_4,dringSUVtimes));
        cursor = myDataBase.rawQuery("select sum(JourneyRouteHwy) from JourneyInfoTable " +
                "where JourneyMode= 'Car'",null);
        int totalHWYDST = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            totalHWYDST = cursor.getInt(0);
            cursor.moveToNext();
        }
        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_5,totalHWYDST));
        if(singleton.checkCO2Unit() == 0)
            allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_6,totalUtilityCO2));
        else
            allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_human_6,totalUtilityCO2/2.06));

        cursor = myDataBase.rawQuery("select sum(JourneyRouteCity) from JourneyInfoTable " +
                "where JourneyMode= 'Car'",null);
        int totalCITYDST = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            totalCITYDST = cursor.getInt(0);
            cursor.moveToNext();
        }
        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_7,totalCITYDST));
        cursor = myDataBase.rawQuery("select count(*) from JourneyInfoTable " +
                "where JourneyMode ='Bus' or JourneyMode='Skytrain'",null);
        int TotalBUSSKYtraintime = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TotalBUSSKYtraintime = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_8,TotalBUSSKYtraintime));
    }
    private void HighestCO2FromCar() {
        if (singleton.isCarCO2Highest()){
            singleton.setCarCO2Highest(false);
            showUpTips(0,allRandomCarTips);
        }else if (singleton.isEnegyHighest()){
            singleton.setEnegyHighest(false);
            showUpTips(0,allRandomEnegyTips);
        }else if(whichTipShowUP%3 ==0){
            showUpTips(0,allRandomUnrelatedTips);
        }else{
            whichTipShowUP++;
        }
    }

    private void showUpTips(final int i,final List<String> tipsList) {
        Snackbar tipBar =  Snackbar.make(findViewById(android.R.id.content), tipsList.get(i%tipsList.size()),
                Snackbar.LENGTH_LONG);
        View snackbarView = tipBar.getView();
        TextView tv= (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setMaxLines(5);
        //make snackBar contain up to 5 lines
            tipBar.setAction("Next", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUpTips(i + 1,tipsList);
                }
            });
            tipBar.setActionTextColor(Color.RED);
            tipBar.setDuration(7500);
            tipBar.show();
    }

    private void getJourneyList() {
        List<Journey> JourneyListFromDB = new ArrayList<>();
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);
        Cursor cursor = myDataBase.rawQuery("select JourneyDate," +
                "JourneyMode," +
                "JourneyCarName," +
                "JourneyRouteName, " +
                "JourneyRouteTotal, " +
                "JourneyCO2Emitted," +
                "JourneyImage," +
                "_id from JourneyInfoTable order by date(JourneyDate) asc ",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String[] tempDate = (cursor.getString(0)).split("-");
            String tempYear = tempDate[0];
            String tempMonth = ChangMonthInString(tempDate[1]);
            String tempDay = ChangDayInString(tempDate[2]) ;
            String date = tempDay + "/" + tempMonth + "/" + tempYear;
            Log.i("day:",tempDate[1].getClass().getName());
            String mode = cursor.getString(1);
            String routeName = cursor.getString(3);
            int totalDst = cursor.getInt(4);
            String vehicleName = cursor.getString(2);
            double co2 = cursor.getDouble(5);
            int imageid = cursor.getInt(6);
            long JourneyDBId = cursor.getLong(cursor.getColumnIndex("_id"));
            Journey tempJourney = new Journey(date,mode,routeName,
                    totalDst,vehicleName,co2,JourneyDBId,imageid);
            JourneyListFromDB.add(tempJourney);
            cursor.moveToNext();
        }
        cursor.close();
        myDataBase.close();
        singleton.setJourneyList(JourneyListFromDB);
    }

    private String ChangDayInString(String tempDay) {
        Log.i("day to be changed",tempDay);
        if (tempDay.equals("01")){
            return "1";
        }
        if (tempDay.equals("02")){
            return "2";
        }
        if (tempDay.equals("03")){
            return "3";
        }
        if (tempDay.equals("04")){
            return "4";
        }
        if (tempDay.equals("05")){
            return "5";
        }
        if (tempDay.equals("06")){
            return "6";
        }
        if (tempDay.equals("07")){
            return "7";
        }
        if (tempDay.equals("08")){
            return "8";
        }
        if (tempDay.equals("09")){
            return "9";
        }
        else {
            return tempDay;
        }

    }
    private String ChangMonthInString(String tempMonth) {
        if (tempMonth.matches("01")){
            return "January";
        }
        if (tempMonth.matches("02")){
            return "February";
        }
        if (tempMonth.matches("03")){
            return "March";
        }
        if (tempMonth.matches("04")){
            return "April";
        }
        if (tempMonth.matches("05")){
            return "May";
        }
        if (tempMonth.matches("06")){
            return "June";
        }
        if (tempMonth.matches("07")){
            return "July";
        }
        if (tempMonth.matches("08")){
            return "August";
        }
        if (tempMonth.matches("09")){
            return "September";
        }
        if (tempMonth.matches("10")){
            return "October";
        }
        if (tempMonth.matches("11")){
            return "November";
        }
        else{
            return "Decemeber";
        }
    }

    private void setButton(final int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent();
                switch (id) {
                    case R.id.btnCreateJourney:
                        showActivity = new Intent(MainMenu.this, SelectTransportationModeAndDate.class);
                        break;
                    case R.id.btnEditJourney:
                        showActivity = new Intent(MainMenu.this, DisplayJourneyList.class);
                        break;
                    case R.id.btnMonthlyUti:
                        showActivity = new Intent(MainMenu.this, DisplayMonthlyUtilities.class);
                        break;
                    case R.id.btnCurrentFootprint:
                        showActivity = new Intent(MainMenu.this, GraphPicker.class);
                        break;

                }
                startActivity(showActivity);
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenu.class);
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
            Cursor cursor = myDataBase.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } else if (mode == databaseCountMode.MoreUtilities) {
            String countQuery = "SELECT  * FROM " + "UtilityInfoTable";
            Cursor cursor = myDataBase.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } else {
            count = -1;
        }
        return count;
    }

    private Notification makeNotification(databaseCountMode mode) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
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
        builder.setContentTitle(getString(R.string.app_name));
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
                    MainMenu.this,
                    getString(R.string.date_difference_error, "MainMenu"),
                    Toast.LENGTH_LONG
            ).show();
        }
        return -1;
    }

    private void getLatestBill() {
        if (getDatabaseCount(databaseCountMode.MoreUtilities) == 0) {
            return;
        }

        Cursor cursor = myDataBase.rawQuery("select * from " +
                "UtilityInfoTable order by UtilityCreationDate asc",null);

        cursor.moveToLast();
        String creationDate = cursor.getString(8);
        cursor.close();
        singleton.setLatestBill(creationDate);
    }

    private void setAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("Notification", singleton.getNotification());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }
}
