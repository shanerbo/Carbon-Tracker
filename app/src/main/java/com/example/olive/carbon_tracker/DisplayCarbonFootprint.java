package com.example.olive.carbon_tracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

public class DisplayCarbonFootprint extends AppCompatActivity {

    private static final int NUM_ROWS = 3;   //rows will be dynamic
    private static final int NUM_COLS = 2;
    //ModelMain modelMain = ModelMain.getInstance();

    float testCarbomEmissionData[] = {10.4f, 55.5f, 45.5f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPieChart();
        populateCarbonFootprintTable();
        getWindow().getDecorView().setBackgroundColor(Color.rgb(49, 86, 28));
    }

    private void setupPieChart() {
        Button btn = (Button) findViewById(R.id.btn_PieChart);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DisplayCarbonFootprint.this, PieChart.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void populateCarbonFootprintTable() {
      //  List<String> make = modelMain.getVehicleMake(this);
        TableLayout table = (TableLayout) findViewById(R.id.tableCarbonFootprint);
        TableRow tableRow0 = new TableRow(this);
        makeColumnHeading(" Date of Trip ", tableRow0);
        makeColumnHeading(" Route Name ", tableRow0);
        makeColumnHeading(" Distance ", tableRow0);
        makeColumnHeading(" Vehicle Name ", tableRow0);
        makeColumnHeading(" Carbon Emitted ", tableRow0);
        table.addView(tableRow0);

        for (int i = 0; i < NUM_ROWS; i++) {
            TableRow tableRow = new TableRow(this);
            TextView dateOfTrip = new TextView(this);
            dateOfTrip.setText(" ");
            dateOfTrip.setTextColor(Color.WHITE);
            dateOfTrip.setGravity(Gravity.CENTER);
            tableRow.addView(dateOfTrip);


            TextView routeName = new TextView(this);
            routeName.setText(" ");
            routeName.setTextColor(Color.WHITE);
            routeName.setGravity(Gravity.CENTER);
            tableRow.addView(routeName);

            TextView distance = new TextView(this);
            distance.setText("");
            distance.setTextColor(Color.WHITE);
            distance.setGravity(Gravity.CENTER);
            tableRow.addView(distance);

            TextView vehicleName = new TextView(this);
         //   vehicleName.setText(make.get(i));
            vehicleName.setTextColor(Color.WHITE);
            vehicleName.setGravity(Gravity.CENTER);
            tableRow.addView(vehicleName);

            TextView carbonEmission = new TextView(this);
            carbonEmission.setText("" + testCarbomEmissionData[i]);
            carbonEmission.setTextColor(Color.WHITE);
            carbonEmission.setGravity(Gravity.CENTER);
            tableRow.addView(carbonEmission);

            table.addView(tableRow);
        }

    }


    public void makeColumnHeading(String heading, TableRow tableRow0) {
        TextView tableColumn = new TextView(this);
        tableColumn.setText(heading);
        tableColumn.setTextColor(Color.BLACK);
        tableRow0.addView(tableColumn);
    }
}
