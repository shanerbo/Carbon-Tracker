package com.example.olive.carbon_tracker.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class VehicleData {
    List<Integer> saveCity = new ArrayList<>();
    List<Integer> saveHwy = new ArrayList<>();
    List<String> saveFuelType = new ArrayList<>();





    public static final int VEHICLE_MAKE_TOKEN = 0;
    public static final int VEHICLE_MODEL_TOKEN = 1;
    public static final int VEHICLE_YEAR_TOKEN = 2;
    public static final int VEHICLE_CITY08_TOKEN = 3;
    public static final int VEHICLE_HIGHWAY08_TOKEN = 4;
    public static final int VEHICLE_FUELTYPE_TOKEN = 5;
    public static final int VEHICLE_DISPL_TOKEN = 6;
    public static final int VEHICLE_TRANY_TOKEN = 7;

    List<String> vehicleMakeArray = new ArrayList<>();
    List<String> vehicleModelArray = new ArrayList<>();
    List<Integer> vehicleYearArray = new ArrayList<>();
    List<Integer> vehicleCity08Array = new ArrayList<>();
    List<Integer> vehicleHighway08 = new ArrayList<>();
    List<String> vehicleFuelTypeArray = new ArrayList<>();
    List<Double> vehicleDisplArray = new ArrayList<>();
    List<String> vehicleTranyArray = new ArrayList<>();
    List<Vehicle> vehicleDataArray = new ArrayList<>();

    List<String> uniqueVehicleMakeArray = new ArrayList<>();
    List<Double> uniqueVehicleDisplArray = new ArrayList<>();
    List<Integer> uniqueVehicleYearArray = new ArrayList<>();
    List<Double> uniqueDisplArray = new ArrayList<>();


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
                //   Log.d("MyActivity", "Line: " + line);
                //Split by '@'
                String[] tokens = line.split("@");
                //Read data
                Vehicle vehicle = new Vehicle();
                if (skipFirstLine > 0) {

//                    vehicle.setMake(tokens[VEHICLE_MAKE_TOKEN]);
                    vehicleMakeArray.add(tokens[VEHICLE_MAKE_TOKEN]);

//                    vehicle.setModel(tokens[VEHICLE_MODEL_TOKEN]);
                    vehicleModelArray.add(tokens[VEHICLE_MODEL_TOKEN]);

//                    vehicle.setYear(Integer.parseInt(tokens[VEHICLE_YEAR_TOKEN]));
                    vehicleYearArray.add(Integer.parseInt(tokens[VEHICLE_YEAR_TOKEN]));

                    vehicleCity08Array.add(Integer.parseInt(tokens[VEHICLE_CITY08_TOKEN]));

                    vehicleHighway08.add(Integer.parseInt(tokens[VEHICLE_HIGHWAY08_TOKEN]));

                    vehicleFuelTypeArray.add(tokens[VEHICLE_FUELTYPE_TOKEN]);

                    vehicleDisplArray.add(Double.parseDouble(tokens[VEHICLE_DISPL_TOKEN]));

                    vehicleTranyArray.add(tokens[VEHICLE_TRANY_TOKEN]);


//                    vehicleDataArray.add(vehicle);
                }
                skipFirstLine++;
                //    Log.d("MyActivity", "just created:" );
            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on  line" + line, e);
            e.printStackTrace();
        }

    }

    public List<String> getVehicleMakeArray() {
        return vehicleMakeArray;
    }

    public List<String> getVehicleModelArray() {
        return vehicleModelArray;
    }

    public List<Integer> getVehicleYearArray() {
        return vehicleYearArray;
    }

    public List<Vehicle> getVehicleDataArray() {
        return vehicleDataArray;
    }


    public List<String> getUniqueVehicleMakeArray() {
        for (int i = 0; i < vehicleMakeArray.size(); i++) {
            String currentMake = vehicleMakeArray.get(i);

            boolean found = checkForUniqueVehicles(currentMake);

            if (found == false) {
                uniqueVehicleMakeArray.add(currentMake);
            }
        }
        return uniqueVehicleMakeArray;
    }

    public boolean checkForUniqueVehicles(String currentMake) {
        boolean found = false;
        for (int i = 0; i < uniqueVehicleMakeArray.size(); i++) {
            if (currentMake.equals(uniqueVehicleMakeArray.get(i))) {
                found = true;
                break;
            }
        }
        return found;
    }

    public List<String> getModelsForAMake(String vehicleMake) {
        List<String> getModelsForMakeArray = new ArrayList<>();
        for (int i = 0; i < vehicleMakeArray.size(); i++) {
            String currentVehicle = vehicleMakeArray.get(i);
            if (vehicleMake.equals(currentVehicle)) {
                String currentModel = vehicleModelArray.get(i);
                boolean found = checkForUniqueModels(currentModel, getModelsForMakeArray);
                if (found == false) {
                    getModelsForMakeArray.add(currentModel);
                }
//                Log.d("Make:  " + vehicleMake + " model added: " + vehicleModelArray.get(i) , "just created:" );
            }
        }

//        Log.d("make: "+ vehicleMake + " FIRST MODEL: " + getModelsForMakeArray.get(0), "just created:" );

        return getModelsForMakeArray;

    }

    public boolean checkForUniqueModels(String vehicleModel, List<String> getModelsForMakeArray) {

        boolean found = false;
        for (int i = 0; i < getModelsForMakeArray.size(); i++) {
            if (vehicleModel.equals(getModelsForMakeArray.get(i))) {
                found = true;
                break;
            }
        }
        return found;
    }


    public List<Integer> getYearsForAModel(String vehicleModel) {
        List<Integer> getYearsForModelArray = new ArrayList<>();
        for (int i = 0; i < vehicleModelArray.size(); i++) {
            String currentModel = vehicleModelArray.get(i);
            if (currentModel.equals(vehicleModel)) {
                int year = vehicleYearArray.get(i);
//                boolean found = uniqueVehicleYearArray(year, getYearsForModelArray);
                if (!getYearsForModelArray.contains(year)) {
                    getYearsForModelArray.add(year);
                }
            }
        }
        return getYearsForModelArray;
    }


//    public boolean uniqueVehicleYearArray(int year, List<Integer> getYearsForModelArray) {
//
//        boolean found = false;
//        for (int i = 0; i < getYearsForModelArray.size(); i++) {
//            if (year == getYearsForModelArray.get(i)) {
//                found = true;
//                break;
//            }
//        }
//        return found;
//    }
//
//    public boolean checkForUniqueYearVehicles(Integer currentYear) {
//        boolean found = false;
//        for (int i = 0; i < uniqueVehicleYearArray.size(); i++) {
//            if (currentYear.equals(uniqueVehicleYearArray.get(i))) {
//                found = true;
//                break;
//            }
//        }
//        return found;
//    }
//
//    public List<Integer> uniqueVehicleYearArray() {
//        for (int i = 0; i < vehicleYearArray.size(); i++) {
//            Integer currentYear = vehicleYearArray.get(i);
//            boolean found = checkForUniqueYearVehicles(currentYear);
//            if (found == false) {
//                uniqueVehicleYearArray.add(currentYear);
//            }
//        }
//        return uniqueVehicleYearArray;
//    }



    public List<String> getDisplForVehicle(String vehicleModel, int vehicleYear) {
        List<String> getDisplPlusTrans = new ArrayList<>();
        List<Integer> saveIndex = new ArrayList<>();
        restCityAndHway();
        int index = 0;
        for (int i = 0; i < vehicleModelArray.size(); i++) {
            String currentModel = vehicleModelArray.get(i);
            if (currentModel.equals(vehicleModel)) {
                for (int j = 0; j < vehicleYearArray.size(); j++) {
                    int year = vehicleYearArray.get(j);
                    if (vehicleYear == year &&
                            !getDisplPlusTrans.contains(vehicleDisplArray.get(i).toString() + "," +vehicleTranyArray.get(i))
                            && (vehicleModelArray.get(j)).equals(vehicleModel)) {
                        String displ = vehicleDisplArray.get(i).toString();
                        String trans = vehicleTranyArray.get(i);

                        getDisplPlusTrans.add(displ +  "," + trans);
                        saveCity.add(vehicleCity08Array.get(i));
                        saveHwy.add(vehicleHighway08.get(i));
                        saveFuelType.add(vehicleFuelTypeArray.get(i));
                    }
                }
            }
        }
        return getDisplPlusTrans;
    }

    public int getGiveCity(int position){
        return saveCity.get(position);
    }
    public int getGiveHway(int position){
        return saveHwy.get(position);
    }
    public String getGiveFuel(int position){
        return saveFuelType.get(position);
    }
    public void restCityAndHway(){
        saveCity.clear();
        saveHwy.clear();
        saveFuelType.clear();
    }

}




