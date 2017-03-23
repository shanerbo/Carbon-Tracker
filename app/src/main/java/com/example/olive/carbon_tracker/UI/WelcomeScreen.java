package com.example.olive.carbon_tracker.UI;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.SuperUltraInfoDataBaseHelper;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * first screen the user sees as the app loads the data
 */
public class WelcomeScreen extends AppCompatActivity {
    private static int exist_time = 2000;
    Singleton singleton = Singleton.getInstance();
    public DatabaseHelper myHelper;
    public SuperUltraInfoDataBaseHelper TableHelper;
    public SQLiteDatabase myDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
//------DataBase starup-----------------------
        myHelper = new DatabaseHelper(this);
        SQLiteDatabase myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);
        myDataBase.close();
        myHelper.close();
        TableHelper = new SuperUltraInfoDataBaseHelper(this);
        SQLiteDatabase createTable =TableHelper.getWritableDatabase();
        createTable.close();
        TableHelper.close();
        generateTips();
//------database done-------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent MainMenu = new Intent(WelcomeScreen.this, com.example.olive.carbon_tracker.UI.MainMenu.class);
//                singleton.setVehicleData(WelcomeScreen.this);
//                singleton.setVehicleMakeArray();
                startActivity(MainMenu);
                finish();
            }
        },exist_time);
    }
    private List<String> allRandomTips = new ArrayList<>();
    private void generateTips(){
        allRandomTips.add("Your highest co2 is coming from driving. Try using transit.");
        allRandomTips.add("Your highest co2 is coming from driving. Try car pooling.");
        allRandomTips.add("Your highest co2 is coming from driving. Try getting an electric car.");
        allRandomTips.add("Your highest co2 is coming from driving. Try Combine errands to make fewer trips.");
        allRandomTips.add("Misc. Combine errands to make fewer trips. Remove excess weight from your car. Use cruise control.");
        allRandomTips.add("Your electricity usage is high, try installing solar panels.");
        allRandomTips.add("Your electricity usage is high, try shutting off lights when not in use");
        allRandomTips.add("Your electricity usage is high, try unplugging unused electronic devices");
        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
        allRandomTips.add("The car you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
//        allRandomTips.add("The journey you just add has highest CO2 emission compare with past. ");
          Collections.shuffle(allRandomTips);
        singleton.setShuffledTips(allRandomTips);
    }
}