package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olive.carbon_tracker.R;

public class CreateNewJourney extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_journey);
        setButton(R.id.Select_Transportation_Mode);
        setButton(R.id.Select_Route);
    }

    public void setButton(final int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent();
                switch (id) {
                    //TODO: Enter the names of the other Activity classes
                    case R.id.Select_Transportation_Mode:
                        showActivity = new Intent(CreateNewJourney.this, CarList.class);
                        break;
                    case R.id.Select_Route:
                        showActivity = new Intent(CreateNewJourney.this, DisplayRouteList.class);
                        break;

                }
                startActivity(showActivity);
            }
        });
    }
}
