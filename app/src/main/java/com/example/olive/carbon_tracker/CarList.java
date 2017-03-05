package com.example.olive.carbon_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CarList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        populateListView();
        setupAddCarButton();
    }

    private void populateListView() {
        String[] my_car_list = {"Toyota", "Dodge", "BMW"}; //TODO sample car list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
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

                Intent gotoAddCar = new Intent(CarList.this, AddCar.class);
                //gotoAddCar.putExtra("my parameter name", 42);
                //startActivityForResult(gotoAddCar, 777);
                startActivity(gotoAddCar);

            }
        });
    }
}
