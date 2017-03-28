package com.example.olive.carbon_tracker.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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

import com.example.olive.carbon_tracker.Model.AlarmReceiver;
import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.MonthlyUtilitiesData;
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

/**
 * uses a stacked bar chart to display monthly carbon emission
 */

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
    List<Double> utilityCO2 = new ArrayList<>();
    List<Journey> journeyList = singleton.getUsersJourneys();

    private List<String> previousDates = new ArrayList<>();
    boolean isChartEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAlarm();
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
                        skytrainCO2.get(i).floatValue(),
                        utilityCO2.get(i).floatValue()}));
            }

            BarDataSet set1;
            set1 = new BarDataSet(transportationEntries, "");
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Bus", "Car", "Sky Train","Utility"});

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


        boolean insideRange = false;
        long smallestDateDifference = 9999999;
        double mostRecentCO2 = 0;
        for (int i=0; i < utilitiesList.size(); i++) {
            //for(int i = utilitiesList.size()-1; i>=0; i--){
            insideRange = false;

            isChartEmpty = false;
            MonthlyUtilitiesData currentUtility = utilitiesList.get(i);

            double currentUtilityIndCO2 = currentUtility.getIndCO2();
            Log.i("utility,co2: " ,"" +currentUtilityIndCO2);
            String currentUtilityStartDate = currentUtility.getStartDate();
            Log.i("utility,sd: " ,"" +currentUtilityStartDate);
            String currentUtilityEndDate = currentUtility.getEndDate();
            Log.i("utility,ed: " ,"" +currentUtilityEndDate);

            //String firstDate = previousDates.get(0);
// if(getDateDifference(currentUtilityEndDate, firstDate)+1 < smallestDateDifference) {
// smallestDateDifference = getDateDifference(currentUtilityEndDate, firstDate) + 1; // } //smallestDateDifference = 2; //smallestDateDifference = getDateDifference(currentUtilityEndDate, firstDate)+1;
            for (int j = 0; j <previousDates.size(); j++) {
                String prevDate = previousDates.get(j);

                String[] prevDate2 = prevDate.split("/");
                String day = addZeroToDay(prevDate2[DAY_TOKEN]);
                String month =   addZeroToDay(prevDate2[MONTH_TOKEN]);
                String year = prevDate2[YEAR_TOKEN];
                String prevDateNewFormat = year + "-" + month + "-" + day;




                Log.i("prevDate: " , ""+ prevDate);
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

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
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