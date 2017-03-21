package com.example.olive.carbon_tracker.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// ListView Icon: Icon made by Puppets (http://www.flaticon.com/authors/puppets) from www.flaticon.com

public class DisplayJourneyList extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();
    private List<Journey> JourneyList = singleton.getUsersJourneys();
    private SQLiteDatabase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_display_journey_list);
        setListView();
    }

    private void setListView() {
        List<Journey> JourneyListFromDB = new ArrayList<>();
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH + DatabaseHelper.DB_NAME,null);
        Cursor cursor = myDataBase.rawQuery("select JourneyDate," +
                "JourneyMode," +
                "JourneyCarName," +
                "JourneyRouteName, " +
                "JourneyRouteTotal, " +
                "JourneyCO2Emitted," +
                "_id from JourneyInfoTable order by date(JourneyDate) asc ",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String[] tempDate = (cursor.getString(0)).split("-");
            String tempYear = tempDate[0];
            String tempMonth = ChangMonthInString(tempDate[1]);
            String tempDay = tempDate[2];
            String date = tempDay + "/" + tempMonth + "/" + tempYear;
            String mode = cursor.getString(1);
            String routeName = cursor.getString(3);
            int totalDst = cursor.getInt(4);
            String vehicleName = cursor.getString(2);
            double co2 = cursor.getDouble(5);
            long JourneyDBId = cursor.getLong(cursor.getColumnIndex("_id"));
            Journey tempJourney = new Journey(date,mode,routeName,
                    totalDst,vehicleName,co2,JourneyDBId);
            JourneyListFromDB.add(tempJourney);
            cursor.moveToNext();
        }

        cursor.close();
        myDataBase.close();
        JourneyList = JourneyListFromDB;
        singleton.setJourneyList(JourneyList);
        ArrayAdapter<Journey> adapter = new myArrayAdapter();
        ListView listView = (ListView) findViewById(R.id.listJourneys);
        listView.setAdapter(adapter);
        setListClickListener(listView);
    }

    private String ChangMonthInString(String tempMonth) {
        if (tempMonth.matches("01")){
            return "January";
        }
        if (tempMonth.matches("02")){
            return "February";
        }
        if (tempMonth.matches("03")){
            return "March";
        }
        if (tempMonth.matches("04")){
            return "April";
        }
        if (tempMonth.matches("05")){
            return "May";
        }
        if (tempMonth.matches("06")){
            return "June";
        }
        if (tempMonth.matches("07")){
            return "July";
        }
        if (tempMonth.matches("08")){
            return "August";
        }
        if (tempMonth.matches("09")){
            return "September";
        }
        if (tempMonth.matches("10")){
            return "October";
        }
        if (tempMonth.matches("11")){
            return "November";
        }
        else{
            return "Decemeber";
        }
    }

    private void setListClickListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showActivity = new Intent(DisplayJourneyList.this, SelectTransportationModeAndDate.class);
                singleton.setEditJourneyPosition(position);
                singleton.setEditPostion_Journey(JourneyList.get(position).getJourneyID());
                singleton.userEditJourney();
                startActivity(showActivity);
                finish();
            }
        });
    }

    private void setImageView(View itemView, Journey journey) {
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgJourney);
        imageView.setImageResource(journey.getIconID());
    }

    private void setTextView(View itemView, Journey journey, int id) {
        TextView textView = (TextView) itemView.findViewById(id);
        String msg;
        if (id == R.id.txtMode) {
            msg = "Mode: " + journey.getMode();
        } else if (id == R.id.txtCarName) {
            msg = "Vehicle Name: " + journey.getVehicleName();
        } else if (id == R.id.textRouteName) {
            msg = "Route Name: " + journey.getRouteName();
        } else if (id == R.id.txtDate) {
            msg = "Date: " + journey.getDateOfTrip();
        } else if (id == R.id.textTotal) {
            msg = "Total Distance: " + journey.getTotalDistance();
        } else {
            double CO2Emitted = journey.getCarbonEmitted();
            DecimalFormat df = new DecimalFormat("#.##");
            CO2Emitted = Double.valueOf(df.format(CO2Emitted));
            msg = "CO2 Emitted: " + CO2Emitted;
        }
        textView.setText(msg);
    }

    private class myArrayAdapter extends ArrayAdapter<Journey> {
        private myArrayAdapter(){
            super(DisplayJourneyList.this, R.layout.single_element_journey_list, JourneyList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.single_element_journey_list, parent, false);
            }

            Journey currJourney = JourneyList.get(position);
            setTextView(itemView, currJourney, R.id.txtMode);
            setTextView(itemView, currJourney, R.id.txtCarName);
            setTextView(itemView, currJourney, R.id.textRouteName);
            setTextView(itemView, currJourney, R.id.txtDate);
            setTextView(itemView, currJourney, R.id.textTotal);
            setTextView(itemView, currJourney, R.id.textCO2);

            return itemView;
        }
    }
    public void onBackPressed() {
        Intent goBackToMainMenu = MainMenu.makeIntent(DisplayJourneyList.this);
        startActivity(goBackToMainMenu);
    }
     public static Intent makeIntent (Context context) {
        return new Intent(context, DisplayJourneyList.class);
    }
}
