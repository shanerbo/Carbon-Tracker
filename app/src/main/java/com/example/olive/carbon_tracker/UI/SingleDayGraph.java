package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.mode;
import static android.media.CamcorderProfile.get;
import static com.example.olive.carbon_tracker.R.id.chart;

public class SingleDayGraph extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    List<Double> todaysCO2 = new ArrayList<>();
    List<Double> carCO2 = new ArrayList<>();
    List<Double> busCO2 = new ArrayList<>();
    List<Double> skytrainCO2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day_graph);
        viewCurrentDate();
        setupPieChart();
        onRestart();
        setupCalendarButton();
    }

    private void setupPieChart() {

        getSingleDayCO2();

        List<PieEntry> pieEntries = new ArrayList<>();

        if (carCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(carCO2.get(0).floatValue(), "CAR"));
        }
        if (busCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(busCO2.get(0).floatValue(), "BUS"));
        }
        if (skytrainCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(skytrainCO2.get(0).floatValue(), "SKYTRAIN"));
        }


        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(Color.rgb(0, 128, 255), Color.rgb(96, 96, 96), Color.rgb(255, 153, 2255), Color.rgb(255, 128, 0), Color.rgb(255, 0, 0));
        PieData data = new PieData(dataSet);

        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.chart);
        chart.setUsePercentValues(false);

        Legend l = chart.getLegend();
        chart.getLegend().setEnabled(false);

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
        Button btn = (Button) findViewById(R.id.btnChangeDateForMonth);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SingleDayGraph.this, DisplayCalendar.class);
                startActivity(intent);
            }
        });
    }

    public void getSingleDayCO2() {
        todaysCO2.clear();
        List<Journey> journeyList = singleton.getUsersJourneys();
        skytrainCO2.add(0, 0.0);
        busCO2.add(0, 0.0);
        carCO2.add(0, 0.0);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        String userDate = day + "/" + month + "/" + year;

        for (int i = 0; i < journeyList.size(); i++) {
            Journey currentJourney = journeyList.get(i);
            String transportationMode = currentJourney.getVehicleName();

            double currentJourneyCO2 = currentJourney.getCarbonEmitted();
            String currentDate = currentJourney.getDateOfTrip();
            if (userDate.equals(currentDate)) {
                switch (transportationMode) {
                    case "Skytrain":
                        currentJourneyCO2 += skytrainCO2.remove(0);
                        skytrainCO2.add(0, currentJourneyCO2);
                        break;
                    case "Bus":
                        currentJourneyCO2 += busCO2.remove(0);
                        busCO2.add(0, currentJourneyCO2);
                        break;
                    default:
                        currentJourneyCO2 += carCO2.remove(0);
                        carCO2.add(0, currentJourneyCO2);
                        break;
                }

            }
        }

    }
}
