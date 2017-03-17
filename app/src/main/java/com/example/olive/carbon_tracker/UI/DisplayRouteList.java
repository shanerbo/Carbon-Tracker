package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Journey;
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

import static com.example.olive.carbon_tracker.R.string.year;

public class DisplayRouteList extends AppCompatActivity {
    private List<Route> RouteList = new ArrayList<Route>();
    Singleton singleton  = Singleton.getInstance();
    String currentRouteName;
    Vehicle vehicle = singleton.getVehicle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_route_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ChooseRoute);
        setSupportActionBar(toolbar);
        RouteList = singleton.getRouteList();
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
                singleton.setEditPosition(position);
                singleton.userEditRoute();
                startActivityForResult(EditIntent,0);
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
            TextView RouteName = (TextView)itemView.findViewById(R.id.CarNameWithimage);
            RouteName.setText("Name: "+currentRoute.getName());
            TextView RouteCityDst = (TextView)itemView.findViewById(R.id.CarMakeWithimage);
            RouteCityDst.setText("Distance in City: " + currentRoute.getCityDistance()+" KM");
            TextView RouteHwayDst = (TextView)itemView.findViewById(R.id.CarModelWithimage);
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

        if(singleton.checkTransportationMode() == 1) { // Walk/Bike
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();
            createNewJourney(cityDistance,HwyDistance,totalCO2, 1);
        } else if(singleton.checkTransportationMode() == 2){ //Bus
            totalCO2 = (cityDistance+HwyDistance)*0.089;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();
            createNewJourney(cityDistance,HwyDistance,totalCO2, 2);
        } else if(singleton.checkTransportationMode() == 3){ //Skytrain
            totalCO2 = (cityDistance+HwyDistance)*0.033;
            String TotalCO2 = String.format("%.2f", totalCO2);
            Toast.makeText(getApplicationContext(), "You have produced: "+ TotalCO2 +"kg of CO2", Toast.LENGTH_SHORT).show();
            createNewJourney(cityDistance,HwyDistance,totalCO2, 3);
        } else {
            singleton.getVehicle().setCityDistance(cityDistance);
            singleton.getVehicle().setHwyDistance(HwyDistance);

            int cityConsume = singleton.getVehicle().getCity08();
            int HwyConsume = singleton.getVehicle().getHighway08();
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

//    public void onBackPressed() {
//        Intent goBackToDisplayCar = DisplayCarList.makeIntent(DisplayRouteList.this);
//        startActivity(goBackToDisplayCar);
//    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, DisplayRouteList.class);
    }

    private void createNewJourney(int cityDistance,int hwyDistance,double co2, int TransMode){
        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        Calendar calendar = new Calendar();

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

        Toast.makeText(getApplicationContext(),"MONTH "+simpleDateFormat.format(date).toUpperCase(),Toast.LENGTH_SHORT).show();
       // System.out.println("DAY "+simpleDateFormat.format(date).toUpperCase());

        simpleDateFormat = new SimpleDateFormat("MMMM");
       // System.out.println("MONTH "+simpleDateFormat.format(date).toUpperCase());

        simpleDateFormat = new SimpleDateFormat("YYYY");
     //   System.out.println("YEAR "+simpleDateFormat.format(date).toUpperCase());

        String day =   singleton.getUserDay();
        String month =  singleton.getUserMonth();
        String year =  singleton.getUserYear();
        DecimalFormat Format = new DecimalFormat("#.##");
        double CO2 = Double.valueOf(Format.format(co2));
        String VehicleName = "";
        switch(TransMode){
            case 0:
                VehicleName = vehicle.getName();
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
        Journey journey = new Journey(day+"/"+month+"/"+year,currentRouteName,(cityDistance+hwyDistance), VehicleName, CO2);

        if (singleton.isEditingJourney()) {
            singleton.changeJourney(journey);
        } else {
            singleton.addUserJourney(journey);
        }
    }
}


