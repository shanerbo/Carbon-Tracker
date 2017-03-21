package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YearGraph extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();
    public static final int MONTHS = 12;
    int chosenYear;
    int chosenMonth;
    int chosenDay;
    public static final int MONTH_TOKEN = 1;
    public static final int YEAR_TOKEN = 2;

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
        setupLineChart();
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
        currentDate.setText(day + "/" + month + "/" + year);
        singleton.setIsDateChanged(true);
        setupLineChart();
    }

    private void setupLineChart() {

        getYearCO2();

        if (!isChartEmpty) {

            LineChart lineChart = (LineChart) findViewById(R.id.chart3);

            ArrayList<Entry> busEntires = new ArrayList<>();
            ArrayList<Entry> carEntires = new ArrayList<>();
            ArrayList<Entry> skytrainEntires = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                busEntires.add(new Entry(i, busCO2.get(i).floatValue()));
                carEntires.add(new Entry(i, carCO2.get(i).floatValue()));
                skytrainEntires.add(new Entry(i, skytrainCO2.get(i).floatValue()));
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

            lineDataSets.add(busDataSet);
            lineDataSets.add(carDataSet);
            lineDataSets.add(skytrainDataSet);

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

        initMonths();

        for (int i = 0; i < MONTHS; i++) {
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
}
