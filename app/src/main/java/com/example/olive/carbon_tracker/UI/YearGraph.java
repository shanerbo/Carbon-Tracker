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
import com.example.olive.carbon_tracker.Model.MonthlyUtilitiesData;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v7.widget.AppCompatDrawableManager.get;
import static com.example.olive.carbon_tracker.UI.MonthGraph.NATIONAL_AVERAGE;
import static com.example.olive.carbon_tracker.UI.MonthGraph.PARIS_ACCORD;

/**
 * uses a line graph to display average monthly carbon emission for one year
 */
public class YearGraph extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();
    public static final int MONTHS = 12;
    public static final int NATIONAL_AVERAGE = 50;
    public static final int PARIS_ACCORD = 30;
    int chosenYear;
    int chosenMonth;
    int chosenDay;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;
    List<Double> utilityCO2 = new ArrayList<>();
    List<Double> carCO2 = new ArrayList<>();
    List<Double> busCO2 = new ArrayList<>();
    List<Double> skytrainCO2 = new ArrayList<>();
    List<Journey> journeyList = singleton.getUsersJourneys();
    private List<String> previousDates = new ArrayList<>();
    boolean isChartEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_graph);
        viewCurrentDate();
   setupCharts();
        setupCalendarButton();
        onRestart();

    }


    private void setupCalendarButton() {
        Button btn = (Button) findViewById(R.id.btnChangeDate_YearGraph);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(YearGraph.this, DisplayCalendar.class);
                startActivity(intent);
            }
        });
    }

    public void onRestart() {
        super.onRestart();
        TextView currentDate = (TextView) findViewById(R.id.txtcurrentDate_YearGraph);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        currentDate.setText(month + "/" + year);
        singleton.setIsDateChanged(true);
        setupLineChart();
    }





    private void setupCharts() {
        getYearCO2();
       // setupCombinedCharts();
        setupLineChart();
        setupPieChart();
    }


    private void setupPieChart() {

        List<PieEntry> pieEntries = new ArrayList<>();

        float totalCarCO2 = 0;
        float totalBusCO2 = 0;
        float totalSkyTrainCO2 = 0;
        float totalUtility = 0;

        for (int i = 0; i < MONTHS; i++) {
            totalCarCO2 += carCO2.get(i).floatValue();
            totalBusCO2 += busCO2.get(i).floatValue();
            totalSkyTrainCO2 += skytrainCO2.get(i).floatValue();
            totalUtility += utilityCO2.get(i).floatValue();
        }
        if (totalBusCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalBusCO2, ""));
        }
        if (totalCarCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalCarCO2, ""));
        }

        if (totalSkyTrainCO2 != 0.0) {
            pieEntries.add(new PieEntry(totalSkyTrainCO2, ""));
        }
        if (totalUtility != 0.0) {
            pieEntries.add(new PieEntry(totalUtility, ""));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(getColors());
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(2);
        PieData data = new PieData(dataSet);

        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.piechart_year);
        chart.setUsePercentValues(false);
//chart.setCenterTextColor(Color.BLACK);
        chart.setEntryLabelColor(Color.BLUE);
        chart.setCenterTextOffset(0, -20);
        Legend l = chart.getLegend();
        chart.getLegend().setEnabled(false);
        chart.setRotationAngle(0);
        //chart.setCenterText(generateCenterSpannableText());
        //chart.setCenterTextColor(Color.BLACK);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setupLineChart() {


        if (!isChartEmpty) {


            ArrayList<Entry> nationalAvgEntries = new ArrayList<Entry>();
            ArrayList<Entry> parisAccordEntries = new ArrayList<Entry>();
            LineDataSet set2 = new LineDataSet(parisAccordEntries, "Paris Accord");
            for (int i = -1; i < MONTHS; i++) {
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




















            LineChart lineChart = (LineChart) findViewById(R.id.chart3);

            ArrayList<Entry> busEntires = new ArrayList<>();
            ArrayList<Entry> carEntires = new ArrayList<>();
            ArrayList<Entry> skytrainEntires = new ArrayList<>();
            ArrayList<Entry> utilityEntires = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                busEntires.add(new Entry(i, busCO2.get(i).floatValue()));
                carEntires.add(new Entry(i, carCO2.get(i).floatValue()));
                skytrainEntires.add(new Entry(i, skytrainCO2.get(i).floatValue()));
                utilityEntires.add(new Entry(i, utilityCO2.get(i).floatValue()));
            }

            IAxisValueFormatter formatter = new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return previousDates.get((int) value);
                }

            };

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setTextSize(7);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(formatter);

            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.setTextColor(ColorTemplate.getHoloBlue());
            leftAxis.setAxisMinimum(0.1f);

            YAxis rightAxis = lineChart.getAxisRight();
            rightAxis.setTextColor(ColorTemplate.getHoloBlue());
            rightAxis.setAxisMinimum(0.1f);

            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            LineDataSet busDataSet = new LineDataSet(busEntires, "Bus");
            LineDataSet carDataSet = new LineDataSet(carEntires, "Car");
            LineDataSet skytrainDataSet = new LineDataSet(skytrainEntires, "Sky Train");
            LineDataSet utilityDataSet = new LineDataSet(utilityEntires, "Utilities");

            busDataSet.setLineWidth(0f);
            busDataSet.setCircleRadius(6f);
            busDataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);
            busDataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);

            carDataSet.setLineWidth(0f);
            carDataSet.setCircleRadius(6f);
            carDataSet.setColor(ColorTemplate.MATERIAL_COLORS[1]);
            carDataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[1]);

            skytrainDataSet.setLineWidth(0f);
            skytrainDataSet.setCircleRadius(6f);
            skytrainDataSet.setColor(ColorTemplate.MATERIAL_COLORS[2]);
            skytrainDataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[2]);


            utilityDataSet.setLineWidth(0f);
            utilityDataSet.setCircleRadius(6f);
            utilityDataSet.setColor(ColorTemplate.MATERIAL_COLORS[3]);
            utilityDataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[3]);

            lineDataSets.add(set);
            lineDataSets.add(set2);
            lineDataSets.add(busDataSet);
            lineDataSets.add(carDataSet);
            lineDataSets.add(skytrainDataSet);
            lineDataSets.add(utilityDataSet);


            lineChart.animateX(800);
            lineChart.animateY(800);
            lineChart.setData(new LineData(lineDataSets));
            lineChart.animate();
            lineChart.invalidate();
        }
    }


    private void getYearCO2() {

        busCO2.clear();
        skytrainCO2.clear();
        carCO2.clear();
        utilityCO2.clear();
        initMonths();

        for (int i = 0; i < MONTHS; i++) {
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
            String monthConvert = getStringMonth(date[MONTH_TOKEN]);
            String currentJourneyDate = monthConvert + "/" + date[YEAR_TOKEN];


            for (int j = 0; j < previousDates.size(); j++) {
                if (currentJourneyDate.equals(previousDates.get(j))) {
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

        List<MonthlyUtilitiesData> utilitiesList = singleton.getBillList();

        boolean insideRange;
        long smallestDateDifference = 9999999;
        double mostRecentCO2 = 0;
        for (int i=0; i < utilitiesList.size(); i++) {
            insideRange = false;

            isChartEmpty = false;
            MonthlyUtilitiesData currentUtility = utilitiesList.get(i);

            double currentUtilityIndCO2 = currentUtility.getIndCO2();
            //Log.i("utility,co2: " ,"" +currentUtilityIndCO2);
            String currentUtilityStartDate = currentUtility.getStartDate();
            Log.i("utility,sd: " ,"" +currentUtilityStartDate);
            String currentUtilityEndDate = currentUtility.getEndDate();
            Log.i("utility,ed: " ,"" +currentUtilityEndDate);

            for (int j = 0; j <previousDates.size(); j++) {
                String prevDate = previousDates.get(j);

                String[] prevDate2 = prevDate.split("/");

                String month = addZeroToDay("" + AbbrMonthNumber(prevDate2[0]));
                String year = prevDate2[1];
                String prevDateNewFormat = year + "-" + month + "-" + "15";





                if (getDateDifference(currentUtilityStartDate, prevDateNewFormat) >= 0 &&
                        getDateDifference(prevDateNewFormat, currentUtilityEndDate) >= 0) {
                    utilityCO2.remove(j);
                    utilityCO2.add(j, currentUtilityIndCO2);
                    insideRange = true;
                } else {
                    long currentDateDifference = getDateDifference(currentUtilityEndDate, prevDateNewFormat);
                    if (currentDateDifference < smallestDateDifference && currentDateDifference > 0) {
                        mostRecentCO2 = currentUtilityIndCO2;

                        if (!insideRange) {
                            utilityCO2.remove(j);
                            utilityCO2.add(j, mostRecentCO2);
                        }
                    }

                }
            }
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

    private long getDateDifference(String StartDate, String EndDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(StartDate);
            Date end = sdf.parse(EndDate);
            long dateDifference = end.getTime() - start.getTime();
            return dateDifference / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            Toast.makeText(YearGraph.this, "ERROR: YearGraph" +
                    " dateDifference calculation failed", Toast.LENGTH_LONG).show();
        }
        return -1;
    }
    private void initMonths() {

        chosenDay = Integer.parseInt(singleton.getUserDay());
        monthNumber(singleton.getUserMonth());
        chosenYear = Integer.parseInt(singleton.getUserYear());
        previousDates.clear();

        int month = chosenMonth;
        int saveYear = chosenYear;

        for (int i = 0; i < MONTHS; i++) {
            switch (month) {
                case 1:
                    previousDates.add("Jan/" + chosenYear);
                    break;
                case 2:
                    previousDates.add("Feb/" + chosenYear);
                    break;
                case 3:
                    previousDates.add("Mar/" + chosenYear);
                    break;
                case 4:
                    previousDates.add("Apr/" + chosenYear);
                    break;
                case 5:
                    previousDates.add("May/" + chosenYear);
                    break;
                case 6:
                    previousDates.add("Jun/" + chosenYear);
                    break;
                case 7:
                    previousDates.add("Jul/" + chosenYear);
                    break;
                case 8:
                    previousDates.add("Aug/" + chosenYear);
                    break;
                case 9:
                    previousDates.add("Sep/" + chosenYear);
                    break;
                case 10:
                    previousDates.add("Oct/" + chosenYear);
                    break;
                case 11:
                    previousDates.add("Nov/" + chosenYear);
                    break;
                case 12:
                    previousDates.add("Dec/" + chosenYear);
                    break;
            }

            --month;

            if (month == 0) {
                month = 12;
                --chosenYear;
            }
        }
        chosenYear = saveYear;
    }

    public String getStringMonth(String month) {
        switch (month) {
            case "January":
                return "Jan";
            case "February":
                return "Feb";
            case "March":
                return "Mar";
            case "April":
                return "Apr";
            case "May":
                return "May";
            case "June":
                return "Jun";
            case "July":
                return "Jul";
            case "August":
                return "Aug";
            case "September":
                return "Sep";
            case "October":
                return "Oct";
            case "November":
                return "Nov";
            case "December":
                return "Dec";
        }

        return null;
    }


    private void viewCurrentDate() {

        boolean isDateChanged = singleton.getIsDateChanged();
        TextView currentDate = (TextView) findViewById(R.id.txtcurrentDate_YearGraph);
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
            currentDate.setText(month + "/" + year);

        } else {
            super.onRestart();
            String day = singleton.getUserDay();
            String month = singleton.getUserMonth();
            String year = singleton.getUserYear();
            currentDate.setText(day + "/" + month + "/" + year);
            singleton.setIsDateChanged(false);
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
    public int AbbrMonthNumber(String month) {

        switch (month) {
            case "Jan":
                return 1;

            case "Feb":
                return 2;

            case "Mar":
                 return 3;

            case "Apr":
                return 4;

            case "May":
                return 5;

            case "Jun":
                return 6;

            case "Jul":
                return 7;

            case "Aug":
                 return 8;

            case "Sep":
                return 9;

            case "Oct":
                return 10;

            case "Nov":
                return 11;

            case "Dec":
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


}
