package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainMenu extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();
    public SQLiteDatabase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        showUpTips(0);
        getJourneyList();
        setContentView(R.layout.activity_main_menu);
        setButton(R.id.btnCreateJourney);
        setButton(R.id.btnCurrentFootprint);
        setButton(R.id.btnEditJourney);
        setButton(R.id.btnMonthlyUti);
    }
    private List<String> allRandomTips = new ArrayList<>();

    private void showUpTips(final int i) {
        allRandomTips.add("tip1");
        allRandomTips.add("tip2");
        allRandomTips.add("tip3");
        allRandomTips.add("tip4");
        allRandomTips.add("tip5");
        allRandomTips.add("tip6");
        allRandomTips.add("tip7");
        allRandomTips.add("tip8");
        allRandomTips.add("tip9");
        //Collections.shuffle(allRandomTips);
        Snackbar.make(findViewById(android.R.id.content), allRandomTips.get(i), Snackbar.LENGTH_LONG)
                .setAction("Next", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpTips(i+1);
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
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
                "_id from JourneyInfoTable order by date(JourneyDate) asc ",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String[] tempDate = (cursor.getString(0)).split("-");
            String tempYear = tempDate[0];
            String tempMonth = ChangMonthInString(tempDate[1]);
            String tempDay = ChangDayInString(tempDate[2]) ;
            String date = tempDay + "/" + tempMonth + "/" + tempYear;
            String mode = cursor.getString(1);
            String routeName = cursor.getString(3);
            int totalDst = cursor.getInt(4);
            String vehicleName = cursor.getString(2);
            double co2 = cursor.getDouble(5);
            long JourneyDBId = cursor.getLong(cursor.getColumnIndex("_id"));
            Journey tempJourney = new Journey(date,mode,routeName,
                    totalDst,vehicleName,co2,JourneyDBId);
            JourneyListFromDB.add(tempJourney);
            cursor.moveToNext();
        }
        cursor.close();
        myDataBase.close();
        singleton.setJourneyList(JourneyListFromDB);
    }

    private String ChangDayInString(String tempDay) {
        if (tempDay.matches("01")){
            return "1";
        }
        if (tempDay.matches("02")){
            return "2";
        }
        if (tempDay.matches("03")){
            return "3";
        }
        if (tempDay.matches("04")){
            return "4";
        }
        if (tempDay.matches("05")){
            return "5";
        }
        if (tempDay.matches("06")){
            return "6";
        }
        if (tempDay.matches("07")){
            return "7";
        }
        if (tempDay.matches("08")){
            return "8";
        }
        else{
            return "9";
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
                        //showActivity = DisplayCarList.makeIntent(MainMenu.this);
                        showActivity = new Intent(MainMenu.this, SelectTransportationModeAndDate.class);
                        break;
                    case R.id.btnCurrentFootprint:
                        showActivity = new Intent(MainMenu.this,DisplayCarbonFootprint.class);
                        break;
                    case R.id.btnEditJourney:
                        showActivity = new Intent(MainMenu.this, DisplayJourneyList.class);
                        break;
                    case R.id.btnMonthlyUti:
                        showActivity = new Intent(MainMenu.this, DisplayMonthlyUtilities.class);
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
}
