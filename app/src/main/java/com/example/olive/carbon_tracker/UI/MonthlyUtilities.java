package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olive.carbon_tracker.R;

public class MonthlyUtilities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_utilities);

        setupCalendarButton(R.id.ID_startDate_button);
        setupCalendarButton(R.id.ID_endDate_button);

    }

    private void setupCalendarButton(int buttonID){
        Button btn = (Button) findViewById(buttonID);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MonthlyUtilities.this,Calendar.class);
                startActivity(intent);
            }
        });
    }
}
