package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.List;

public class CarList extends AppCompatActivity {
    private List<Vehicle> VehicleList = new ArrayList<Vehicle>();
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        VehicleList = singleton.getVehicleList();
        showAllCar();
        AddNewCar();
        EditCar();
        UserChooseCar();
    }

    private void UserChooseCar() {
        ListView CarInfo = (ListView) findViewById(R.id.ID_Car_List);
        CarInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ConfirmCar = DisplayRouteList.makeIntent(CarList.this);
                Vehicle userPickVehicle = VehicleList.get(position);
                singleton.setUserPickVehicleItem(userPickVehicle);

                startActivity(ConfirmCar);
            }


        });
    }

    private void EditCar() {
        ListView CarInfo = (ListView)findViewById(R.id.ID_Car_List);
        CarInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent EditIntent = AddCar.makeIntent(CarList.this);
                singleton.setEditPosition_car(position);
                singleton.userEditRoute_car();
                startActivityForResult(EditIntent,0);//case 1 means add route
                //case 2 means edit route
                return true;
            }
        });
    }


    private void AddNewCar() {
        //FloatingActionButton AddCar = (FloatingActionButton) findViewById(R.id.ID_button_OKAdd);
        final Button AddCar = (Button) findViewById(R.id.ID_add_new_car_button);
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddIntent = com.example.olive.carbon_tracker.UI.AddCar.makeIntent(CarList.this);
                singleton.userAddVehicle();
                startActivityForResult(AddIntent,1);//case 1 means add route
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showAllCar();
    }


    private void showAllCar() {
        //List<Vehicle> car_list = Singleton.getCurrInstance().getVehicle();
        ArrayAdapter<Vehicle> adapter = new mArrayAdapter();
        ListView list = (ListView) findViewById(R.id.ID_Car_List);
        list.setAdapter(adapter);
    }
    private class mArrayAdapter extends ArrayAdapter<Vehicle> {
        public mArrayAdapter(){
            super(CarList.this, R.layout.singe_element_car_list,VehicleList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.singe_element_car_list, parent, false);
            }
            Vehicle currentVehicle = VehicleList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.RouteImage);
            imageView.setImageResource(currentVehicle.getIconId());
            TextView carName = (TextView)itemView.findViewById(R.id.CarNameWithimage);
            carName.setText("Car Name: "+currentVehicle.getName());
            TextView carMake = (TextView)itemView.findViewById(R.id.CarMakeWithimage);
            carMake.setText("Car Make: " + currentVehicle.getMake());
            TextView carModel = (TextView)itemView.findViewById(R.id.CarModelWithimage);
            carModel.setText("Car Model: "+ currentVehicle.getModel());
            TextView carYear = (TextView)itemView.findViewById(R.id.CarYearWImage);
            carYear.setText("Car Year: " + currentVehicle.getYear());
            return itemView;
        }
    }
    public static Intent makeIntent(Context context) {
        return new Intent(context, CarList.class);
    }
}
