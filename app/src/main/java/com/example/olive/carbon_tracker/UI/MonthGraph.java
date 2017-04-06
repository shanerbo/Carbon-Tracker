package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.data.Entry;

import android.support.v7.app.AppCompatActivity;
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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.olive.carbon_tracker.R.id.chart;

/**
 * uses a stacked bar chart to display monthly carbon emission
 */

public class MonthGraph extends AppCompatActivity {
    public static final int DAY_TOKEN = 0;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;
    public static final int MONTH = 28;
    public static final int NATIONAL_AVERAGE = 50;
    public static final int PARIS_ACCORD = 30;
    int chosenYear;
    int chosenMonth;
    int chosenDay;
    Singleton singleton = Singleton.getInstance();
    List<Double> carCO2 = new ArrayList<>();
    List<Double> busCO2 = new ArrayList<>();
    List<Double> skytrainCO2 = new ArrayList<>();
    List<Double> utilityCO2 = new ArrayList<>();
    List<Journey> journeyList = singleton.getUsersJourneys();
    List<String> carNamesForMode = new ArrayList<>();
    List<Double> carNameSCO2ForMode = new ArrayList<>();
    private List<String> previousDates = new ArrayList<>();
    boolean isChartEmpty = true;
    List<String> routeNames = new ArrayList<>();
    List<Double> routeNameCO2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_month_graph);
        viewCurrentDate();
        setupCharts();
        onRestart();
        setupCalendarButton();
        setToolBar();
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

    private void setupCharts() {
        getMonthCO2();
        setupCombinedCharts();
        setupPieChart();
        setupModePieChart();
        setupRoutePieChart();
    }

    private void setupCombinedCharts() {
        CombinedChart chart = (CombinedChart) findViewById(R.id.chart2);

        CombinedData dataCombine = new CombinedData();

        dataCombine.setData(setupBarChart());
        dataCombine.setData(setupLineChart());

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return previousDates.get((int) value);
            }

        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        chart.getDescription().setEnabled(false);
        chart.setData(dataCombine);
        chart.animateX(1200);
        chart.animateY(1200);
        chart.invalidate();

    }

    private BarData setupBarChart() {

        ArrayList<BarEntry> transportationEntries = new ArrayList<BarEntry>();

        if (isChartEmpty) {
            Toast.makeText(getApplicationContext(), "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();

        } else {
            for (int i = 0; i < MONTH; i++) {
                transportationEntries.add(new BarEntry(
                        i, new float[]{busCO2.get(i).floatValue(),
                        carCO2.get(i).floatValue(),
                        skytrainCO2.get(i).floatValue(),
                        utilityCO2.get(i).floatValue()}));
            }

            BarDataSet set1;
            set1 = new BarDataSet(transportationEntries, "");
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Bus", "Car", "Sky Train", "Utility"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(0f);

            return data;

        }

        return null;
    }


    private LineData setupLineChart() {
        LineData lineData = new LineData();
        ArrayList<Entry> nationalAvgEntries = new ArrayList<Entry>();
        ArrayList<Entry> parisAccordEntries = new ArrayList<Entry>();
        LineDataSet set2 = new LineDataSet(parisAccordEntries, "Paris Accord");
        for (int i = 0; i < MONTH; i++) {
            nationalAvgEntries.add(new Entry(i + 0.5f, NATIONAL_AVERAGE));
            parisAccordEntries.add(new Entry(i + 0.5f, PARIS_ACCORD));
        }

        LineDataSet set = new LineDataSet(nationalAvgEntries, "National Avg");
        set.setColor(Color.CYAN);
        set.setLineWidth(2f);
        set.setCircleColor(Color.CYAN);
        set.setCircleRadius(2f);
        set.setFillColor(Color.CYAN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        set2.setColor(Color.MAGENTA);
        set2.setLineWidth(2f);
        set2.setCircleColor(Color.MAGENTA);
        set2.setCircleRadius(2f);
        set2.setFillColor(Color.MAGENTA);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(false);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        lineData.addDataSet(set);
        lineData.addDataSet(set2);

        return lineData;

    }

    private void setupPieChart() {

        List<PieEntry> pieEntries = new ArrayList<>();

        float totalCarCO2 = 0;
        float totalBusCO2 = 0;
        float totalSkyTrainCO2 = 0;
        float totalUtility = 0;

        for (int i = 0; i < MONTH; i++) {
            totalCarCO2 += carCO2.get(i).floatValue();
            totalBusCO2 += busCO2.get(i).floatValue();
            totalSkyTrainCO2 += skytrainCO2.get(i).floatValue();
            totalUtility += utilityCO2.get(i).floatValue();
        }
        if (totalBusCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalBusCO2, "BUS"));
        }
       if (totalCarCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalCarCO2, "CAR"));
       }

       if (totalSkyTrainCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalSkyTrainCO2, "SKYTRAIN"));
        }
        if (totalUtility != 0.0) {
            pieEntries.add(new PieEntry(totalUtility, "UTILITY"));
       }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(getColors());
        PieData data = new PieData(dataSet);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.piechart_month);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        chart.getDescription().setEnabled(false);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        chart.setUsePercentValues(false);
        chart.setData(data);
        chart.animateY(1000);
        chart.setEntryLabelTextSize(9f);
        chart.setRotationAngle(0);
        chart.invalidate();
    }
    private void setupModePieChart() {

        List<PieEntry> pieEntries = new ArrayList<>();

        float totalCarCO2 = 0;
        float totalBusCO2 = 0;
        float totalSkyTrainCO2 = 0;
        float totalUtility = 0;

        for (int i = 0; i < MONTH; i++) {
            //totalCarCO2 += carNameSCO2ForMode.get(i).floatValue();
            totalBusCO2 += busCO2.get(i).floatValue();
            totalSkyTrainCO2 += skytrainCO2.get(i).floatValue();
            totalUtility += utilityCO2.get(i).floatValue();
        }
        if (totalBusCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalBusCO2, "BUS"));
        }
        for (int i = 0; i < carNamesForMode.size(); i++) {
            pieEntries.add(new PieEntry(carNameSCO2ForMode.get(i).floatValue(), carNamesForMode.get(i)));
        }

        if (totalSkyTrainCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalSkyTrainCO2, "SKYTRAIN"));
        }
        if (totalUtility != 0.0) {
            pieEntries.add(new PieEntry(totalUtility, "UTILITY"));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(getColors());
        PieData data = new PieData(dataSet);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.mode_PieChart_MonthGraph);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.animateY(1000);
        chart.setEntryLabelTextSize(9f);
        chart.setRotationAngle(0);
        chart.invalidate();
    }
    private void setupRoutePieChart() {

        List<PieEntry> pieEntries = new ArrayList<>();



        for (int i = 0; i < MONTH; i++) {

          //  totalUtility += utilityCO2.get(i).floatValue();
        }

        for (int i = 0; i <routeNameCO2.size(); i++) {
            pieEntries.add(new PieEntry(routeNameCO2.get(i).floatValue(), routeNames.get(i)));
        }


     //   if (totalUtility != 0.0) {
       //     pieEntries.add(new PieEntry(totalUtility, "UTILITY"));
      //  }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(getColors());
        PieData data = new PieData(dataSet);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart)
                findViewById(R.id.route_PieChart_MonthGraph);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);

        chart.setData(data);
        chart.animateY(1000);
        chart.setEntryLabelTextSize(9f);
        chart.setRotationAngle(0);
        chart.invalidate();
    }
    public void onRestart() {
        super.onRestart();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentData_MonthGraph);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        currentDate.setText(day + "/" + month + "/" + year);
        singleton.setIsDateChanged(true);
        setupCharts();
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

    public void getMonthCO2() {

        busCO2.clear();
        skytrainCO2.clear();
        carCO2.clear();
        utilityCO2.clear();
        carNameSCO2ForMode.clear();
        carNamesForMode.clear();
        routeNames.clear();
        routeNameCO2.clear();
        getPrevious28Days();
        List<MonthlyUtilitiesData> utilitiesList = singleton.getBillList();
        for (int i = 0; i < MONTH; i++) {
            busCO2.add(i, 0.0);
            carCO2.add(i, 0.0);
            skytrainCO2.add(i, 0.0);
            utilityCO2.add(i, 0.0);
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
                    double co2ForRoute = currentJourneyCO2;
                    routeInfomation(currentJourney.getRouteName(), co2ForRoute);

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
                            double currentJourneyCO2Save = currentJourneyCO2;
                            currentJourneyCO2 += carCO2.remove(j);
                            carCO2.add(j, currentJourneyCO2);
                            modeInformation(transportationMode,currentJourneyCO2Save);
                            break;
                    }
                }
            }


        }


        boolean insideRange = false;
        long smallestDateDifference = 9999999;
        double mostRecentCO2 = 0;
        for (int i = 0; i < utilitiesList.size(); i++) {
            //for(int i = utilitiesList.size()-1; i>=0; i--){
            insideRange = false;

            isChartEmpty = false;
            MonthlyUtilitiesData currentUtility = utilitiesList.get(i);

            double currentUtilityIndCO2 = currentUtility.getIndCO2();
            Log.i("utility,co2: ", "" + currentUtilityIndCO2);
            String currentUtilityStartDate = currentUtility.getStartDate();
            Log.i("utility,sd: ", "" + currentUtilityStartDate);
            String currentUtilityEndDate = currentUtility.getEndDate();
            Log.i("utility,ed: ", "" + currentUtilityEndDate);

            //String firstDate = previousDates.get(0);
// if(getDateDifference(currentUtilityEndDate, firstDate)+1 < smallestDateDifference) {
// smallestDateDifference = getDateDifference(currentUtilityEndDate, firstDate) + 1; // } //smallestDateDifference = 2; //smallestDateDifference = getDateDifference(currentUtilityEndDate, firstDate)+1;
            for (int j = 0; j < previousDates.size(); j++) {
                String prevDate = previousDates.get(j);

                String[] prevDate2 = prevDate.split("/");
                String day = addZeroToDay(prevDate2[DAY_TOKEN]);
                String month = addZeroToDay(prevDate2[MONTH_TOKEN]);
                String year = prevDate2[YEAR_TOKEN];
                String prevDateNewFormat = year + "-" + month + "-" + day;

                Log.i("prevDate: ", "" + prevDate);
                if (getDateDifference(currentUtilityStartDate, prevDateNewFormat) >= 0 &&
                        getDateDifference(prevDateNewFormat, currentUtilityEndDate) >= 0) {
                    utilityCO2.remove(j);
                    //currentUtilityIndCO2 += utilityCO2.remove(j);
                    utilityCO2.add(j, currentUtilityIndCO2);
                    insideRange = true;
                } else {
                    long currentDateDifference = getDateDifference(currentUtilityEndDate, prevDateNewFormat);
                    if (currentDateDifference < smallestDateDifference && currentDateDifference > 0) {
                        mostRecentCO2 = currentUtilityIndCO2;
                        //smallestDateDifference = currentDateDifference;

                        if (!insideRange) {
                            //currentUtilityIndCO2 += utilityCO2.remove(j);
                            utilityCO2.remove(j);
                            utilityCO2.add(j, mostRecentCO2);
                        }
                    }

                }
            }
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
    private long getDateDifference(String StartDate, String EndDate) {

        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(StartDate);
            Date end = sdf.parse(EndDate);
            long dateDifference = end.getTime() - start.getTime();
            return dateDifference / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            Toast.makeText(MonthGraph.this, "ERROR: SingleDayGraph" +
                    " dateDifference calculation failed", Toast.LENGTH_LONG).show();
        }
        return -1;
    }

    private void getPrevious28Days() {
        //what the user has picked as a date
        chosenDay = Integer.parseInt(singleton.getUserDay());
        monthNumber(singleton.getUserMonth());
        chosenYear = Integer.parseInt(singleton.getUserYear());
        int currentDay = chosenDay;

        previousDates.clear();
        int subtract = 0; //to include the current day
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

        int stacksize = 4;

        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }

    private void setToolBar(){
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_month_graph);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            startActivity(new Intent(MonthGraph.this, AboutActivity.class));
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