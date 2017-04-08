package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * uses pie chart to display carbon emissions for s single day
 */
public class SingleDayGraph extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    List<Double> todaysCO2 = new ArrayList<>();
    List<Double> carCO2 = new ArrayList<>();
    List<Double> busCO2 = new ArrayList<>();
    List<Double> skytrainCO2 = new ArrayList<>();
    List<Double> utilityCO2 = new ArrayList<>();

    List<String> carNamesForMode = new ArrayList<>();
    List<Double> carNameSCO2ForMode = new ArrayList<>();

    List<String> routeNames = new ArrayList<>();
    List<Double> routeNameCO2 = new ArrayList<>();

    List<Double> electricityCO2 = new ArrayList<>();
    List<Double> naturalGasCO2 = new ArrayList<>();


    public static final int DAY_TOKEN = 0;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_single_day_graph);
        viewCurrentDate();
        setupPieChart();
        setupModePieChart();
        setupRoutePieChart();
        onRestart();
        setToolBar();
    }

    private void setupPieChart() {

        getSingleDayCO2();

        List<PieEntry> pieEntries = new ArrayList<>();
      if (busCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(busCO2.get(0).floatValue(), "BUS"));
       }
      if (carCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(carCO2.get(0).floatValue(), "CAR"));
      }

     if (skytrainCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(skytrainCO2.get(0).floatValue(), "SKYTRAIN"));
    }
       if (utilityCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(utilityCO2.get(0).floatValue(), "UTILITY"));
      }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");

        dataSet.setColors(getColors());
        PieData data = new PieData(dataSet);

        data.setValueTextSize(9f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.chart);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        chart.setData(data);
        chart.animateY(1000);
        chart.setEntryLabelTextSize(9f);
        chart.invalidate();
        chart.setRotationAngle(0);
    }


    private void setupModePieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        if (busCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(busCO2.get(0).floatValue(), "BUS"));
        }
        for (int i = 0; i < carNamesForMode.size(); i++) {
            pieEntries.add(new PieEntry(carNameSCO2ForMode.get(i).floatValue(), carNamesForMode.get(i)));
        }

        if (skytrainCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(skytrainCO2.get(0).floatValue(), "SKYTRAIN"));
        }

        if (electricityCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(electricityCO2.get(0).floatValue(), "ELECTRICITY"));
        }
        if (naturalGasCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(naturalGasCO2.get(0).floatValue(), "NATURAL GAS"));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(getColors());
        PieData data = new PieData(dataSet);

        data.setValueTextSize(9f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.mode_PieChart);
        chart.setUsePercentValues(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.animateY(1000);
        chart.setEntryLabelTextSize(9f);
        chart.invalidate();
        chart.setRotationAngle(0);
    }


    private void setupRoutePieChart() {



        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < routeNames.size(); i++) {
            pieEntries.add(new PieEntry(routeNameCO2.get(i).floatValue(), routeNames.get(i)));

        }

        if (electricityCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(electricityCO2.get(0).floatValue(), "ELECTRICITY"));
        }
        if (naturalGasCO2.get(0) != 0.0) {
            pieEntries.add(new PieEntry(naturalGasCO2.get(0).floatValue(), "NATURAL GAS"));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(getColors());
        PieData data = new PieData(dataSet);

        data.setValueTextSize(9f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.route_PieChart);
        chart.setUsePercentValues(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.animateY(1000);
        chart.setEntryLabelTextSize(9f);
        chart.invalidate();
        chart.setRotationAngle(0);

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
        setupModePieChart();
        setupRoutePieChart();
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


    public void getSingleDayCO2() {
        todaysCO2.clear();
        List<Journey> journeyList = singleton.getUsersJourneys();
        List<MonthlyUtilitiesData> utilitiesList = singleton.getBillList();
        utilityCO2.clear();
        carNameSCO2ForMode.clear();
        carNamesForMode.clear();
        routeNames.clear();
        routeNameCO2.clear();
        skytrainCO2.add(0, 0.0);
        busCO2.add(0, 0.0);
        carCO2.add(0, 0.0);
        utilityCO2.add(0, 0.0);
        electricityCO2.add(0,0.0);
        naturalGasCO2.add(0,0.0);
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
                routeInfomation(currentJourney.getRouteName(), currentJourneyCO2);
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
                        double currentJourneyCO2Save = currentJourneyCO2;
                        currentJourneyCO2 += carCO2.remove(0);
                        carCO2.add(0, currentJourneyCO2);
                        modeInformation(transportationMode, currentJourneyCO2Save);
                        break;
                }
            }
        }

        boolean insideRange = false;
        long smallestDateDifference = 99999999;
        double mostRecentCO2 = 0;
        double electricity = 0;
        double currentElecCO2 =0;
        double naturalGas =0;
        double currentGasco2 = 0;
        for (int i = 0; i < utilitiesList.size(); i++) {

            MonthlyUtilitiesData currentUtility = utilitiesList.get(i);

            Log.i("Elect"+currentUtility.getIndElecUsage(),"dfdf");
            double currentUtilityIndCO2 = currentUtility.getIndCO2();
            String currentUtilityStartDate = currentUtility.getStartDate();
            String currentUtilityEndDate = currentUtility.getEndDate();
             electricity = currentUtility.getIndElecUsage();
             currentElecCO2 = electricity * 0.009;
             naturalGas = currentUtility.getIndGasUsage();
             currentGasco2 = naturalGas *56.1;
            String[] date = userDate.split("/");
            String monthNumber = addZeroToDay("" + getMonthNumber(date[MONTH_TOKEN]));
            String currentDate = date[YEAR_TOKEN] + "-" + monthNumber + "-" + addZeroToDay(date[DAY_TOKEN]);

            if (getDateDifference(currentUtilityStartDate, currentDate) >= 0 &&
                    getDateDifference(currentDate, currentUtilityEndDate) >= 0) {

                currentUtilityIndCO2 += utilityCO2.remove(0);
                utilityCO2.add(currentUtilityIndCO2);
                currentElecCO2 += electricityCO2.remove(0);
                electricityCO2.add(currentElecCO2);

                currentGasco2 += naturalGasCO2.remove(0);
                naturalGasCO2.add(currentGasco2);
                insideRange = true;

            } else {
                long currentDateDifference = getDateDifference(currentUtilityEndDate, currentDate);
                if (currentDateDifference < smallestDateDifference && currentDateDifference > 0) {
                    mostRecentCO2 = currentUtilityIndCO2;
                    smallestDateDifference = currentDateDifference;
                }
            }

        }
        if (!insideRange) {
            mostRecentCO2 += utilityCO2.remove(0);
            utilityCO2.add(mostRecentCO2);
            currentElecCO2 += electricityCO2.remove(0);
            electricityCO2.add(currentElecCO2);

            currentGasco2 += naturalGasCO2.remove(0);
            naturalGasCO2.add(currentGasco2);
        }


    }


    private void routeInfomation(String currentJourneyRoute, double currentJourneyCO2) {

        double currentJourneyCO2Save = currentJourneyCO2;
        boolean foundRouteName = false;
        for (int j = 0; j < routeNames.size(); j++) {

            String currentRouteName = routeNames.get(j);

            if (currentRouteName.equals(currentJourneyRoute)) {
                foundRouteName = true;
                currentJourneyCO2Save += routeNameCO2.remove(j);
                routeNameCO2.add(j, currentJourneyCO2Save);
            }


        }

        if (!foundRouteName) {
            routeNames.add(currentJourneyRoute);
            routeNameCO2.add(currentJourneyCO2Save);
        }

    }





    private void modeInformation(String transportationMode, double currentJourneyCO2Save) {
        boolean foundMathchingCar = false;
        for (int j = 0; j < carNamesForMode.size(); j++) {

            String currentCar = carNamesForMode.get(j);

            if (currentCar.equals(transportationMode)) {
                foundMathchingCar = true;
                currentJourneyCO2Save += carNameSCO2ForMode.remove(j);
                carNameSCO2ForMode.add(j, currentJourneyCO2Save);
            }


        }

        if (!foundMathchingCar) {
            carNamesForMode.add(transportationMode);
            carNameSCO2ForMode.add(currentJourneyCO2Save);
        }
    }

    private long getDateDifference(String StartDate, String EndDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(StartDate);
            Date end = sdf.parse(EndDate);
            long dateDifference = end.getTime() - start.getTime();
            return dateDifference / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            Toast.makeText(SingleDayGraph.this, R.string.errorSingleDayCalculationFailed, Toast.LENGTH_LONG).show();
        }
        return -1;
    }


    private String addZeroToDay(String startDay) {
        if (startDay.equals("1")) {
            return "01";
        }
        if (startDay.equals("2")) {
            return "02";
        }
        if (startDay.equals("3")) {
            return "03";
        }
        if (startDay.equals("4")) {
            return "04";
        }
        if (startDay.equals("5")) {
            return "05";
        }
        if (startDay.equals("6")) {
            return "06";
        }
        if (startDay.equals("7")) {
            return "07";
        }
        if (startDay.equals("8")) {
            return "08";
        }
        if (startDay.equals("9")) {
            return "09";
        } else {
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
    private int[] getColors() {

        int stacksize = 4;

        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }

    private void setToolBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_single_day);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.tool_change_unit){
            if(singleton.checkCO2Unit() == 0) {
                singleton.humanRelatableUnit();
                Toast.makeText(getApplicationContext(), R.string.UnitChangedToGarbageUnit, Toast.LENGTH_SHORT).show();
            }
            else {
                singleton.originalUnit();
                Toast.makeText(getApplicationContext(), R.string.UnitChangedToKG, Toast.LENGTH_SHORT).show();
            }
            saveCO2UnitStatus(singleton.checkCO2Unit());
            return true;
        }
        if(id == R.id.tool_about){
            startActivity(new Intent(SingleDayGraph.this, AboutActivity.class));
            return true;
        }
        if(id == R.id.tool_date){
            startActivity(new Intent(SingleDayGraph.this, DisplayCalendar.class));
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
