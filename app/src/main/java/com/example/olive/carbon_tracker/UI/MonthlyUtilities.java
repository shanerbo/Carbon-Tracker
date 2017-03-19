package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthlyUtilities extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_utilities);

        setupCalendarButton(R.id.ID_startDate_button);
        setupCalendarButton(R.id.ID_endDate_button);

        viewCurrentDate();

    }

    private void setupCalendarButton(final int buttonID){
        Button btn = (Button) findViewById(buttonID);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent setDate = new Intent(MonthlyUtilities.this, Calendar.class);
                if(buttonID == R.id.ID_startDate_button)
                    setDate.putExtra("MonthlyUtilities", 10);
                if(buttonID == R.id.ID_endDate_button)
                    setDate.putExtra("MonthlyUtilities", 20);
                startActivity(setDate);
            }
        });
    }

    public void onRestart() {
        super.onRestart();
        TextView currentStartDate = (TextView) findViewById(R.id.displayStartDate);
        String startDay = singleton.getStartDay();
        String startMonth = singleton.getStartMonth();
        String startYear = singleton.getStartYear();
        currentStartDate.setText(startDay + "/" + startMonth + "/" + startYear);
        singleton.setStartDateChanged(true);

        TextView currentEndDate = (TextView) findViewById(R.id.DisplayEndDate);
        String EndDay = singleton.getEndDay();
        String EndMonth = singleton.getEndMonth();
        String EndYear = singleton.getEndYear();
        currentEndDate.setText(EndDay + "/" + EndMonth + "/" + EndYear);
        singleton.setEndDateChanged(true);
    }

    private void viewCurrentDate(){
        boolean isStartDateChanged = singleton.isStartDateChanged();
        boolean isEndDateChanged = singleton.isEndDateChanged();
        TextView currentStartDate = (TextView) findViewById(R.id.displayStartDate);
        TextView currentEndDate = (TextView) findViewById(R.id.DisplayEndDate);
        if(isStartDateChanged == false) {
            Date StartDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

            simpleDateFormat = new SimpleDateFormat("d");
            String StartDay = simpleDateFormat.format(StartDate);

            simpleDateFormat = new SimpleDateFormat("MMMM");
            String StartMonth = simpleDateFormat.format(StartDate);

            simpleDateFormat = new SimpleDateFormat("yyyy");
            String StartYear = simpleDateFormat.format(StartDate);
            singleton.setStartDay(StartDay);
            singleton.setStartMonth(StartMonth);
            singleton.setStartYear(StartYear);
            currentStartDate.setText(StartDay + "/" + StartMonth + "/" + StartYear);
        }
        else if(isStartDateChanged == true){
            super.onRestart();
            String startDay = singleton.getStartDay();
            String startMonth = singleton.getStartMonth();
            String startYear = singleton.getStartYear();
            currentStartDate.setText(startDay + "/" + startMonth + "/" + startYear);
            singleton.setStartDateChanged(false);
        }

        else if(isEndDateChanged == false){
            Date EndDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

            simpleDateFormat = new SimpleDateFormat("d");
            String EndDay = simpleDateFormat.format(EndDate);

            simpleDateFormat = new SimpleDateFormat("MMMM");
            String EndMonth = simpleDateFormat.format(EndDate);

            simpleDateFormat = new SimpleDateFormat("yyyy");
            String EndYear = simpleDateFormat.format(EndDate);
            singleton.setEndDay(EndDay);
            singleton.setEndMonth(EndMonth);
            singleton.setEndYear(EndYear);
            currentEndDate.setText(EndDay + "/" + EndMonth + "/" + EndYear);
        }
        else if(isEndDateChanged == true){
            super.onRestart();
            String EndDay = singleton.getEndDay();
            String EndMonth = singleton.getEndMonth();
            String EndYear = singleton.getEndYear();
            currentEndDate.setText(EndDay + "/" + EndMonth + "/" + EndYear);
            singleton.setEndDateChanged(false);

        }
    }
}
