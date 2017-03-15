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
public class MainActivity extends AppCompatActivity {
    private static int exist_time = 2000;
    Singleton singleton = Singleton.getInstance();
    public DatabaseHelper myHelper;
    public SuperUltraInfoDataBaseHelper TableHelper;
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
//------database done-------------------------

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent MainMenu = new Intent(MainActivity.this, com.example.olive.carbon_tracker.UI.MainMenu.class);
//                singleton.setVehicleData(MainActivity.this);
//                singleton.setVehicleMakeArray();
                startActivity(MainMenu);
                finish();
            }
        },exist_time);
    }
}