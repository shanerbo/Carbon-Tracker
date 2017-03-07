package com.example.olive.carbon_tracker.UI;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        setupPieChart();
    }

    private void setupPieChart() {

        //populating a list of PiesEntries:
        List<PieEntry> pieEntries = new ArrayList<>();

        //TODO enter info into pie entries when singleton is made
        /*
        for (int i = 0; i < testCarbomEmissionData.length; i++) {

            pieEntries.add(new PieEntry(,);
        }*/

        PieDataSet dataSet = new PieDataSet(pieEntries, "carbon emission");
        dataSet.setColors(Color.rgb(20, 248, 24), Color.rgb(207, 248, 246));
        PieData data = new PieData(dataSet);

        //get the chart:
        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }

}
