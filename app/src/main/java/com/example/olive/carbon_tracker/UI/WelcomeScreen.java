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

        //TODO Get data location for CO2
//        generateTipsForCar(singleton.getHighestCO2FromCar());
//        generateTipsForEnegy(100.0);
//        generateTipsForGas(100.0);
//        generateTipsForUnrelated();
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

//    private void generateTipsForGas(double co2) {
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_1, co2));
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_2, co2));
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_3, co2));
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_4, co2));
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_5, co2));
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_6, co2));
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_7, co2));
//        allRandomGasTips.add(getString(R.string.natural_gas_tip_8, co2));
//        Collections.shuffle(allRandomGasTips);
//        singleton.setShuffledTipsForGas(allRandomGasTips);
//    }
//
//    private void generateTipsForEnegy(double co2) {
//        allRandomEnegyTips.add(getString(R.string.energy_tip_1, co2));
//        allRandomEnegyTips.add(getString(R.string.energy_tip_2, co2));
//        allRandomEnegyTips.add(getString(R.string.energy_tip_3, co2));
//        allRandomEnegyTips.add(getString(R.string.energy_tip_4, co2));
//        allRandomEnegyTips.add(getString(R.string.energy_tip_5, co2));
//        allRandomEnegyTips.add(getString(R.string.energy_tip_6, co2));
//        allRandomEnegyTips.add(getString(R.string.energy_tip_7, co2));
//        allRandomEnegyTips.add(getString(R.string.energy_tip_8, co2));
//        Collections.shuffle(allRandomEnegyTips);
//        singleton.setShuffledTipsForEnegy(allRandomEnegyTips);
//    }
//
//
//    private void generateTipsForCar(double co2){
//        allRandomCarTips.add(getString(R.string.vehicle_tip_1, co2));
//        allRandomCarTips.add(getString(R.string.vehicle_tip_2, co2));
//        allRandomCarTips.add(getString(R.string.vehicle_tip_3, co2));
//        allRandomCarTips.add(getString(R.string.vehicle_tip_4, co2));
//        allRandomCarTips.add(getString(R.string.vehicle_tip_5, co2));
//        allRandomCarTips.add(getString(R.string.vehicle_tip_6, co2));
//        allRandomCarTips.add(getString(R.string.vehicle_tip_7, co2));
//        allRandomCarTips.add(getString(R.string.vehicle_tip_8, co2));
//        Collections.shuffle(allRandomCarTips);
//        singleton.setShuffledTipsForCar(allRandomCarTips);
//    }
//    private void generateTipsForUnrelated(){
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_1));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_2));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_3));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_4));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_5));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_6));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_7));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_8));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_9));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_10));
//        allRandomUnrelatedTips.add(getString(R.string.unrelated_tip_11));
//        Collections.shuffle(allRandomUnrelatedTips);
//        singleton.setUnrelatedTips(allRandomUnrelatedTips);
//    }
}