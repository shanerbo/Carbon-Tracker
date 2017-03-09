package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setButton(R.id.btnCreateJourney);
        setButton(R.id.btnCurrentFootprint);
        setupPieChart();
    }
    private void setupPieChart(){
        List<Journey> journeyList = singleton.getUsersJourneys();
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i =0;i<journeyList.size();i++) {
            if (!journeyList.isEmpty()) {
                Journey currentJourney = journeyList.get(i);
                int co2  = (int) currentJourney.getCarbonEmitted();
                pieEntries.add(new PieEntry(co2, currentJourney.getRouteName()));

            }
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"carbon emission");
        dataSet.setColors(Color.rgb(0,128,255),Color.rgb(128,128,128),Color.rgb(255, 153, 2255),Color.rgb(255, 128, 0),Color.rgb(255, 0, 0));
        PieData data = new PieData(dataSet);
        com.github.mikephil.charting.charts.PieChart chart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setButton(final int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent();
                switch (id) {
                    case R.id.btnCreateJourney:
                        showActivity = DisplayCarList.makeIntent(MainMenu.this);
                        break;
                    case R.id.btnCurrentFootprint:
                        showActivity = new Intent(MainMenu.this,DisplayCarbonFootprint.class);
                        break;
                }
                startActivity(showActivity);
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenu.class);
    }
}
