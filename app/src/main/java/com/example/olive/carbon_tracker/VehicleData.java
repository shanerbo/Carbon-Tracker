package com.example.olive.carbon_tracker;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class VehicleData {
    public static final int VEHICLE_MAKE_TOKEN = 0;
    public static final int VEHICLE_MODEL_TOKEN = 1;
    public static final int VEHICLE_YEAR_TOKEN = 2;

    List<String> vehicleMakeArray = new ArrayList<>();
    List<String> vehicleModelArray = new ArrayList<>();
    List<Integer> vehicleYearArray = new ArrayList<>();
    List<Vehicle> vehicleDataArray = new ArrayList<>();

    List<String> uniqueVehicleMakeArray = new ArrayList<>();

    //TODO Find out the information we need and extract from
    // TODO the excel file as needed
    public void ExtractVehicleData(Context context) {

        InputStream is = context.getResources().openRawResource(R.raw.vehicledata);
        //buffer reader works good for line by line
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = " ";
        try {
            //step over headers
            reader.readLine();
            int skipFirstLine = 0;
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity", "Line: " + line);
                //Split by '@'
                String[] tokens = line.split("@");
                //Read data
                Vehicle vehicle = new Vehicle();
                if (skipFirstLine > 0) {

                    vehicle.setMake(tokens[VEHICLE_MAKE_TOKEN]);
                    vehicleMakeArray.add(tokens[VEHICLE_MAKE_TOKEN]);

                    vehicle.setModel(tokens[VEHICLE_MODEL_TOKEN]);
                    vehicleModelArray.add(tokens[VEHICLE_MODEL_TOKEN]);

                    vehicle.setYear(Integer.parseInt(tokens[VEHICLE_YEAR_TOKEN]));
                    vehicleYearArray.add(Integer.parseInt(tokens[VEHICLE_YEAR_TOKEN]));

                    vehicleDataArray.add(vehicle);
                }
                skipFirstLine++;
                Log.d("MyActivity", "just created:" + vehicle);
            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on  line" + line, e);
            e.printStackTrace();
        }

    }

    public List<String> getVehicleMakeArray(){
        return vehicleMakeArray;
    }

    public  List<String> getVehicleModelArray(){
        return  vehicleModelArray;
    }

    public  List<Integer> getVehicleYearArray(){
        return  vehicleYearArray;
    }

    public List<Vehicle>  getVehicleDataArray(){
        return vehicleDataArray;
    }



    public List<String> getUniqueVehicleMakeArray(){
        for(int i =0;i<vehicleMakeArray.size();i++){
            String currentMake= vehicleMakeArray.get(i);

            boolean found = checkForUniqueVehicles(currentMake);

            if(found == false){
                uniqueVehicleMakeArray.add(currentMake);
            }
        }
        return uniqueVehicleMakeArray;
    }

    public boolean checkForUniqueVehicles(String currentMake){
        boolean found = false;
        for(int i =0;i<uniqueVehicleMakeArray.size();i++){
            if(currentMake.equals(uniqueVehicleMakeArray.get(i))){
                found= true;
                break;
            }
        }
        return found;
    }



}
