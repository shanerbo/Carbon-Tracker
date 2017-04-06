package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the list of cars the user has entered
 */
public class DisplayCarList extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();
    private List<Vehicle> VehicleList = new ArrayList<>();

    private SQLiteDatabase myDataBase;

    public static Intent makeIntent(Context context) {
        return new Intent(context, DisplayCarList.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_display_car_list);
        //VehicleList = singleton.getVehicleList();
        showAllCar();
        AddNewCar();
        EditCar();
        UserChooseCar();
        setToolBar();
    }

    private void UserChooseCar() {
        ListView CarInfo = (ListView) findViewById(R.id.ID_Car_List);
        CarInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ConfirmCar = DisplayRouteList.makeIntent(DisplayCarList.this);
                Vehicle userPickVehicle = VehicleList.get(position);
                singleton.setUserPickVehicleItem(userPickVehicle);

                startActivity(ConfirmCar);
                finish();
            }


        });
    }

    private void EditCar() {
        ListView CarInfo = (ListView) findViewById(R.id.ID_Car_List);
        CarInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent EditIntent = AddCar.makeIntent(DisplayCarList.this);
                long DB_id = VehicleList.get(position).getVehicleDBId();

                singleton.setEditPosition_car(DB_id);
                singleton.userEditRoute_car();

                startActivityForResult(EditIntent,0);
                finish();
                return true;
            }
        });
    }

    private void AddNewCar() {
        final FloatingActionButton AddCar = (FloatingActionButton) findViewById(R.id.ID_add_new_car_button);
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddIntent = com.example.olive.carbon_tracker.UI.AddCar.makeIntent(DisplayCarList.this);
                singleton.userAddVehicle();
                startActivityForResult(AddIntent,1);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showAllCar();
    }

    private void showAllCar() {
        List<Vehicle> VehicleListFromDB = new ArrayList<>();
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);
        Cursor cursor = myDataBase.rawQuery("select CarName," +
                "CarMake," +
                "CarModel," +
                "CarYear, " +
                "CarCity08, " +
                "CarHwy08," +
                "CarFuelType, " +
                "_id," +
                "CarImage from CarInfoTable",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String CarName = cursor.getString(0);
            String CarMake = cursor.getString(1);
            String CarModel = cursor.getString(2);
            int CarYear = cursor.getInt(3);
            double CarCity08 = cursor.getDouble(4);
            double CarHwy08 = cursor.getDouble(5);
            String fuelType = cursor.getString(6);
            int CarImageId = cursor.getInt(8);
            long carDBId = cursor.getLong(cursor.getColumnIndex("_id"));
            Vehicle tempVehicle = new Vehicle(CarName,CarMake,CarModel,
                    CarYear,CarCity08,CarHwy08,fuelType,carDBId,CarImageId);
            VehicleListFromDB.add(tempVehicle);
            cursor.moveToNext();
        }
        cursor.close();
        myDataBase.close();
        VehicleList = VehicleListFromDB;
        ArrayAdapter<Vehicle> adapter = new mArrayAdapter();
        ListView list = (ListView) findViewById(R.id.ID_Car_List);


        list.setAdapter(adapter);
    }

    public void onBackPressed() {
        Intent goBackToModeSelect = SelectTransportationModeAndDate.makeIntent(DisplayCarList.this);
        startActivity(goBackToModeSelect);
    }

    private class mArrayAdapter extends ArrayAdapter<Vehicle> {
        private mArrayAdapter() {
            super(DisplayCarList.this, R.layout.singe_element_car_list, VehicleList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.singe_element_car_list, parent, false);
            }
            Vehicle currentVehicle = VehicleList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.CarImage);
            imageView.setImageResource(currentVehicle.getImageID());
            TextView carName = (TextView) itemView.findViewById(R.id.StartingDate);
            carName.setText("Car Name: " + currentVehicle.getName());
            TextView carMake = (TextView) itemView.findViewById(R.id.EndingDate);
            carMake.setText("Car Make: " + currentVehicle.getMake());
            TextView carModel = (TextView) itemView.findViewById(R.id.IndElecUsage);
            carModel.setText("Car Model: " + currentVehicle.getModel());
            TextView carYear = (TextView) itemView.findViewById(R.id.CarYearWImage);
            carYear.setText("Car Year: " + currentVehicle.getYear());
            return itemView;
        }
    }

    private void setToolBar(){
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_car);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.tool_change_unit){
            if(singleton.checkCO2Unit() == 0)
                singleton.humanRelatableUnit();
            else
                singleton.originalUnit();
            saveCO2UnitStatus(singleton.checkCO2Unit());
            Toast.makeText(getApplicationContext(), "CO2 unit has been changed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.tool_about){
            startActivity(new Intent(DisplayCarList.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCO2UnitStatus(int status) {
        SharedPreferences prefs = this.getSharedPreferences("CO2Status", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("CO2 status", status);
        editor.apply();
    }
}
