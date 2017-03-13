package com.example.olive.carbon_tracker.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar extends AppCompatActivity {
    private Singleton singleton = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getUsersDataPick();

    }

    private void  getUsersDataPick(){
        CalendarView view = new CalendarView(this);
        setContentView(view);
        Toast.makeText(getApplicationContext(),"is date changed: " + singleton.getIsDateChanged(),Toast.LENGTH_SHORT).show();
        view.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {

                singleton.setUserDay("" +date);
                singleton.setUserMonth(getStringMonth(month));
                singleton.setUserYear("" + year);
                singleton.setIsDateChanged(true);
                Toast.makeText(getApplicationContext(),"is date changed: " + singleton.getIsDateChanged(),Toast.LENGTH_SHORT).show();
            }
        });
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


}
