package com.example.olive.carbon_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DisplayRouteList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_route_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ChooseRoute);
        setSupportActionBar(toolbar);

        FloatingActionButton UserCreateRoute = (FloatingActionButton) findViewById(R.id.AddRoute);
        UserCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                Intent addNewRoute = new Intent(DisplayRouteList.this,AddNewRoute.class);
                startActivityForResult(addNewRoute,1);//1 means edit the route
            }
        });
    }

}
