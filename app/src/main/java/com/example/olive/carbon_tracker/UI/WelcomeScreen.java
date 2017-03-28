package com.example.olive.carbon_tracker.UI;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.olive.carbon_tracker.Model.AlarmReceiver;
import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.SuperUltraInfoDataBaseHelper;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * first screen the user sees as the app loads the data
 */
public class WelcomeScreen extends AppCompatActivity {
    private static int exist_time = 3000;
    Singleton singleton = Singleton.getInstance();
    public DatabaseHelper myHelper;
    public SuperUltraInfoDataBaseHelper TableHelper;
    public SQLiteDatabase myDataBase;
    List<Journey> journeyList = singleton.getUsersJourneys();
    private List<String> allRandomCarTips = new ArrayList<>();
    private List<String> allRandomEnegyTips = new ArrayList<>();
    private List<String> allRandomGasTips = new ArrayList<>();
    private List<String> allRandomUnrelatedTips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setAlarm();
        setDates();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
//------DataBase startup-----------------------
        myHelper = new DatabaseHelper(this);
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);
        myDataBase.close();
        myHelper.close();
        TableHelper = new SuperUltraInfoDataBaseHelper(this);
        SQLiteDatabase createTable =TableHelper.getWritableDatabase();
        createTable.close();
        TableHelper.close();
//------database done-------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent MainMenu = new Intent(WelcomeScreen.this, com.example.olive.carbon_tracker.UI.MainMenu.class);
                startActivity(MainMenu);
                finish();
            }
        },exist_time);


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

    private void setDates() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        singleton.setCurrentDate(sdf.format(calendar.getTime()));
        singleton.setLatestBill(sdf.format(calendar.getTime()));
    }
}