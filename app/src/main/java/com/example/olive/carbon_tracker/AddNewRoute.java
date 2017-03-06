package com.example.olive.carbon_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddNewRoute extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_new_route);
    }
}
