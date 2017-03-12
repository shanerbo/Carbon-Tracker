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

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

public class SelectTransportationModeAndDate extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    private static int UserTransportationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_transportation_mode_and_date);


        populateTransportationSpinner();
        setButton(R.id.ID_button_OKmode);
        setButton(R.id.ID_button_mode_cancel);

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
                    UserTransportationMode = 0;
                    singleton.ModeCar();
                }
                else if(UserMode.matches("Walk/bike")){ //0g of CO2 emissions per km of walked / bike travel.
                    UserTransportationMode = 1;
                    singleton.ModeWalkBike();
                }
                else if(UserMode.matches("Bus")){ //89g of CO2 emissions per km of bus travel.
                    UserTransportationMode = 2;
                    singleton.ModeBus();
                }
                else if(UserMode.matches("Skytrain")){ //33g of CO2 emissions per km of skytrain travel.
                    UserTransportationMode = 3;
                    singleton.ModeSkytrain();
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
                        if(UserTransportationMode == 0) { //user selects car
                            Intent showActivity = new Intent(SelectTransportationModeAndDate.this, DisplayCarList.class);
                            startActivity(showActivity);
                            break;
                        }
                        else { //user selects transportation modes other than cars
                            Intent showActivity = new Intent(SelectTransportationModeAndDate.this, DisplayRouteList.class);
                            startActivity(showActivity);
                            break;
                        }
                    case R.id.ID_button_mode_cancel:
                        finish();
                        break;
                }
            }
        });
    }

}
