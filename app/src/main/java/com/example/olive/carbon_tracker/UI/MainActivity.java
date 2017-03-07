package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

public class MainActivity extends AppCompatActivity {
    private static int exist_time = 2500;
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent MainMenu = new Intent(MainActivity.this, com.example.olive.carbon_tracker.UI.MainMenu.class);
                startActivity(MainMenu);
                singleton.setVehicleData(MainActivity.this);
                finish();
            }
        },exist_time);

    }

}
