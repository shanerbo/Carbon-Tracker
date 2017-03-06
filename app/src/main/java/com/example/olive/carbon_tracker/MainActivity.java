package com.example.olive.carbon_tracker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;

public class MainActivity extends AppCompatActivity {
    private static int exist_time = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent MainMenu = new Intent(MainActivity.this,MainMenu.class);
                startActivity(MainMenu);
                finish();
            }
        },exist_time);
    }
}
