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
import java.util.List;

import static com.example.olive.carbon_tracker.R.id.chart;


public class MonthGraph extends AppCompatActivity {
    public static final int DAY_TOKEN = 0;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;
    public static final int MONTH = 28;
    Singleton singleton = Singleton.getInstance();
    List<Double> monthCO2 = new ArrayList<>(); //TODO NEED TO BREAK THIS UP
    List<Double> carCO2 = new ArrayList<>();
    List<Double> busCO2 = new ArrayList<>();
    List<Double> skytrainCO2 = new ArrayList<>();
    List<Journey> journeyList = singleton.getUsersJourneys();
    List<String> previousDates = new ArrayList<>();
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

    //TODO make sure to make date shorter, like 01/01/17
    private void setupBarChart() {

        getMonthCO2();

        BarChart chart = (BarChart) findViewById(R.id.chart2);


        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return previousDates.get((int) value);
            }

            // we don't draw numbers, so no decimal digits needed
            // @Override
            public int getDecimalDigits() {
                return 0;
            }
        };

       XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        if (isChartEmpty) {
            Toast.makeText(getApplicationContext(),"NO DATA AVAILABLE",Toast.LENGTH_SHORT).show();

        } else {
            for (int i = 0; i < 28; i++) {
                yVals1.add(new BarEntry(
                        i, new float[]{busCO2.get(i).floatValue(), carCO2.get(i).floatValue(), skytrainCO2.get(i).floatValue()}));

            }


        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        //   set1.setDrawIcons(false);
        set1.setColors(getColors());
        set1.setStackLabels(new String[]{"bus", "car", "sktrain"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        // data.setValueFormatter(formatter);
            data.setValueTextSize(0f);


        chart.setData(data);
        chart.setFitBars(true);
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


    //TODO if we want i can add the route name as well for the legened
    public void getMonthCO2() {
        monthCO2.clear();
        previousDates.clear();
        busCO2.clear();
        skytrainCO2.clear();
        carCO2.clear();
        getPrevious28Days();
        //what the user has picked as a date
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        for (int i = 0; i < MONTH; i++) {
            busCO2.add(i, 0.0);
            carCO2.add(i, 0.0);
            skytrainCO2.add(i, 0.0);
        }


        int monthCounter = 28;
        //need to see if the dates match the journey dates
        for (int i = 0; i < journeyList.size(); i++) {
            isChartEmpty = false;
            Journey currentJourney = journeyList.get(i);
            Log.i("    ", " ");
            String[] monthJourney = currentJourney.getDateOfTrip().split("/");
            String transportationMode = currentJourney.getVehicleName();
            Log.i("current journ mode = " + transportationMode, " ");
            double currentJourneyCO2 = currentJourney.getCarbonEmitted();
            Log.i("current journ co2 = " + currentJourneyCO2, " ");
            String currentJourneyDate = currentJourney.getDateOfTrip();
            Log.i("current journ date = " + currentJourneyDate, " ");
            Log.i("checking prev dates", " ");
            for (int j = 0; j < previousDates.size(); j++) {
                Log.i("   " + previousDates.get(j) + "==", currentJourneyDate);
                if (previousDates.get(j).equals(currentJourney.getDateOfTrip())) {
                    switch (transportationMode) {
                        //TODO initialize the first 28 indicies to 0 of the arrays
                        case "Skytrain":
                            currentJourneyCO2 += skytrainCO2.remove(j);
                            skytrainCO2.add(j, currentJourneyCO2);
                            break;
                        case "Bus":
                            currentJourneyCO2 += busCO2.remove(j);
                            Log.i("FOUND MATCHING BUS ", " ");
                            Log.i("has co2: " + currentJourneyCO2, " ");
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
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        int currentDay = Integer.parseInt(day);
        int subtract = 0;
        previousDates.clear();
        //getting the dates for the past 28 days
        for (int i = 0; i < MONTH; i++) {
            currentDay = currentDay - subtract;
            if (currentDay > 0) {
                previousDates.add(currentDay + "/" + month + "/" + year);
            } else {
                getPreviousMonthDetails();
                String date = previousDates.get(i);
                String[] lastMonth = date.split("/");
                currentDay = Integer.parseInt(lastMonth[0]);
                month = lastMonth[1];
                year = lastMonth[2];
            }
            subtract = 1;
        }
        //shortenDate();
    }

    private void getPreviousMonthDetails() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDateOfPreviousMonth = cal.getTime();
        String[] lastMonth = (lastDateOfPreviousMonth.toString()).split(" ");
        int day = Integer.parseInt(lastMonth[2]);
        String month = getStringMonth(lastMonth[1]);
        String year = lastMonth[5];
        previousDates.add(day + "/" + month + "/" + year);
    }

    public void addCarbonToDate() {

    }


    public String getStringMonth(String month) {
        switch (month) {
            case "Jan":
                return "January ";
            case "Feb":
                return "February";
            case "Mar":
                return "March";
            case "Apr":
                return "April";
            case "May":
                return "May";
            case "Jun":
                return "June";
            case "Jul":
                return "July";
            case "Aug":
                return "August";
            case "Sept":
                return "September";
            case "Oct":
                return "October";
            case "Nov":
                return "November";
            case "Dec":
                return "December";
        }

        return null;
    }

    public void shortenDate() {
        Log.i("   ", " ");
        Log.i("SHORTEN DATE ", " ");
        for (int i = 0; i < previousDates.size(); i++) {
            String[] currentDate = previousDates.remove(i).split("/");
            String day = currentDate[DAY_TOKEN];
            String month = currentDate[MONTH_TOKEN];
            //TODO make year shorter
            String year = currentDate[YEAR_TOKEN];
            switch (month) {
                case "January":
                    month = "1";
                    break;
                case "February":
                    month = "2";
                    break;
                case "March":
                    month = "3";
                    break;
                case "April":
                    month = "4";
                    break;
                case "May":
                    month = "5";
                    break;
                case "June":
                    month = "6";
                    break;
                case "July":
                    month = "7";
                    break;
                case "August":
                    month = "8";
                    break;
                case "September":
                    month = "9";
                    break;
                case "October":
                    month = "10";
                    break;
                case "November":
                    month = "11";
                    break;
                case "December":
                    month = "12";
                    break;
            }
            previousDates.add(i, day + "/" + month + "/" + year);
        }
        for (String date : previousDates) {
            Log.i("date----" + date, "");
            //   Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
        }
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
/*


BarData data = new BarData(dataSets);
        data.setValueFormatter(formatter);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);

        List<BarEntry> busEntries = new ArrayList<>();
        for (int i = 0; i < busCO2.size(); i++) {
            int co2 = busCO2.get(i).intValue();
            busEntries.add(new BarEntry(i, co2));
        }
        BarDataSet busSet = new BarDataSet(busEntries, "BUS");
        busSet.setColor(Color.BLUE);
        busSet.setValueTextColor(Color.BLUE);
        busSet.setBarShadowColor(Color.BLUE);
        busSet.setBarBorderColor(Color.BLUE);
        busSet.setHighLightColor(Color.BLUE);


        List<BarEntry> skytrainEntries = new ArrayList<>();
        for (int i = 0; i < skytrainCO2.size(); i++) {
            int co2 = skytrainCO2.get(i).intValue();
            skytrainEntries.add(new BarEntry(i, co2));
        }
        BarDataSet skytrainSet = new BarDataSet(skytrainEntries, "SKYTRAIN");
        skytrainSet.setColor(Color.RED);
        skytrainSet.setValueTextColor(Color.RED);

        List<BarEntry> carEntries = new ArrayList<>();
        for (int i = 0; i < carCO2.size(); i++) {
            int co2 = carCO2.get(i).intValue();
            carEntries.add(new BarEntry(i, co2));
        }
        BarDataSet set1;
      //  BarDataSet carSet = new BarDataSet(carEntries, "CAR");

        set1 = new BarDataSet(yVals1, "Statistics Vienna 2014");
       // set1.setDrawIcons(false);
        set1.setColors(getColors());
        set1.setStackLabels(new String[]{"bus", "skytrain", "car"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data)





        carSet.setColor(Color.GREEN);
        carSet.setValueTextColor(Color.GREEN);
        // dataSet.setColors(Color.rgb(0, 128, 255), Color.rgb(96, 96, 96), Color.rgb(255, 153, 2255), Color.rgb(255, 128, 0), Color.rgb(255, 0, 0));
        //get the chart:
        // data.setBarWidth(0.9f);



// set a custom value formatter
        BarData data = new BarData(busSet,carSet,skytrainSet);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // ref
 */