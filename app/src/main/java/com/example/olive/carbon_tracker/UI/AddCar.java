package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    //private PotCollection MyPot = new PotCollection();

    private List<Vehicle> VehicleList = new ArrayList<Vehicle>();
    List<String> make_list = new ArrayList<String>();
    List<Integer> year_list = new ArrayList<Integer>();
    Singleton singleton = Singleton.getInstance();
    private int position;
    VehicleData vehicleData = new VehicleData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        VehicleList = singleton.getVehicleList();
        make_list = singleton.getVehicleMakeArray();
        year_list = singleton.getVehicleYearArray();
        vehicleData = singleton.getVehicleData();
        if (singleton.checkEdit_car() ==1 ){
            position = singleton.getEditPosition_car();
            Vehicle VehicleToBeEdit = VehicleList.get(position);
            String VehicleName = VehicleToBeEdit.getName();
            EditText Name = (EditText) findViewById(R.id.ID_Car_Name);
            Name.setText(VehicleName);
        }else{
            position = singleton.getAddPosition_car();
        }


        populateDropDownMenus();
        setupAddCarButton(position);
        delButton(position);
    }

    private void populateDropDownMenus() {


        //TODO extract method
        //TODO access via singleton class

//        List<Vehicle> make_list = Singleton.getCurrInstance().getVehicle();
//        ArrayAdapter<Vehicle>make_adapter =  new ArrayAdapter<>(
//                this, android.R.layout.simple_dropdown_item_1line, make_list);
        //List<String> make_list = myCar.getVehicleMakeArray();
//        List<String> make_list = myCar.getUniqueVehicleMakeArray();
        ArrayAdapter<String> make_adapter =  new ArrayAdapter<>(
               this, android.R.layout.simple_dropdown_item_1line, make_list);
        Spinner Make_spinner = (Spinner) findViewById(R.id.ID_drop_down_make);
        Make_spinner.setAdapter(make_adapter);



        Make_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Make = parent.getSelectedItem().toString();
                Toast.makeText(AddCar.this, "the make is: " + Make, Toast.LENGTH_SHORT).show();
                List<String> model_list = singleton.updateModels(Make);
                Toast.makeText(AddCar.this, "the first model is : " + model_list.get(0), Toast.LENGTH_SHORT).show();


                ArrayAdapter<String> model_adapter =  new ArrayAdapter<String>(
                        AddCar.this, android.R.layout.simple_dropdown_item_1line, model_list);
                Spinner Model_spinner = (Spinner) findViewById(R.id.ID_drop_down_model);
                Model_spinner.setAdapter(model_adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        //List<String> model_list = myCar.getVehicleModelArray();
        List<String> model_list = vehicleData.getModelsForAMake(Make_spinner.getSelectedItem().toString());
        ArrayAdapter<String> model_adapter =  new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, model_list);
        Spinner Model_spinner = (Spinner) findViewById(R.id.ID_drop_down_model);
        Model_spinner.setAdapter(model_adapter);

        ArrayAdapter<Integer> year_adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, year_list);
        Spinner Year_spinner = (Spinner) findViewById(R.id.ID_drop_down_year);
        Year_spinner.setAdapter(year_adapter);


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

                if(nickname.length() == 0){
                    Toast.makeText(AddCar.this, "Adding Car Failed: car name cannot be empty", Toast.LENGTH_LONG).show();
                    finish();
                }


                String CarName = nickname.getText().toString();
                String CarMake = Make_spinner.getSelectedItem().toString();
                String CarModel = Model_spinner.getSelectedItem().toString();
                int CarYear = Integer.parseInt(Year_spinner.getSelectedItem().toString());
                if(!CarName.matches("") && !CarMake.matches("") && !CarModel.matches("") && CarYear>0){
                    Vehicle userInput = new Vehicle(CarName,CarMake,CarModel,CarYear);
                    if (singleton.checkEdit_car() ==1){
                        VehicleList.set(position,userInput);
                        singleton.setVehiclesList(VehicleList);
                        singleton.userFinishEdit_car();
                    }else{
                        VehicleList.add(userInput);
                        singleton.setVehiclesList(VehicleList);
                        singleton.userFinishAdd_car();
                    }
                }

                Toast.makeText(AddCar.this, "Saving your "+ CarName +"'s info... ", Toast.LENGTH_SHORT).show();
                finish();
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
                        .setTitle("Delete Route")
                        .setMessage(R.string.Warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent del_intent = new Intent();
                                VehicleList.remove(position);
                                singleton.setVehiclesList(VehicleList);
                                setResult(Activity.RESULT_OK,del_intent);
                                Toast.makeText(AddCar.this,getString(R.string.UserDeleteVehicle),Toast.LENGTH_LONG).show();
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
        });
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, AddCar.class);
    }
}
