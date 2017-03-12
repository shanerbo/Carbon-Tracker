package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.olive.carbon_tracker.R;

public class EditJourney extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);
        setButton(R.id.btnEditVehicle);
        setButton(R.id.btnEditRoute);
        setButton(R.id.btnEditDate);
    }
    //TODO: Establish a way to access a journey

    private void setButton(int id) {
        Button button = (Button) findViewById(id);
        Intent showEditActivity = new Intent();
        if (id == R.id.btnEditVehicle) {
            showEditActivity = AddCar.makeIntent(EditJourney.this);
        } else if (id == R.id.btnEditRoute) {
            showEditActivity = AddNewRoute.makeIntent(EditJourney.this);
        } else {
            //TODO: Intent opens up the Calender Activity to change date
//            showEditActivity = ;
        }
        startActivity(showEditActivity);
    }
}
