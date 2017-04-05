package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
//        setAlarm();
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

        int CO2Status = getCO2UnitStatus(this);
        if(CO2Status == 1)
            singleton.humanRelatableUnit();
        else
            singleton.originalUnit();

        hideNavigationBar();

    }



    private void setDates() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        singleton.setCurrentDate(sdf.format(calendar.getTime()));
        singleton.setLatestBill(sdf.format(calendar.getTime()));
    }

    static public int getCO2UnitStatus(Context context){
        SharedPreferences prefs = context.getSharedPreferences("CO2Status", MODE_PRIVATE);
        return prefs.getInt("CO2 status", 0);
    }

    private void hideNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if(visibility == 0)
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        else
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    }
                });
    }
}