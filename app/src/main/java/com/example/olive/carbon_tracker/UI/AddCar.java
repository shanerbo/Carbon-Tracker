package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.VehicleData;
import com.github.mikephil.charting.exception.DrawingDataSetNotCreatedException;

import java.util.ArrayList;
import java.util.List;

public class AddCar extends AppCompatActivity {

    private List<Vehicle> VehicleList = new ArrayList<>();
    private List<String> make_list = new ArrayList<>();
    private Singleton singleton = Singleton.getInstance();
    private int position;
    private VehicleData vehicleData = new VehicleData();
    private String VehicleNameToBeEdit;


    private  List<String> carListDB;

    public DatabaseHelper myHelper;
    private SQLiteDatabase myDataBase;

//    myHelper.close()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        VehicleList = singleton.getVehicleList();
        make_list = singleton.getVehicleMakeArray();
//------------create a new DatabaseHelper and it will copy external sql to local-------------------
        myHelper = new DatabaseHelper(this);
        //myHelper.openDataBase();
        myHelper.close();
//------------get local sql which is created by Helper---------------------------------------------
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);

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

        //reading data from database
        String make;
        Cursor cursor = myDataBase.rawQuery("select distinct make from DB order by make asc",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            make = cursor.getString(0);
            make_list.add(make);
            cursor.moveToNext();
        }
        cursor.close();
//        myHelper.close();

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
                final String Make = parent.getSelectedItem().toString();

                //List<String> model_list = singleton.updateModels(Make);
                List<String> model_list = new ArrayList<>();
                String model;
                Cursor cursor = myDataBase.rawQuery("select distinct model from DB where make = ?order by model asc", new String[]{Make});
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    model = cursor.getString(0);
                    model_list.add(model);
                    cursor.moveToNext();
                }

                cursor.close();
                //myHelper.close();

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
                        //List<Integer> year_list = singleton.updateYears(Model);
                        List<Integer> year_list = new ArrayList<>();


                        Integer year;
                        Cursor cursor = myDataBase.rawQuery("select distinct year from DB where model = ? and make = ?order by year asc", new String[]{Model,Make});
                        cursor.moveToFirst();
                        while(!cursor.isAfterLast()){
                            year = cursor.getInt(0);
                            year_list.add(year);
                            cursor.moveToNext();
                        }

                        cursor.close();
                        //myHelper.close();



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
                                final String Year = parent.getSelectedItem().toString();
                                List<String> disp_list = new ArrayList<>();


                                String displAndMode;
                                Cursor cursor = myDataBase.rawQuery("select distinct drive, trany, displ  from DB where model = ? " +
                                                "and make = ?" +
                                                "and year = ?",
                                        new String[]{Model,Make,Year});
                                cursor.moveToFirst();
                                while(!cursor.isAfterLast()){
                                    displAndMode = (cursor.getString(0))+","+(cursor.getString(1)+","+(cursor.getString(2)));
                                    disp_list.add(displAndMode);
                                    cursor.moveToNext();
                                }

                                cursor.close();

                                ArrayAdapter<String> displ_adapter = new ArrayAdapter<>(
                                        AddCar.this, android.R.layout.simple_dropdown_item_1line, disp_list);
                                Spinner Displ_spinner = (Spinner) findViewById(R.id.ID_drop_down_dspl);
                                Displ_spinner.setAdapter(displ_adapter);


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
                    String CarYear = Year_spinner.getSelectedItem().toString();
                    String CityAndHighway = Displ_spinner.getSelectedItem().toString();

                    List<String> city08_highway_08 = new ArrayList<>();
                    String CityHighway = new String();
                    String[] DTDparts = CityAndHighway.split(",");
                    String drive = DTDparts[0];
                    String trany = DTDparts[1];
                    String displ = DTDparts[2];
                    Cursor cursor = myDataBase.rawQuery("select city08, highway08, fuelType  from DB where " +
                                    "make = ? " +
                                    "and model = ?" +
                                    "and year = ?" +
                                    "and drive = ?" +
                                    "and trany = ?" +
                                    "and displ = ?",
                            new String[]{CarMake,CarModel,CarYear,drive,trany,displ});
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()){
                        Toast.makeText(AddCar.this,"first fuelType",Toast.LENGTH_LONG).show();
                        CityHighway = (cursor.getString(0))+","+(cursor.getString(1)+","+(cursor.getString(2)));
                        city08_highway_08.add(CityHighway);
                        cursor.moveToNext();
                    }

                    cursor.close();
                    String firstCityHighway = city08_highway_08.get(0);
                    String[] splitCityHighway = firstCityHighway.split(",");

                    int city = Integer.parseInt(splitCityHighway[0]);
                    int highWay = Integer.parseInt(splitCityHighway[1]);
                    String fuelType = splitCityHighway[2];
                    int CarYearFromString = Integer.parseInt(CarYear);
//                    int city = singleton.getCityData(CityAndHighway);
//                    int highWay = singleton.getHwayData(CityAndHighway);
//                    String fuelType = singleton.getFuelType(CityAndHighway);
//
//                    int CarYear = Integer.parseInt(Year_spinner.getSelectedItem().toString());
                    if (!CarName.matches("") && !CarMake.matches("") && !CarModel.matches("") && CarYearFromString > 0) {
                        Vehicle userInput = new Vehicle(CarName, CarMake, CarModel, CarYearFromString,city,highWay,fuelType);

                        if (singleton.checkEdit_car() == 1) {
                            VehicleList.set(position, userInput);
                            singleton.setVehiclesList(VehicleList);
                            singleton.userFinishEdit_car();
                            String userInputNewCarName = userInput.getName();
                            singleton.UserEnterNewCarName(userInputNewCarName,VehicleNameToBeEdit);
                            Intent userEditCar = DisplayCarList.makeIntent(AddCar.this);
                            startActivity(userEditCar);
                        } else {
                            VehicleList.add(userInput);
                            singleton.setVehiclesList(VehicleList);

                            singleton.setUserPickVehicleItem(userInput);

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
//        Intent goBackToDisplayCar = DisplayCarList.makeIntent(AddCar.this);
//        startActivity(goBackToDisplayCar);
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddCar.class);
    }
}
