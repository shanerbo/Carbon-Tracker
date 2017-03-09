package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.VehicleData;

import java.util.ArrayList;
import java.util.List;

public class AddCar extends AppCompatActivity {

    private List<Vehicle> VehicleList = new ArrayList<>();
    List<String> make_list = new ArrayList<>();
    List<String> userVehicleNames = new ArrayList<>();
    List<Integer> year_list = new ArrayList<>();
    Singleton singleton = Singleton.getInstance();
    private int position;
    VehicleData vehicleData = new VehicleData();
    String VehicleNameToBeEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        VehicleList = singleton.getVehicleList();
        make_list = singleton.getVehicleMakeArray();
//        year_list = singleton.getVehicleYearArray();

        vehicleData = singleton.getVehicleData();
        if (singleton.checkEdit_car() ==1 ){
            position = singleton.getEditPosition_car();
            Vehicle VehicleToBeEdit = VehicleList.get(position);
            VehicleNameToBeEdit = VehicleToBeEdit.getName();

            EditText Name = (EditText) findViewById(R.id.ID_Car_Name);
            Name.setText(VehicleNameToBeEdit);



        }else{
            position = singleton.getAddPosition_car();
        }


        populateDropDownMenus();
        setupAddCarButton(position);
        delButton(position);
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private int getIndex(Spinner spinner, int myString) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    private void populateDropDownMenus() {

        ArrayAdapter<String> make_adapter =  new ArrayAdapter<>(
               this, android.R.layout.simple_dropdown_item_1line, make_list);
        Spinner Make_spinner = (Spinner) findViewById(R.id.ID_drop_down_make);

        Make_spinner.setAdapter(make_adapter);
        if (VehicleList.size() != 0&&singleton.checkEdit_car() ==1){
            Vehicle VehicleToBeEdit = VehicleList.get(position);
            Make_spinner.setSelection(getIndex(Make_spinner,VehicleToBeEdit.getMake()));
        }

        Make_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position_model, long id) {
                String Make = parent.getSelectedItem().toString();
                List<String> model_list = singleton.updateModels(Make);
                ArrayAdapter<String> model_adapter =  new ArrayAdapter<>(
                        AddCar.this, android.R.layout.simple_dropdown_item_1line, model_list);
                Spinner Model_spinner = (Spinner) findViewById(R.id.ID_drop_down_model);
                Model_spinner.setAdapter(model_adapter);
                if (VehicleList.size() != 0&&singleton.checkEdit_car() ==1){
                    Vehicle VehicleToBeEdit = VehicleList.get(position);
                    Model_spinner.setSelection(getIndex(Model_spinner,VehicleToBeEdit.getModel()));
                }

                Model_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position_year, long id) {
                        final String Model = parent.getSelectedItem().toString();
                        List<Integer> year_list = singleton.updateYears(Model);
                        ArrayAdapter<Integer> year_adapter = new ArrayAdapter<>(
                                AddCar.this, android.R.layout.simple_dropdown_item_1line, year_list);
                        Spinner Year_spinner = (Spinner) findViewById(R.id.ID_drop_down_year);
                        Year_spinner.setAdapter(year_adapter);
                        if (VehicleList.size() != 0&&singleton.checkEdit_car() ==1){
                            Vehicle VehicleToBeEdit = VehicleList.get(position);
                            int Year = VehicleToBeEdit.getYear();
                            Year_spinner.setSelection(getIndex(Year_spinner,Year));
                        }
                        Year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                final Integer Year = Integer.parseInt(parent.getSelectedItem().toString());
                                List<String> disp_list = singleton.updateDispl(Model,Year);
                                ArrayAdapter<String> displ_adapter = new ArrayAdapter<>(
                                        AddCar.this, android.R.layout.simple_dropdown_item_1line, disp_list);
                                Spinner Displ_spinner = (Spinner) findViewById(R.id.ID_drop_down_dspl);
                                Displ_spinner.setAdapter(displ_adapter);
//                                Toast.makeText(AddCar.this,"" +disp_list.get(0),Toast.LENGTH_LONG).show();
//                                if (VehicleList.size() != 0&&singleton.checkEdit_car() ==1){
//                                    Vehicle VehicleToBeEdit = VehicleList.get(position);
//                                    int Displ = VehicleToBeEdit.getYear();
//                                    Year_spinner.setSelection(getIndex(Year_spinner,Year));
//                                }


                            }


                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void setupAddCarButton(final int position) {
        FloatingActionButton check = (FloatingActionButton) findViewById(R.id.ID_button_OKAdd);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nickname = (EditText) findViewById(R.id.ID_Car_Name);
                Spinner Make_spinner = (Spinner) findViewById(R.id.ID_drop_down_make);
                Spinner Model_spinner = (Spinner) findViewById(R.id.ID_drop_down_model);
                Spinner Year_spinner = (Spinner) findViewById(R.id.ID_drop_down_year);
                Spinner Displ_spinner = (Spinner) findViewById(R.id.ID_drop_down_dspl);

                if(nickname.length() != 0) {

                    String CarName = nickname.getText().toString();
                    String CarMake = Make_spinner.getSelectedItem().toString();
                    String CarModel = Model_spinner.getSelectedItem().toString();
                    int CityAndHighway = Displ_spinner.getSelectedItemPosition();

                    int city = singleton.getCityData(CityAndHighway);
                    int highWay = singleton.getHwayData(CityAndHighway);
                    String fuelType = singleton.getFuelType(CityAndHighway);

                    int CarYear = Integer.parseInt(Year_spinner.getSelectedItem().toString());
                    if (!CarName.matches("") && !CarMake.matches("") && !CarModel.matches("") && CarYear > 0) {
                        Vehicle userInput = new Vehicle(CarName, CarMake, CarModel, CarYear,city,highWay,fuelType);

                        if (singleton.checkEdit_car() == 1) {
                            VehicleList.set(position, userInput);
                            singleton.setVehiclesList(VehicleList);
                            singleton.userFinishEdit_car();
                            String userInputNewCarName = userInput.getName();
                            singleton.UserEnterNewCarName(userInputNewCarName,VehicleNameToBeEdit);

                        } else {
                            VehicleList.add(userInput);
                            singleton.setVehiclesList(VehicleList);

                            Vehicle userPickVehicle = userInput;
                            singleton.setUserPickVehicleItem(userPickVehicle);

                            singleton.userFinishAdd_car();
                            Intent userCreateCar = DisplayRouteList.makeIntent(AddCar.this);
                            startActivity(userCreateCar);
                        }
                    }

                    finish();
                }else{
                    Toast.makeText(AddCar.this, "Please fill the name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void delButton(final int position) {
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.ID_button_delete);

        if (singleton.checkAdd_car() == 1){
            delete.setVisibility(View.INVISIBLE);
            //hide the delete button
            return;
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddCar.this)
                        .setTitle("Delete Car")
                        .setMessage(R.string.Warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent del_intent = new Intent();
                                VehicleList.remove(position);
                                singleton.setVehiclesList(VehicleList);
                                singleton.userFinishEdit_car();
                                setResult(Activity.RESULT_OK,del_intent);
                                Toast.makeText(AddCar.this,getString(R.string.UserDeleteVehicle),Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                singleton.userFinishEdit_car();
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
    }
    public void onBackPressed() {
        singleton.userFinishEdit_car();
        singleton.userFinishAdd_car();
        Intent goBackToDisplayCar = DisplayCarList.makeIntent(AddCar.this);
        startActivity(goBackToDisplayCar);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddCar.class);
    }
}
