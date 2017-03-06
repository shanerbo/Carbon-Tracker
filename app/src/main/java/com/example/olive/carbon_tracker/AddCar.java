package com.example.olive.carbon_tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        EditCar();
    }

    private void populateDropDownMenus() {
        myCar.ExtractVehicleData(this);

        //TODO extract method
        //TODO access via singleton class

//        List<Vehicle> make_list = Singleton.getCurrInstance().getVehicle();
//        ArrayAdapter<Vehicle>make_adapter =  new ArrayAdapter<>(
//                this, android.R.layout.simple_dropdown_item_1line, make_list);
        //List<String> make_list = myCar.getVehicleMakeArray();
        List<String> make_list = myCar.getUniqueVehicleMakeArray();
        ArrayAdapter<String> make_adapter =  new ArrayAdapter<>(
               this, android.R.layout.simple_dropdown_item_1line, make_list);
        Spinner Make_spinner = (Spinner) findViewById(R.id.ID_drop_down_make);
        Make_spinner.setAdapter(make_adapter);



        Make_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //List<String> model_list = myCar.getModelsForAMake(parent.getSelectedItem().toString());
//                ArrayAdapter<String> model_adapter =  new ArrayAdapter<String>(
//                        this, android.R.layout.simple_dropdown_item_1line, model_list);
//                Spinner Model_spinner = (Spinner) findViewById(R.id.ID_drop_down_model);
//                Model_spinner.setAdapter(model_adapter);
                Toast.makeText(AddCar.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        //List<String> model_list = myCar.getVehicleModelArray();
        List<String> model_list = myCar.getModelsForAMake(Make_spinner.getSelectedItem().toString());
        ArrayAdapter<String> model_adapter =  new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, model_list);
        Spinner Model_spinner = (Spinner) findViewById(R.id.ID_drop_down_model);
        Model_spinner.setAdapter(model_adapter);

        List<Integer> year_list = myCar.getVehicleYearArray();
        ArrayAdapter<Integer> year_adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, year_list);
        Spinner Year_spinner = (Spinner) findViewById(R.id.ID_drop_down_year);
        Year_spinner.setAdapter(year_adapter);


    }

    private void setupAddCarButton() {
        Button AddCar = (Button) findViewById(R.id.ID_button_OKAdd);
        AddCar.setOnClickListener(new View.OnClickListener() {
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


                Intent saveData = new Intent();
                saveData.putExtra("Car Name", CarName);
                saveData.putExtra("Car Make", CarMake);
                saveData.putExtra("Car Model", CarModel);
                saveData.putExtra("Car Year", CarYear);
                setResult(Activity.RESULT_OK, saveData);
                Toast.makeText(AddCar.this, "Saving your "+ CarName +"'s info... ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button Cancel = (Button) findViewById(R.id.ID_button_cancel_add);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddCar.this, "Nothing has been saved", Toast.LENGTH_SHORT).show();
                Intent noData = new Intent();
                setResult(Activity.RESULT_CANCELED, noData);
                finish();
            }
        });

    }


    private void EditCar() {
        if (getIntent().getStringExtra("Car Name") != null) {
            if (getIntent().getStringExtra("Car Name").length() != 0) {
                EditText DisplayCarName = (EditText) findViewById(R.id.ID_Car_Name);
                DisplayCarName.setText(getIntent().getStringExtra("Car Name"));
            }
        }
    }
}
