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
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class DisplayCarbonFootprint extends AppCompatActivity {

    private static final int NUM_COLS = 2;

    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carbon_footprint);
        setupPieChart();
        populateCarbonFootprintTable();
        getWindow().getDecorView().setBackgroundColor(Color.rgb(49, 86, 28));

    }

    //TODO will be used when we set up pie chart class
    private void setupPieChart() {
        Button btn = (Button) findViewById(R.id.btn_PieChart);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DisplayCarbonFootprint.this,Chart.class);
                startActivity(intent);
            }
        });
    }

    public void populateCarbonFootprintTable() {
       List<Journey> journeyList = singleton.getUsersJourneys();
        int NUM_ROWS = journeyList.size();   //rows will be dynamic


        //  Toast.makeText(DisplayCarbonFootprint.this, " first route name is " + routes. , Toast.LENGTH_SHORT).show();
        List<String> vehicleNames = singleton.getMake(this);
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
                //Toast.makeText(getApplicationContext(),"" + currentVehicle.get)
                enterRowInfo(dateOfTrip, tableRow, currentJourney.getDateOfTrip(), i);

                TextView routeName = new TextView(this);
                enterRowInfo(routeName, tableRow, currentJourney.getRouteName(), i);

                TextView distance = new TextView(this);
                enterRowInfo(distance, tableRow," " +currentJourney.getTotalDistance(),i);

                TextView vehicleName = new TextView(this);
                enterRowInfo(vehicleName, tableRow, currentJourney.getVehicleName(), i);

               TextView carbonEmission = new TextView(this);
              enterRowInfo(carbonEmission, tableRow,"" +currentJourney.getCarbonEmitted(),i);

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

    public void enterRowInfo(TextView textView, TableRow tableRow, String vehicle, int i) {
        textView.setText("" + vehicle);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        tableRow.addView(textView);
    }
}
