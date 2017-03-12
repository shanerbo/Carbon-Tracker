package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.text.SimpleDateFormat;
import java.util.*;

public class SelectTransportationModeAndDate extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_transportation_mode_and_date);

        populateTransportationSpinner();
        setButton(R.id.ID_button_OKmode);
        setButton(R.id.ID_button_mode_cancel);
        viewCurrentDate();
        setupCalendarButton();

    }
    public void onRestart() {
        super.onRestart();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate);
        String day = singleton.getUserDay();
        String month = singleton.getUserMonth();
        String year = singleton.getUserYear();
        currentDate.setText(day + "/" + month + "/" + year);
        singleton.setIsDateChanged(true);
    }
    private void viewCurrentDate(){
        boolean isDateChanged = singleton.getIsDateChanged();
        TextView currentDate = (TextView) findViewById(R.id.txtCurrentDate);
        if(isDateChanged == false) {
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


    private void setupCalendarButton(){
            Button btn = (Button) findViewById(R.id.btnChangeDate);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(SelectTransportationModeAndDate.this,Calendar.class);
                    startActivity(intent);
                }
            });
    }

    private void populateTransportationSpinner() {
        String[] modes = {"Car", "Walk/bike", "Bus", "Skytrain"};
        ArrayAdapter<String> mode_adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, modes);
        Spinner mode_spinner = (Spinner) findViewById(R.id.ID_trans_mode_spinner);
        mode_spinner.setAdapter(mode_adapter);

        mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String UserMode = parent.getSelectedItem().toString();
                if(UserMode.matches("Car")){
;
                }
                else if(UserMode.matches("Walk/bike")){ //0g of CO2 emissions per km of walked / bike travel.
;
                }
                else if(UserMode.matches("Bus")){ //89g of CO2 emissions per km of bus travel.
;
                }
                else if(UserMode.matches("Skytrain")){ //unknown CO2 emission
;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void setButton(final int id) {
        FloatingActionButton button = (FloatingActionButton) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id) {
                    case R.id.ID_button_OKmode:
                        Intent showActivity = new Intent(SelectTransportationModeAndDate.this, DisplayCarList.class);
                        startActivity(showActivity);
                        break;
                    case R.id.ID_button_mode_cancel:
                        finish();
                        break;
                }
            }
        });
    }

}
