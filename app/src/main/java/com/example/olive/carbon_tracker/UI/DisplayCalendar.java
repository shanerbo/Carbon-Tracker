package com.example.olive.carbon_tracker.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.AlarmReceiver;
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
        setAlarm();
        setContentView(R.layout.activity_calendar);
        getUsersDataPick();

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


    private void setAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
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
