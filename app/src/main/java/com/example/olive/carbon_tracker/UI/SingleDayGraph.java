package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class SingleDayGraph extends AppCompatActivity {
    public static final int DAY_TOKEN = 0;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;
    Singleton singleton = Singleton.getInstance();
    List<Double> todaysCO2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_single_day_graph);
        viewCurrentDate();

        setupPieChart();
        onRestart();
        setupCalendarButton();

    }

    private void setupPieChart() {

        getSingleDayCO2();
        //populating a list of PiesEntries:
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < todaysCO2.size(); i++) {
            if (!todaysCO2.isEmpty()) {
                int co2 = todaysCO2.get(i).intValue();
                pieEntries.add(new PieEntry(co2, ""));

            }
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, "carbon emission");
        dataSet.setColors(Color.rgb(0, 128, 255), Color.rgb(96, 96, 96), Color.rgb(255, 153, 2255), Color.rgb(255, 128, 0), Color.rgb(255, 0, 0));
        PieData data = new PieData(dataSet);

        //get the chart:
        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }

    public void onRestart() {
        super.onRestart();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate_SingleDay);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        currentDate.setText(day + "/" + month + "/" + year);
        singleton.setIsDateChanged(true);
        setupPieChart();
    }

    private void viewCurrentDate() {
        boolean isDateChanged = singleton.getIsDateChanged();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate_SingleDay);
        if (isDateChanged == false) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

            simpleDateFormat = new SimpleDateFormat("d");
            String day = simpleDateFormat.format(date);

            simpleDateFormat = new SimpleDateFormat("MMMM");
            String month = simpleDateFormat.format(date);

            simpleDateFormat = new SimpleDateFormat("YYYY");
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

    private void setupCalendarButton() {
        Button btn = (Button) findViewById(R.id.btnChangeDate_SingleDay);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SingleDayGraph.this, Calendar.class);
                startActivity(intent);
            }
        });
    }

    //TODO if we want i can add the route name as well for the legened
    public void getSingleDayCO2() {
        todaysCO2.clear();
        List<Journey> journeyList = singleton.getUsersJourneys();

        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();

        for (int i = 0; i < journeyList.size(); i++) {
            Journey currentJourney = journeyList.get(i);
            String[] currentDate = currentJourney.getDateOfTrip().split("/");

            if (currentDate[DAY_TOKEN].equals(day) && currentDate[MONTH_TOKEN].equals(month) && currentDate[YEAR_TOKEN].equals(year)) {
                todaysCO2.add(currentJourney.getCarbonEmitted());

            }
        }

    }

}
