package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.olive.carbon_tracker.R.id.chart;


public class MonthGraph extends AppCompatActivity {
    public static final int DAY_TOKEN = 0;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;
    public static final int MONTH = 28;
    int chosenYear;
    int chosenMonth;
    int chosenDay;
    Singleton singleton = Singleton.getInstance();
    List<Double> carCO2 = new ArrayList<>();
    List<Double> busCO2 = new ArrayList<>();
    List<Double> skytrainCO2 = new ArrayList<>();
    List<Journey> journeyList = singleton.getUsersJourneys();

    private List<String> previousDates = new ArrayList<>();
    boolean isChartEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_graph);
        viewCurrentDate();
        setupBarChart();
        onRestart();
        setupCalendarButton();
    }


    private void setupCalendarButton() {
        Button btn = (Button) findViewById(R.id.btnCalendar_MonthGraph);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MonthGraph.this, DisplayCalendar.class);
                startActivity(intent);
            }
        });
    }

    private void setupBarChart() {

        getMonthCO2();

        BarChart chart = (BarChart) findViewById(R.id.chart2);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return previousDates.get((int) value);
            }

        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        ArrayList<BarEntry> transportationEntries = new ArrayList<BarEntry>();

        if (isChartEmpty) {
            Toast.makeText(getApplicationContext(), "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();

        } else {
            for (int i = 0; i < MONTH; i++) {
                transportationEntries.add(new BarEntry(
                        i, new float[]{busCO2.get(i).floatValue(),
                        carCO2.get(i).floatValue(),
                        skytrainCO2.get(i).floatValue()}));

            }

            BarDataSet set1;
            set1 = new BarDataSet(transportationEntries, "");
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Bus", "Car", "Sky Train"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(0f);

            chart.setData(data);
            chart.setFitBars(true);
            chart.animateX(1200);
            chart.animateY(1200);
            chart.invalidate();
        }
    }


    public void onRestart() {
        super.onRestart();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentData_MonthGraph);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        currentDate.setText(day + "/" + month + "/" + year);
        singleton.setIsDateChanged(true);
        setupBarChart();
    }

    private void viewCurrentDate() {
        boolean isDateChanged = singleton.getIsDateChanged();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentData_MonthGraph);
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

    public void getMonthCO2() {

        busCO2.clear();
        skytrainCO2.clear();
        carCO2.clear();

        getPrevious28Days();

        for (int i = 0; i < MONTH; i++) {
            busCO2.add(i, 0.0);
            carCO2.add(i, 0.0);
            skytrainCO2.add(i, 0.0);
        }

        for (int i = 0; i < journeyList.size(); i++) {
            isChartEmpty = false;
            Journey currentJourney = journeyList.get(i);

            String transportationMode = currentJourney.getVehicleName();

            double currentJourneyCO2 = currentJourney.getCarbonEmitted();

            String[] date = currentJourney.getDateOfTrip().split("/");
            int monthNumber = getMonthNumber(date[MONTH_TOKEN]);
            String currentJourneyDate = date[DAY_TOKEN] + "/" + monthNumber + "/" + date[YEAR_TOKEN];

            for (int j = 0; j < previousDates.size(); j++) {
                if (previousDates.get(j).equals(currentJourneyDate)) {
                    switch (transportationMode) {
                        case "Skytrain":
                            currentJourneyCO2 += skytrainCO2.remove(j);
                            skytrainCO2.add(j, currentJourneyCO2);
                            break;
                        case "Bus":
                            currentJourneyCO2 += busCO2.remove(j);
                            busCO2.add(j, currentJourneyCO2);
                            break;
                        default:
                            currentJourneyCO2 += carCO2.remove(j);
                            carCO2.add(j, currentJourneyCO2);
                            break;
                    }
                }
            }
        }
    }


    private void getPrevious28Days() {
        //what the user has picked as a date
        chosenDay = Integer.parseInt(singleton.getUserDay());
        monthNumber(singleton.getUserMonth());
        chosenYear = Integer.parseInt(singleton.getUserYear());
        int currentDay = chosenDay;

        previousDates.clear();
        int subtract  = 0; //to include the current day
        //getting the dates for the past 28 days
        for (int i = 0; i < MONTH; i++) {
            currentDay = currentDay - subtract;
            if (currentDay > 0) {
                previousDates.add(currentDay + "/" + chosenMonth + "/" + chosenYear);
            } else {
                getPreviousMonthDetails();
                String date = previousDates.get(i);
                String[] lastMonth = date.split("/");
                currentDay = Integer.parseInt(lastMonth[0]);
            }
            subtract = 1;
        }
    }

    private void getPreviousMonthDetails() {
        GregorianCalendar cal = new GregorianCalendar();
        if (chosenMonth == 1) {
            chosenYear--;
            chosenMonth = 12;
            chosenDay = 31;
            previousDates.add(chosenDay + "/" + chosenMonth + "/" + chosenYear);
        } else {
            int month = chosenMonth - 2;
            cal.set(chosenYear, month, 1);
            chosenDay = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            previousDates.add(chosenDay + "/" + (--chosenMonth) + "/" + chosenYear);
        }
    }


    public void monthNumber(String month) {
        switch (month) {
            case "January":
                chosenMonth = 1;
                break;
            case "February":
                chosenMonth = 2;
                break;
            case "March":
                chosenMonth = 3;
                break;
            case "April":
                chosenMonth = 4;
                break;
            case "May":
                chosenMonth = 5;
                break;
            case "June":
                chosenMonth = 6;
                break;
            case "July":
                chosenMonth = 7;
                break;
            case "August":
                chosenMonth = 8;
                break;
            case "September":
                chosenMonth = 9;
                break;
            case "October":
                chosenMonth = 10;
                break;
            case "November":
                chosenMonth = 11;
                break;
            case "December":
                chosenMonth = 12;
                break;
        }

    }

    public int getMonthNumber(String month) {

        switch (month) {
            case "January":
                return 1;

            case "February":
                return 2;

            case "March":
                return 3;

            case "April":
                return 4;

            case "May":
                return 5;

            case "June":
                return 6;

            case "July":
                return 7;

            case "August":
                return 8;

            case "September":
                return 9;

            case "October":
                return 10;

            case "November":
                return 11;

            case "December":
                return 12;

        }
        return -1;
    }

    private int[] getColors() {

        int stacksize = 3;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }
}