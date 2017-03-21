package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.olive.carbon_tracker.R;

public class GraphPicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_graph_picker);
        setButton(R.id.btnSingleDay);
        setButton(R.id.btnMonthGraph);
        setButton(R.id.btnYearGraph);
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
}
