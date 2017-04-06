package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.util.Calendar;

/**
 * Sets date when using calender
 */


public class DisplayCalendar extends AppCompatActivity {
    private Singleton singleton = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calendar);
        getUsersDataPick();
        setToolBar();
    }



    private void  getUsersDataPick(){
        if(getIntent().getIntExtra("AddMonthlyUtilities", 0) == 10) {
            //User sets starting date for monthly utilities
            CalendarView view = new CalendarView(this);
            setContentView(view);
            view.setOnDateChangeListener(new OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                                int date) {
                    singleton.setStartDay("" +date);
                    singleton.setStartMonth(""+(month+1));
                    singleton.setStartYear("" + year);
                    singleton.setStartDateChanged(true);
                }
            });
        }
        else if(getIntent().getIntExtra("AddMonthlyUtilities", 0) == 20) {
            //User sets ending date for monthly utilities
            CalendarView view = new CalendarView(this);
            setContentView(view);
            view.setOnDateChangeListener(new OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                                int date) {
                    singleton.setEndDay("" +date);
                    singleton.setEndMonth(""+(month+1));
                    singleton.setEndYear("" + year);
                    singleton.setEndDateChanged(true);
                }
            });
        }
        else {
            CalendarView view = new CalendarView(this);
            setContentView(view);
            view.setOnDateChangeListener(new OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                                int date) {
                    singleton.setUserDay("" + date);
                    singleton.setUserMonth(getStringMonth(month));
                    singleton.setUserYear("" + year);
                    singleton.setIsDateChanged(true);
                }
            });
        }
    }

    public String  getStringMonth(int month){
        switch (month){

            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";

        }

            return  null;
    }

    private void setToolBar(){
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_calendar);
        setSupportActionBar(toolBar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            startActivity(new Intent(DisplayCalendar.this, AboutActivity.class));
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
