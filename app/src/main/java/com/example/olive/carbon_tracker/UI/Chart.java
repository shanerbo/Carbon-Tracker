package com.example.olive.carbon_tracker.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.olive.carbon_tracker.Model.AlarmReceiver;
import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * draws a pie chart for single day data
 */

public class Chart extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setAlarm();
        setContentView(R.layout.activity_chart);
        setupPieChart();
    }

    private void setupPieChart(){
        List<Journey> journeyList = singleton.getUsersJourneys();
        //populating a list of PiesEntries:
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i =0;i<journeyList.size();i++) {
            if (!journeyList.isEmpty()) {
                Journey currentJourney = journeyList.get(i);
                int co2  = (int) currentJourney.getCarbonEmitted();
                pieEntries.add(new PieEntry(co2, currentJourney.getRouteName()));

            }
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"carbon emission");
        dataSet.setColors(Color.rgb(0,128,255),Color.rgb(96,96,96),Color.rgb(255, 153, 2255),Color.rgb(255, 128, 0),Color.rgb(255, 0, 0));
        PieData data = new PieData(dataSet);

        //get the chart:
        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
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
