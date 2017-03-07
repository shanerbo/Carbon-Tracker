package com.example.olive.carbon_tracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EditOrDeleteCar extends AppCompatActivity {
    public static final int REQUEST_CODE_EDIT_CAR = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_car);


        EditCar();
        DeleteCar();
    }

    private void EditCar() {
        Button edit = (Button) findViewById(R.id.EditButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String carName = intent.getStringExtra("Car Name");
                String carMake = intent.getStringExtra("Car Make");
                String carModel = intent.getStringExtra("Car Model");
                int carYear = intent.getIntExtra("Car Year", 0);

                Intent EditCar = new Intent(EditOrDeleteCar.this, AddCar.class);
                EditCar.putExtra("Car Name", carName);
                EditCar.putExtra("Car Make", carMake);
                EditCar.putExtra("Car Model", carModel);
                EditCar.putExtra("Car Year", carYear);
                Toast.makeText(EditOrDeleteCar.this, "Edit your car here", Toast.LENGTH_SHORT).show();
                startActivityForResult(EditCar, REQUEST_CODE_EDIT_CAR);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_EDIT_CAR){
            if(resultCode == Activity.RESULT_OK){
                String Car_Name = data.getStringExtra("Car Name");
                String Car_Make = data.getStringExtra("Car Make");
                String Car_Model = data.getStringExtra("Car Model");
                int Car_Year = data.getIntExtra("Car Year", 0);

                Intent saveData = new Intent();
                saveData.putExtra("Car Name", Car_Name);
                saveData.putExtra("Car Make", Car_Make);
                saveData.putExtra("Car Model", Car_Model);
                saveData.putExtra("Car Year", Car_Year);
                setResult(Activity.RESULT_OK, saveData);

                finish();
            }
        }
    }

    private void DeleteCar() {
        Button delete = (Button) findViewById(R.id.DeleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIntent = getIntent();
                int car_position = deleteIntent.getIntExtra("Car Position",0);

                Intent deleteCar = new Intent();
                deleteCar.putExtra("Car Position", car_position);
                setResult(Activity.RESULT_FIRST_USER,deleteCar);

                Toast.makeText(EditOrDeleteCar.this, "The selected car has been deleted.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
