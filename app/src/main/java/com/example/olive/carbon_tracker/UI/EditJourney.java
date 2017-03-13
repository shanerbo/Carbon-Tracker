package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.List;

public class EditJourney extends AppCompatActivity {
    private Singleton singleton = Singleton.getInstance();
    private List<Journey> JourneyList = singleton.getUsersJourneys();
    private Journey currJourney;
    private int JourneyPosition;
    private Intent input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);
        setButton(R.id.btnEditVehicle);
        setButton(R.id.btnEditRoute);
        setButton(R.id.btnEditDate);
        setFloatingActionButton(R.id.btnOkay);
        setFloatingActionButton(R.id.btnDelete);
        input = getIntent();
        JourneyPosition = input.getIntExtra("Position", -1);
        currJourney = singleton.getJourney(JourneyPosition);
        setTextView(R.id.txtCurrentVehicle);
        setTextView(R.id.txtCurrentRoute);
        setTextView(R.id.txtCurrentDate);
    }

    private void setButton(final int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent();
                if (id == R.id.btnEditVehicle) {
                    showActivity = AddCar.makeIntent(EditJourney.this);

                } else if (id == R.id.btnEditRoute) {
                    showActivity = AddNewRoute.makeIntent(EditJourney.this);
                } else {
                    //TODO: Intent opens up the Calender Activity to change date
//                    showActivity = ;
                }
                startActivityForResult(showActivity, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setTextView(R.id.txtCurrentVehicle);
        setTextView(R.id.txtCurrentRoute);
        setTextView(R.id.txtCurrentDate);
    }

    private void setTextView(int id) {
        TextView textView = (TextView) findViewById(id);
        String msg;
        if (id == R.id.txtCurrentVehicle) {
            msg = currJourney.getVehicleName();
        } else if (id == R.id.txtCurrentRoute) {
            msg = currJourney.getRouteName();
        } else {
            msg = currJourney.getDateOfTrip();
        }
        textView.setText(msg);
    }

    private void setFloatingActionButton(final int id) {
        FloatingActionButton button = (FloatingActionButton) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == R.id.btnOkay) {
                    finish();
                } else {
                    setAlertDialog();
                }
            }
        });
    }

    private void setAlertDialog() {
        new AlertDialog.Builder(EditJourney.this)
                .setTitle("Delete Car")
                .setMessage("This Journey will be discarded, do you want to delete it?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent del_intent = new Intent();
                        JourneyList.remove(JourneyPosition);
                        singleton.setJourneyList(JourneyList);
                        setResult(Activity.RESULT_OK, del_intent);
                        Toast.makeText(EditJourney.this,getString(R.string.UserDeleteVehicle),Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
