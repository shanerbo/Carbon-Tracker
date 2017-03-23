package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.MonthlyUtilitiesData;
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
    List<Double> utilityCO2 = new ArrayList<>();
    public static final int DAY_TOKEN = 0;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;
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
        if (utilityCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(utilityCO2.get(0).floatValue(), "UTILITY"));
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
        List<MonthlyUtilitiesData> utilitiesList = singleton.getBillList();
        utilityCO2.clear();
        skytrainCO2.add(0, 0.0);
        busCO2.add(0, 0.0);
        carCO2.add(0, 0.0);
        utilityCO2.add(0,0.0);
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

        boolean insideRange = false;
        long smallestDateDifference = 99999999;
        double mostRecentCO2 = 0;
        for (int i = 0; i < utilitiesList.size(); i++) {

            MonthlyUtilitiesData currentUtility= utilitiesList.get(i);
            double currentUtilityIndCO2 =  currentUtility.getIndCO2();
            String currentUtilityStartDate = currentUtility.getStartDate();
            String currentUtilityEndDate = currentUtility.getEndDate();

            String[] date = userDate.split("/");
            String monthNumber = addZeroToDay(""+getMonthNumber(date[MONTH_TOKEN]));
            String currentDate =  date[YEAR_TOKEN]+ "-" + monthNumber + "-" + addZeroToDay(date[DAY_TOKEN]);

            if(getDateDifference(currentUtilityStartDate, currentDate)>=0 &&
                    getDateDifference(currentDate, currentUtilityEndDate)>=0){

                currentUtilityIndCO2 += utilityCO2.remove(0);
                utilityCO2.add(currentUtilityIndCO2);
                insideRange = true;

            }
            else {
                long currentDateDifference = getDateDifference(currentUtilityEndDate, currentDate);
                if (currentDateDifference < smallestDateDifference && currentDateDifference > 0) {
                    mostRecentCO2 = currentUtilityIndCO2;
                    smallestDateDifference = currentDateDifference;
                }
            }

        }
        if(!insideRange){
            mostRecentCO2 += utilityCO2.remove(0);
            utilityCO2.add(mostRecentCO2);
        }


    }

    private long getDateDifference(String StartDate, String EndDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(StartDate);
            Date end = sdf.parse(EndDate);
            long dateDifference = end.getTime() - start.getTime();
            return dateDifference / 1000 / 60 / 60 / 24;
        }
        catch(Exception  e){
            Toast.makeText(SingleDayGraph.this, "ERROR: SingleDayGraph" +
                    " dateDifference calculation failed", Toast.LENGTH_LONG).show();
        }
        return -1;
    }


    private String addZeroToDay(String startDay) {
        if(startDay.equals("1")){
            return "01";
        }        if(startDay.equals("2")){
            return "02";
        }        if(startDay.equals("3")){
            return "03";
        }        if(startDay.equals("4")){
            return "04";
        }        if(startDay.equals("5")){
            return "05";
        }        if(startDay.equals("6")){
            return "06";
        }        if(startDay.equals("7")){
            return "07";
        }        if(startDay.equals("8")){
            return "08";
        }        if(startDay.equals("9")){
            return "09";
        }else{
            return startDay;
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
}
