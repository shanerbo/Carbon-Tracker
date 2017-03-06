package com.example.olive.carbon_tracker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddCar extends AppCompatActivity {
    //private PotCollection MyPot = new PotCollection();
    private VehicleData myCar = new VehicleData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        populateDropDownMenus();
        setupAddCarButton();
    }

    private void populateDropDownMenus() {
        myCar.ExtractVehicleData(this);
        List<String> make_list = myCar.getVehicleMakeArray();
//        List<Vehicle> make_list = Singleton.getCurrInstance().getVehicle();
//        ArrayAdapter<Vehicle>make_adapter =  new ArrayAdapter<>(
//                this, android.R.layout.simple_dropdown_item_1line, make_list);
        ArrayAdapter<String> make_adapter =  new ArrayAdapter<>(
               this, android.R.layout.simple_dropdown_item_1line, make_list);
        Spinner Make_spinner = (Spinner) findViewById(R.id.ID_drop_down_make);
        Make_spinner.setAdapter(make_adapter);

        List<String> model_list = myCar.getVehicleModelArray();
        ArrayAdapter<String> model_adapter =  new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, model_list);
        Spinner Model_spinner = (Spinner) findViewById(R.id.ID_drop_down_model);
        Model_spinner.setAdapter(model_adapter);

        List<Integer> year_list = myCar.getVehicleYearArray();
        ArrayAdapter<Integer> year_adapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_dropdown_item_1line, year_list);
        Spinner Year_spinner = (Spinner) findViewById(R.id.ID_drop_down_year);
        Year_spinner.setAdapter(year_adapter);




    }

    private void setupAddCarButton() {
        Button AddCar = (Button) findViewById(R.id.ID_add_new_car_button);
//        AddCar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText NickName = (EditText) findViewById(R.id.ID_Car_Name);
//                if(NickName.length() <= 0){
//                    Toast.makeText(AddCar.this, "Adding Car Failed: car name cannot be empty", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//            }
//        });
    }
}
