package com.example.olive.carbon_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setButton(R.id.btnCreateJourney);
        setButton(R.id.btnSelectJourney);
        setButton(R.id.btnCurrentFootprint);
    }

    private void setButton(final int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent();
                switch (id) {
                    //TODO: Enter the names of the other Activity classes
                    case R.id.btnCreateJourney:
                        //showActivity = new Intent(MainMenu.this, );
                        break;
                    case R.id.btnSelectJourney:
                        //showActivity = new Intent(MainMenu.this, );
                        break;
                    case R.id.btnCurrentFootprint:
                        //showActivity = new Intent(MainMenu.this, );
                        break;
                }
                startActivity(showActivity);
            }
        });
    }


}
