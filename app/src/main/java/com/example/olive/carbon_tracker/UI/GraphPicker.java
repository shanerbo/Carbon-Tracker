package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;


/**
 * lets user pick a single day, monthly or year graph
 */
public class GraphPicker extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_graph_picker);
        setButton(R.id.btnSingleDay);
        setButton(R.id.btnMonthGraph);
        setButton(R.id.btnYearGraph);

        setToolBar();
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

    private void setToolBar(){
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_graph_picker);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.tool_change_unit){
            if(singleton.checkCO2Unit() == 0)
                singleton.humanRelatableUnit();
            else
                singleton.originalUnit();
            saveCO2UnitStatus(singleton.checkCO2Unit());
            Toast.makeText(getApplicationContext(), "CO2 unit has been changed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.tool_about){
            startActivity(new Intent(GraphPicker.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCO2UnitStatus(int status) {
        SharedPreferences prefs = this.getSharedPreferences("CO2Status", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("CO2 status", status);
        editor.apply();
    }
}
