package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.DateInterval;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.MonthlyUtilitiesData;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class MonthlyUtilities extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    private List<MonthlyUtilitiesData> MonthlyUtilitiesList = new ArrayList<>();

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_utilities);
        MonthlyUtilitiesList = singleton.getBillList();

        if(singleton.checkEditMonthlyUtilities() == 1){
            position = singleton.getEditPosition_bill();
            MonthlyUtilitiesData billToBeEdit = MonthlyUtilitiesList.get(position);

            TextView StartDate = (TextView) findViewById(R.id.displayStartDate);
            TextView EndDate = (TextView) findViewById(R.id.DisplayEndDate);
            StartDate.setText(billToBeEdit.getStartDate());
            EndDate.setText(billToBeEdit.getEndDate());

            EditText elec = (EditText) findViewById(R.id.editElecUsage);
            EditText gas = (EditText) findViewById(R.id.editNaturalGasUsage);
            EditText people = (EditText) findViewById(R.id.editNum_people);
            String roundElec = String.format("%.2f", billToBeEdit.getIndElecUsage() * billToBeEdit.getNumOfPeople() * billToBeEdit.getTotalDays());
            String roundGas = String.format("%.2f", billToBeEdit.getIndGasUsage() * billToBeEdit.getNumOfPeople() * billToBeEdit.getTotalDays());
            elec.setText(roundElec);
            gas.setText(roundGas);
            people.setText(""+billToBeEdit.getNumOfPeople()+"");
        }


        setupCalendarButton(R.id.ID_startDate_button);
        setupCalendarButton(R.id.ID_endDate_button);

        setupAddButton(position);
        setupDeleteButton(position);

        viewCurrentDate();



    }



    private void setupCalendarButton(final int buttonID){
        Button btn = (Button) findViewById(buttonID);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent setDate = new Intent(MonthlyUtilities.this, Calendar.class);
                if(buttonID == R.id.ID_startDate_button)
                    setDate.putExtra("MonthlyUtilities", 10);
                if(buttonID == R.id.ID_endDate_button)
                    setDate.putExtra("MonthlyUtilities", 20);
                startActivity(setDate);
            }
        });
    }

    private void viewCurrentDate(){
        boolean isStartDateChanged = singleton.isStartDateChanged();
        boolean isEndDateChanged = singleton.isEndDateChanged();
        TextView currentStartDate = (TextView) findViewById(R.id.displayStartDate);
        TextView currentEndDate = (TextView) findViewById(R.id.DisplayEndDate);
        if(isStartDateChanged == false) {
            Date StartDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

            simpleDateFormat = new SimpleDateFormat("d");
            String StartDay = simpleDateFormat.format(StartDate);

            simpleDateFormat = new SimpleDateFormat("MM");
            String StartMonth = simpleDateFormat.format(StartDate);

            simpleDateFormat = new SimpleDateFormat("yyyy");
            String StartYear = simpleDateFormat.format(StartDate);
            singleton.setStartDay(StartDay);
            singleton.setStartMonth(StartMonth);
            singleton.setStartYear(StartYear);
            currentStartDate.setText(StartDay + "/" + StartMonth + "/" + StartYear);
        }
        if(isStartDateChanged == true){
            super.onRestart();
            String startDay = singleton.getStartDay();
            String startMonth = singleton.getStartMonth();
            String startYear = singleton.getStartYear();
            currentStartDate.setText(startDay + "/" + startMonth + "/" + startYear);
            singleton.setStartDateChanged(false);
        }

        if(isEndDateChanged == false){
            Date EndDate = new Date();
            SimpleDateFormat simpleEndDateFormat = new SimpleDateFormat("EEEE");

            simpleEndDateFormat = new SimpleDateFormat("d");
            String EndDay = simpleEndDateFormat.format(EndDate);

            simpleEndDateFormat = new SimpleDateFormat("MM");
            String EndMonth = simpleEndDateFormat.format(EndDate);

            simpleEndDateFormat = new SimpleDateFormat("yyyy");
            String EndYear = simpleEndDateFormat.format(EndDate);
            singleton.setEndDay(EndDay);
            singleton.setEndMonth(EndMonth);
            singleton.setEndYear(EndYear);
            currentEndDate.setText(EndDay + "/" + EndMonth + "/" + EndYear);
        }
        if(isEndDateChanged == true){
            super.onRestart();
            String EndDay = singleton.getEndDay();
            String EndMonth = singleton.getEndMonth();
            String EndYear = singleton.getEndYear();
            currentEndDate.setText(EndDay + "/" + EndMonth + "/" + EndYear);
            singleton.setEndDateChanged(false);

        }
    }

    public void onRestart() {
        super.onRestart();
        TextView currentStartDate = (TextView) findViewById(R.id.displayStartDate);
        String startDay = singleton.getStartDay();
        String startMonth = singleton.getStartMonth();
        String startYear = singleton.getStartYear();
        currentStartDate.setText(startDay + "/" + startMonth + "/" + startYear);
        singleton.setStartDateChanged(true);

        TextView currentEndDate = (TextView) findViewById(R.id.DisplayEndDate);
        String EndDay = singleton.getEndDay();
        String EndMonth = singleton.getEndMonth();
        String EndYear = singleton.getEndYear();
        currentEndDate.setText(EndDay + "/" + EndMonth + "/" + EndYear);
        singleton.setEndDateChanged(true);
    }


    private void setupAddButton(final int position) {
        FloatingActionButton check = (FloatingActionButton) findViewById(R.id.ok_billing_btn);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startDay = singleton.getStartDay();
                String startMonth = singleton.getStartMonth();
                String startYear = singleton.getStartYear();

                String EndDay = singleton.getEndDay();
                String EndMonth = singleton.getEndMonth();
                String EndYear = singleton.getEndYear();

                EditText ETelectricUsage = (EditText) findViewById(R.id.editElecUsage);
                EditText ETnaturalGasUsage = (EditText) findViewById(R.id.editNaturalGasUsage);
                EditText ETnumOfPeople = (EditText) findViewById(R.id.editNum_people);
                String electricUsage = ETelectricUsage.getText().toString();
                String naturalGasUsage = ETnaturalGasUsage.getText().toString();
                String numOfPeople = ETnumOfPeople.getText().toString();


                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date start = sdf.parse(startMonth+"/"+startDay+"/"+startYear);
                        Date end = sdf.parse(EndMonth+"/"+EndDay+"/"+EndYear);
                        long dateDifference = end.getTime() - start.getTime();
                        dateDifference = dateDifference / 1000 / 60 / 60 / 24;
                        //Toast.makeText(MonthlyUtilities.this,""+dateDifference,Toast.LENGTH_LONG).show();

                        if(dateDifference > 0 && (!electricUsage.matches("") || !naturalGasUsage.matches(""))
                                && !numOfPeople.matches("") && parseInt(numOfPeople)!= 0){

                                if(!electricUsage.matches("") && naturalGasUsage.matches("")) {
                                    double indElecUsage = parseDouble(electricUsage) / parseDouble(numOfPeople);
                                    double indGasUsage = 0;
                                    double indCO2 = indElecUsage*0.009 + indGasUsage*56.1;
                                    MonthlyUtilitiesData userInput = new MonthlyUtilitiesData(
                                            startDay+"/"+startMonth+"/"+startYear, EndDay+"/"+EndMonth+"/"+EndYear,
                                            dateDifference, indElecUsage/dateDifference, indGasUsage/dateDifference,
                                            parseLong(numOfPeople), indCO2/dateDifference);

                                    if(singleton.checkEditMonthlyUtilities() == 1){ //editing
                                        //TODO edit with database
                                        MonthlyUtilitiesList.set(position, userInput);
                                        singleton.setBillList(MonthlyUtilitiesList);
                                        singleton.userFinishEditMonthlyUtilities();
                                    }
                                    else {
                                        MonthlyUtilitiesList.add(userInput);
                                        singleton.userFinishAdd_MonthlyUtilities();
                                    }
                                }
                                else if(!naturalGasUsage.matches("") && electricUsage.matches("")) {
                                    double indElecUsage = 0;
                                    double indGasUsage = parseDouble(naturalGasUsage) / parseDouble(numOfPeople);
                                    double indCO2 = indElecUsage*0.009 + indGasUsage*56.1;
                                    MonthlyUtilitiesData userInput = new MonthlyUtilitiesData(
                                            startDay + "/" + startMonth + "/" + startYear, EndDay + "/" + EndMonth + "/" + EndYear,
                                            dateDifference, indElecUsage/dateDifference, indGasUsage/dateDifference,
                                            parseLong(numOfPeople),indCO2/dateDifference);

                                    if(singleton.checkEditMonthlyUtilities() == 1){ //editing
                                        //TODO edit with database
                                        MonthlyUtilitiesList.set(position, userInput);
                                        singleton.setBillList(MonthlyUtilitiesList);
                                        singleton.userFinishEditMonthlyUtilities();
                                    }
                                    else {
                                        MonthlyUtilitiesList.add(userInput);
                                        singleton.userFinishAdd_MonthlyUtilities();
                                    }
                                }
                                else{
                                    double indElecUsage = parseDouble(electricUsage) / parseDouble(numOfPeople);
                                    double indGasUsage = parseDouble(naturalGasUsage) / parseDouble(numOfPeople);
                                    double indCO2 = indElecUsage*0.009 + indGasUsage*56.1;
                                    MonthlyUtilitiesData userInput = new MonthlyUtilitiesData(
                                            startDay+"/"+startMonth+"/"+startYear, EndDay+"/"+EndMonth+"/"+EndYear,
                                            dateDifference, indElecUsage/dateDifference, indGasUsage/dateDifference,
                                            parseLong(numOfPeople), indCO2/dateDifference);
                                    //Toast.makeText(MonthlyUtilities.this, decimalElec+"-"+decimalGas,Toast.LENGTH_LONG).show();
                                    if(singleton.checkEditMonthlyUtilities() == 1){ //editing
                                        //TODO edit with database
                                        MonthlyUtilitiesList.set(position, userInput);
                                        singleton.setBillList(MonthlyUtilitiesList);
                                        singleton.userFinishEditMonthlyUtilities();
                                    }
                                    else {
                                        MonthlyUtilitiesList.add(userInput);
                                        singleton.userFinishAdd_MonthlyUtilities();
                                    }
                                }


                            startActivity(new Intent(MonthlyUtilities.this, DisplayMonthlyUtilities.class));
                            finish();
                        }
                        else if(dateDifference <= 0){
                            Toast.makeText(MonthlyUtilities.this,"Ending date cannot be equal to or earlier than starting date",Toast.LENGTH_LONG).show();
                        }
                        else if(electricUsage.matches("") && naturalGasUsage.matches("")){
                            Toast.makeText(MonthlyUtilities.this,"Please fill in at least one of the usage data",Toast.LENGTH_LONG).show();
                        }
                        else if(numOfPeople.matches("") || parseInt(numOfPeople)== 0){
                            Toast.makeText(MonthlyUtilities.this,"Number of people cannot be zero(blank)",Toast.LENGTH_LONG).show();
                        }


                    }
                    catch(Exception  e){
                        Toast.makeText(MonthlyUtilities.this, "Please pick a date", Toast.LENGTH_LONG).show();
                    }

            }
        });
    }



    private void setupDeleteButton(final int position){
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.cancel_billing_btn);
        if(singleton.checkAdd_MonthlyUtilities() == 1){
            delete.setVisibility(View.INVISIBLE);
            return;
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MonthlyUtilities.this)
                        .setTitle("Delete Bill")
                        .setMessage(R.string.BillWarning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent del_intent = new Intent();
                                //TODO delete bill from database
                                MonthlyUtilitiesList.remove(position);
                                singleton.setBillList(MonthlyUtilitiesList);
                                singleton.userFinishEditMonthlyUtilities();
                                setResult(Activity.RESULT_OK, del_intent);
                                Toast.makeText(MonthlyUtilities.this, "The selected bill has been deleted", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MonthlyUtilities.this, DisplayMonthlyUtilities.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                singleton.userFinishEditMonthlyUtilities();
                                startActivity(new Intent(MonthlyUtilities.this, DisplayMonthlyUtilities.class));
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
    }

    public void onBackPressed(){
        singleton.userFinishEditMonthlyUtilities();
        singleton.userFinishAdd_MonthlyUtilities();
        startActivity(new Intent(MonthlyUtilities.this, DisplayMonthlyUtilities.class));
        finish();
    }


}
