package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

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
        setButton(R.id.btnEditJourney);
    }

    private void setButton(final int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent();
                switch (id) {
                    case R.id.btnCreateJourney:
                        //showActivity = DisplayCarList.makeIntent(MainMenu.this);
                        showActivity = new Intent(MainMenu.this, SelectTransportationModeAndDate.class);
                        break;
                    case R.id.btnCurrentFootprint:
                        showActivity = new Intent(MainMenu.this,DisplayCarbonFootprint.class);
                        break;
                    case R.id.btnEditJourney:
                        showActivity = new Intent(MainMenu.this, DisplayJourneyList.class);
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
