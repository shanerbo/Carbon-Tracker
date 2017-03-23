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

/**
 * extracts data from excel
 */
public class VehicleData {
    private List<Integer> saveCity = new ArrayList<>();
    private List<Integer> saveHwy = new ArrayList<>();
    private List<String> saveFuelType = new ArrayList<>();

    private  static final int VEHICLE_MAKE_TOKEN = 0;
    private  static final int VEHICLE_MODEL_TOKEN = 1;
    private  static final int VEHICLE_YEAR_TOKEN = 2;
    private  static final int VEHICLE_CITY08_TOKEN = 3;
    private  static final int VEHICLE_HIGHWAY08_TOKEN = 4;
    private  static final int VEHICLE_FUELTYPE_TOKEN = 5;
    private  static final int VEHICLE_DISPL_TOKEN = 6;
    private  static final int VEHICLE_TRANY_TOKEN = 7;

    private List<String> vehicleMakeArray = new ArrayList<>();
    private List<String> vehicleModelArray = new ArrayList<>();
    private List<Integer> vehicleYearArray = new ArrayList<>();
    private List<Integer> vehicleCity08Array = new ArrayList<>();
    private List<Integer> vehicleHighway08 = new ArrayList<>();
    private List<String> vehicleFuelTypeArray = new ArrayList<>();
    private List<Double> vehicleDisplArray = new ArrayList<>();
    private List<String> vehicleTranyArray = new ArrayList<>();
    private List<Vehicle> vehicleDataArray = new ArrayList<>();

    private List<String> uniqueVehicleMakeArray = new ArrayList<>();
    private List<Double> uniqueVehicleDisplArray = new ArrayList<>();
    private List<Integer> uniqueVehicleYearArray = new ArrayList<>();
    private List<Double> uniqueDisplArray = new ArrayList<>();


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
                String[] tokens = line.split("@");
                //Read data
                Vehicle vehicle = new Vehicle();
                if (skipFirstLine > 0) {

                    vehicleMakeArray.add(tokens[VEHICLE_MAKE_TOKEN]);

                    vehicleModelArray.add(tokens[VEHICLE_MODEL_TOKEN]);

                    vehicleYearArray.add(Integer.parseInt(tokens[VEHICLE_YEAR_TOKEN]));

                    vehicleCity08Array.add(Integer.parseInt(tokens[VEHICLE_CITY08_TOKEN]));

                    vehicleHighway08.add(Integer.parseInt(tokens[VEHICLE_HIGHWAY08_TOKEN]));

                    vehicleFuelTypeArray.add(tokens[VEHICLE_FUELTYPE_TOKEN]);

                    vehicleDisplArray.add(Double.parseDouble(tokens[VEHICLE_DISPL_TOKEN]));

                    vehicleTranyArray.add(tokens[VEHICLE_TRANY_TOKEN]);


                }
                skipFirstLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public List<String> getUniqueVehicleMakeArray() {
        for (int i = 0; i < vehicleMakeArray.size(); i++) {
            String currentMake = vehicleMakeArray.get(i);
            if (!uniqueVehicleMakeArray.contains(currentMake)) {
                uniqueVehicleMakeArray.add(currentMake);
            }
        }
        return uniqueVehicleMakeArray;
    }


    public List<String> getModelsForAMake(String vehicleMake) {
        List<String> getModelsForMakeArray = new ArrayList<>();
        for (int i = 0; i < vehicleMakeArray.size(); i++) {
            String currentVehicle = vehicleMakeArray.get(i);
            if (vehicleMake.equals(currentVehicle)) {
                String currentModel = vehicleModelArray.get(i);
                if (!getModelsForMakeArray.contains(currentModel)) {
                    getModelsForMakeArray.add(currentModel);
                }
            }
        }

        return getModelsForMakeArray;

    }




    public List<Integer> getYearsForAModel(String vehicleModel) {
        List<Integer> getYearsForModelArray = new ArrayList<>();
        for (int i = 0; i < vehicleModelArray.size(); i++) {
            String currentModel = vehicleModelArray.get(i);
            if (currentModel.equals(vehicleModel)) {
                int year = vehicleYearArray.get(i);
                if (!getYearsForModelArray.contains(year)) {
                    getYearsForModelArray.add(year);
                }
            }
        }
        return getYearsForModelArray;
    }


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




