package com.example.olive.carbon_tracker.UI;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.SuperUltraInfoDataBaseHelper;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.Singleton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayRouteList extends AppCompatActivity {
    private List<Route> RouteList = new ArrayList<Route>();
    Singleton singleton  = Singleton.getInstance();
    String currentRouteName;
    Vehicle _vehicle = singleton.getVehicle();
    private SQLiteDatabase myDataBase;
    private SQLiteDatabase RouteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SuperUltraInfoDataBaseHelper RouteDBhelper = new SuperUltraInfoDataBaseHelper(this);
        RouteDB = RouteDBhelper.getWritableDatabase();

        setContentView(R.layout.activity_display_route_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ChooseRoute);
        setSupportActionBar(toolbar);
        //RouteList = singleton.getRouteList();
        AddRoute();
        EditRoute();
        showAllRoute();
        UserChooseRoute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showAllRoute();
    }

    private void EditRoute() {
        ListView RouteInfo = (ListView) findViewById(R.id.ROUTES);
        RouteInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent EditIntent = AddNewRoute.makeIntent(DisplayRouteList.this);
                long DB_id = RouteList.get(position).getRouteDBId();
                singleton.setEditPosition_Route(DB_id);
                singleton.userEditRoute();
                startActivityForResult(EditIntent,0);
                finish();
                return true;
            }
        });
    }

    private void UserChooseRoute() {
        ListView CarInfo = (ListView) findViewById(R.id.ROUTES);
        CarInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Route userPickRoute = RouteList.get(position);
                currentRouteName = userPickRoute.getName();

                calculateCO2(userPickRoute);

            }


        });
    }

    private void AddRoute() {
        FloatingActionButton addRoute = (FloatingActionButton) findViewById(R.id.AddRoute);
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddIntent = AddNewRoute.makeIntent(DisplayRouteList.this);
                singleton.userAddRoute();
                startActivityForResult(AddIntent,1);
                finish();
            }
        });

    }

    private void showAllRoute() {
        List<Route> RouteListFromDB = new ArrayList<Route>();
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);
        Cursor cursor = myDataBase.rawQuery("select " +
                "RouteName," +
                "RouteCityDistance," +
                "RouteHwyDistance," +
                "RouteTotalDistance, " +
                "_id " +
                "from RouteInfoTable",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String RouteName = cursor.getString(0);
            int RouteCityDistance = cursor.getInt(1);
            int RouteHwyDistance = cursor.getInt(2);
            int RouteToatalDistance = cursor.getInt(3);
            long routeDBId = cursor.getLong(cursor.getColumnIndex("_id"));
            Route tempRoute = new Route(RouteName,RouteCityDistance,RouteHwyDistance,RouteToatalDistance,routeDBId);
            RouteListFromDB.add(tempRoute);
            cursor.moveToNext();
        }
        cursor.close();
        RouteList = RouteListFromDB;
        ArrayAdapter<Route> adapter = new myArrayAdapter();
        ListView RoutesShown = (ListView)findViewById(R.id.ROUTES);
        RoutesShown.setAdapter(adapter);
    }

    private class myArrayAdapter extends ArrayAdapter<Route> {
        public myArrayAdapter(){
            super(DisplayRouteList.this, R.layout.single_element_route_list, RouteList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.single_element_route_list, parent, false);
            }
            Route currentRoute = RouteList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.RouteImage);
            imageView.setImageResource(currentRoute.getIconId());
            TextView RouteName = (TextView)itemView.findViewById(R.id.StartingDate);
            RouteName.setText("Name: "+currentRoute.getName());
            TextView RouteCityDst = (TextView)itemView.findViewById(R.id.EndingDate);
            RouteCityDst.setText("Distance in City: " + currentRoute.getCityDistance()+" KM");
            TextView RouteHwayDst = (TextView)itemView.findViewById(R.id.IndElecUsage);
            RouteHwayDst.setText("Distance in HighWay: "+ currentRoute.getHighwayDistance()+" KM");
            TextView RouteTotalDst = (TextView)itemView.findViewById(R.id.totalDistanceWImage);
            RouteTotalDst.setText("Total Distance: " + currentRoute.getTotalDistance()+" KM");
            return itemView;
        }
    }

    public void calculateCO2(Route userInput){
        int cityDistance = userInput.getCityDistance();
        int HwyDistance = userInput.getHighwayDistance();
        double totalCO2 = 0;

        if (singleton.checkTransportationMode() == 1) { // Walk/Bike
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();

            String day =   singleton.getUserDay();
            String month =  singleton.getUserMonth();
            String year =  singleton.getUserYear();
            String date = day+"/"+month+"/"+year;

            ContentValues cv = new ContentValues();
            cv.put(SuperUltraInfoDataBaseHelper.Journey_Date,date);

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarName,"Walk/Bike");
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMode,"Walk/Bike");

            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteId, userInput.getRouteDBId());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteName, userInput.getName());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteCityDist, userInput.getCityDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteHwyDist, userInput.getHighwayDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteTotalDist, userInput.getTotalDistance());

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CO2Emitted, totalCO2);

            long idPassBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Journey_Table,null,cv);
            RouteDB.close();

            createNewJourney(cityDistance,HwyDistance,totalCO2, 1);
        }
        else if (singleton.checkTransportationMode() == 2){ //Bus
            totalCO2 = (cityDistance+HwyDistance)*0.089;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();


            String day =   singleton.getUserDay();
            String month =  singleton.getUserMonth();
            String year =  singleton.getUserYear();
            String date = day+"/"+month+"/"+year;

            ContentValues cv = new ContentValues();
            cv.put(SuperUltraInfoDataBaseHelper.Journey_Date,date);

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarName,"Bus");
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMode,"Bus");

            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteId, userInput.getRouteDBId());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteName, userInput.getName());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteCityDist, userInput.getCityDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteHwyDist, userInput.getHighwayDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteTotalDist, userInput.getTotalDistance());

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CO2Emitted, totalCO2);

            long idPassBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Journey_Table,null,cv);
            RouteDB.close();

            createNewJourney(cityDistance,HwyDistance,totalCO2, 2);
        }
        else if (singleton.checkTransportationMode() == 3){ //Skytrain
            totalCO2 = (cityDistance+HwyDistance)*0.02348;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();

            String day =   singleton.getUserDay();
            String month =  singleton.getUserMonth();
            String year =  singleton.getUserYear();
            String date = day+"/"+month+"/"+year;

            ContentValues cv = new ContentValues();
            cv.put(SuperUltraInfoDataBaseHelper.Journey_Date,date);

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarName,"Skytrain");
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMode,"Skytrain");

            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteId, userInput.getRouteDBId());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteName, userInput.getName());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteCityDist, userInput.getCityDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteHwyDist, userInput.getHighwayDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteTotalDist, userInput.getTotalDistance());

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CO2Emitted, totalCO2);

            long idPassBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Journey_Table,null,cv);
            RouteDB.close();

            createNewJourney(cityDistance,HwyDistance,totalCO2, 3);
        }
        else {
            singleton.getVehicle().setCityDistance(cityDistance);
            singleton.getVehicle().setHwyDistance(HwyDistance);

            double cityConsume = singleton.getVehicle().getCity08();
            double HwyConsume = singleton.getVehicle().getHighway08();
            String fuelType = singleton.getVehicle().getFuelType();
            double fuelCost;
            if (fuelType.toLowerCase().matches("diesel")) {
                fuelCost = 10.16;
            } else if (fuelType.toLowerCase().matches("electricity")) {
                fuelCost = 0;
            } else {
                fuelCost = 8.89;
            }
            double cityGas = (cityDistance * 0.621371192 / cityConsume);
            double hwyGas = HwyDistance * 0.621371192 / HwyConsume;
            double totalGas = cityGas + hwyGas;
            totalCO2 = fuelCost * totalGas;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "The CO2 you produced: " + TotalCO2, Toast.LENGTH_LONG).show();


            String day =   singleton.getUserDay();
            String month =  singleton.getUserMonth();
            String year =  singleton.getUserYear();
            String date = day+"/"+month+"/"+year;

            ContentValues cv = new ContentValues();
            cv.put(SuperUltraInfoDataBaseHelper.Journey_Date,date);

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarId,_vehicle.getCarDBId());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarName, _vehicle.getName());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMode, "Car");
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarMake,_vehicle.getMake());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarModel,_vehicle.getModel());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarYear,_vehicle.getYear());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarCity,_vehicle.getCity08());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarHwy,_vehicle.getHighway08());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_CarFuelType,_vehicle.getFuelType());

            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteId, userInput.getRouteDBId());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteName, userInput.getName());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteCityDist, userInput.getCityDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteHwyDist, userInput.getHighwayDistance());
            cv.put(SuperUltraInfoDataBaseHelper.Journey_RouteTotalDist, userInput.getTotalDistance());

            cv.put(SuperUltraInfoDataBaseHelper.Journey_CO2Emitted, totalCO2);

            long idPassBack = RouteDB.insert(SuperUltraInfoDataBaseHelper.Journey_Table,null,cv);
            RouteDB.close();

            createNewJourney(cityDistance, HwyDistance, totalCO2, 0);
        }

        if (singleton.isEditingJourney()) {
            singleton.userFinishEditJourney();
            Intent userEditJourney = DisplayJourneyList.makeIntent(DisplayRouteList.this);
            startActivity(userEditJourney);
        } else {
            Intent ConfirmCar = MainMenu.makeIntent(DisplayRouteList.this);
            startActivity(ConfirmCar);
        }
        finish();
    }

    public void onBackPressed() {
        Intent goBackToDisplayCar = DisplayCarList.makeIntent(DisplayRouteList.this);
        startActivity(goBackToDisplayCar);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, DisplayRouteList.class);
    }

    private void createNewJourney(int cityDistance,int hwyDistance,double co2, int TransMode){
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        Calendar calendar = new Calendar();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Toast.makeText(getApplicationContext(),"MONTH "+simpleDateFormat.format(date).toUpperCase(),Toast.LENGTH_SHORT).show();

        String day =   singleton.getUserDay();
        String month =  singleton.getUserMonth();
        String year =  singleton.getUserYear();
        DecimalFormat Format = new DecimalFormat("#.##");
        double CO2 = Double.valueOf(Format.format(co2));
        String VehicleName = "";
        switch(TransMode){
            case 0:
                VehicleName = _vehicle.getName();
                break;
            case 1:
                VehicleName = "Walk/Bike";
                break;
            case 2:
                VehicleName = "Bus";
                break;
            case 3:
                VehicleName = "Skytrain";
                break;
        }

        long temp = 9999;
        Journey journey = new Journey(day+"/"+month+"/"+year,VehicleName,currentRouteName,(cityDistance+hwyDistance), VehicleName, CO2, temp);
        if (singleton.isEditingJourney()) {
            singleton.changeJourney(journey);
        } else {
            singleton.addUserJourney(journey);
        }
    }
}


