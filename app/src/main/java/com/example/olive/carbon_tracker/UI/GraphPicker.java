package com.example.olive.carbon_tracker.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.olive.carbon_tracker.Model.AlarmReceiver;
import com.example.olive.carbon_tracker.R;

import java.util.Calendar;

/**
 * lets user pick a single day, monthly or year graph
 */
public class GraphPicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setAlarm();
        setContentView(R.layout.activity_graph_picker);
        setButton(R.id.btnSingleDay);
        setButton(R.id.btnMonthGraph);
        setButton(R.id.btnYearGraph);
    }


    private void setButton(final int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent();
                switch (id) {
                    case R.id.btnSingleDay:
                        //showActivity = DisplayCarList.makeIntent(MainMenu.this);
                        showActivity = new Intent(GraphPicker.this, SingleDayGraph.class);
                        break;
                    case R.id.btnMonthGraph:
                        showActivity = new Intent(GraphPicker.this,MonthGraph.class);
                        break;
                    case R.id.btnYearGraph:
                        showActivity = new Intent(GraphPicker.this,YearGraph.class);
                        break;

                }
                startActivity(showActivity);
            }
        });
    }

    private void setAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }
}
