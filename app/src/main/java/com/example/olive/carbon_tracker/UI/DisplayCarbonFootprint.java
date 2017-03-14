package com.example.olive.carbon_tracker.UI;

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

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Singleton;

import java.util.List;


public class DisplayCarbonFootprint extends AppCompatActivity {


    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carbon_footprint);
        setupPieChart();
        populateCarbonFootprintTable();
        getWindow().getDecorView().setBackgroundColor(Color.rgb(49, 86, 28));

    }

    private void setupPieChart() {
        Button btn = (Button) findViewById(R.id.btn_PieChart);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DisplayCarbonFootprint.this,GraphPicker.class);
                startActivity(intent);
            }
        });
    }

    public void populateCarbonFootprintTable() {
       List<Journey> journeyList = singleton.getUsersJourneys();
        int NUM_ROWS = journeyList.size();   //rows will be dynamic


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
            if (!journeyList.isEmpty()) {
                Journey currentJourney = journeyList.get(i);
                enterRowInfo(dateOfTrip, tableRow, currentJourney.getDateOfTrip());

                TextView routeName = new TextView(this);
                enterRowInfo(routeName, tableRow, currentJourney.getRouteName());

                TextView distance = new TextView(this);
                enterRowInfo(distance, tableRow," " +currentJourney.getTotalDistance());

                TextView vehicleName = new TextView(this);
                enterRowInfo(vehicleName, tableRow, currentJourney.getVehicleName());

               TextView carbonEmission = new TextView(this);
              enterRowInfo(carbonEmission, tableRow,"" +currentJourney.getCarbonEmitted());

                table.addView(tableRow);
            }
        }
    }

    public void makeColumnHeading(String heading, TableRow tableRow0) {
        TextView tableColumn = new TextView(this);
        tableColumn.setText(heading);
        tableColumn.setTextColor(Color.BLACK);
        tableRow0.addView(tableColumn);
    }

    public void enterRowInfo(TextView textView, TableRow tableRow, String vehicle) {
        textView.setText("" + vehicle);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        tableRow.addView(textView);
    }
}
