package com.example.olive.carbon_tracker.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Car;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.List;

public class CarList extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_ADD = 777;
    private static final int ACTIVITY_RESULT_EDIT = 321;
    List<String> my_car_list = new ArrayList<>();
    //private Car myCar = new Car("1", "2", "3", 4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        populateListView();
        setupAddCarButton();
        RegisterLongClick();
    }

    private void populateListView() {

        //List<Vehicle> car_list = Singleton.getCurrInstance().getVehicle();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.car_list_listview, my_car_list);
        ListView list = (ListView) findViewById(R.id.ID_Car_List);
        list.setAdapter(adapter);
    }

    private void setupAddCarButton() {
        Button AddCar = (Button) findViewById(R.id.ID_add_new_car_button);
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CarList.this, "Enter your new car's info here", Toast.LENGTH_LONG).show();

                Intent gotoAddCar = new Intent(CarList.this, com.example.olive.carbon_tracker.UI.AddCar.class);
                gotoAddCar.putExtra("my parameter name", 42);
                startActivityForResult(gotoAddCar, ACTIVITY_RESULT_ADD);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED)
            return;

        switch(requestCode){
            case ACTIVITY_RESULT_ADD: //add new car
                if(resultCode == Activity.RESULT_OK){
                    String CarName = data.getStringExtra("Car Name");
                    String CarMake = data.getStringExtra("Car Make");
                    String CarModel = data.getStringExtra("Car Model");
                    int CarYear = data.getIntExtra("Car Year", 0);
                    Car adding = new Car(CarName, CarMake, CarModel, CarYear);
                    my_car_list.add(adding.getName() +": "+ adding.getMake() +" "+ adding.getModel() +" ("+ adding.getYear() +")");
                    populateListView();
                    break;
                }
            case ACTIVITY_RESULT_EDIT: //edit or delete car
                if(resultCode == Activity.RESULT_OK){ //edit car
                    String CarName = data.getStringExtra("Car Name");
                    String CarMake = data.getStringExtra("Car Make");
                    String CarModel = data.getStringExtra("Car Model");
                    int CarYear = data.getIntExtra("Car Year", 0);
                    int CarPos = data.getIntExtra("Car Position", 0);
                    Car adding = new Car(CarName, CarMake, CarModel, CarYear);
                    my_car_list.set(CarPos,
                            adding.getName() +": "+ adding.getMake() +" "+ adding.getModel() +" ("+ adding.getYear() +")");
                    populateListView();
                    break;
                }
                else{ //delete car
                    int CarPos = data.getIntExtra("Car Position", 0);
                    my_car_list.remove(CarPos);
                    populateListView();
                    break;
                }


        }

    }


    private void RegisterLongClick() {
        ListView list = (ListView) findViewById(R.id.ID_Car_List);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CarList.this, EditOrDeleteCar.class);
                //Singleton Car = Singleton.getCurrInstance();
                //Car.getVehicle(position);
                intent.putExtra("Car Position", position);
                intent.putExtra("Car Name", my_car_list.get(position));
//                intent.putExtra("Car Name", myCar.getName());
//                intent.putExtra("Car Make", myCar.getMake());
//                intent.putExtra("Car Model", myCar.getModel());
//                intent.putExtra("Car Year", myCar.getYear());
                startActivityForResult(intent, ACTIVITY_RESULT_EDIT);

                return true;
            }
        });
    }
}
