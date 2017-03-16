package com.example.olive.carbon_tracker.UI;

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

import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.Model.Vehicle;
import com.example.olive.carbon_tracker.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// ListView Icon: Icon made by Puppets (http://www.flaticon.com/authors/puppets) from www.flaticon.com

public class DisplayJourneyList extends AppCompatActivity {
    private Singleton singleton = Singleton.getInstance();
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
                "_id from JourneyInfoTable",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String date = cursor.getString(0);
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
        ArrayAdapter<Journey> adapter = new myArrayAdapter();
        ListView listView = (ListView) findViewById(R.id.listJourneys);
        listView.setAdapter(adapter);
        setListClickListener(listView);
    }

    private void setListClickListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showActivity = new Intent(DisplayJourneyList.this, EditJourney.class);
                showActivity.putExtra("Position", position);
                startActivityForResult(showActivity, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setListView();
    }


    private void setImageView(View itemView, Journey journey) {
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgJourney);
        imageView.setImageResource(journey.getIconID());
    }

    private void setTextView(View itemView, Journey journey, int id) {
        TextView textView = (TextView) itemView.findViewById(id);
        String msg;
        if (id == R.id.txtMode) {
            msg = "Vehicle: " + journey.getVehicleName();
        } else if (id == R.id.txtCarName) {
            msg = "Route: " + journey.getRouteName();
        } else {
            msg = "Date: " + journey.getDateOfTrip();
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

            TextView JourneyDate = (TextView)itemView.findViewById(R.id.txtDate);
            JourneyDate.setText("Date: "+currJourney.getDateOfTrip());

            TextView CarName = (TextView)itemView.findViewById(R.id.txtCarName);
            CarName.setText("Vehicle Name: " + currJourney.getVehicleName());

            TextView RouteName = (TextView)itemView.findViewById(R.id.textRouteName);
            RouteName.setText("Route Name: "+ currJourney.getRouteName());

            TextView mode = (TextView)itemView.findViewById(R.id.txtMode);
            mode.setText("Mode: " + currJourney.getMode());

            TextView TotalDst = (TextView)itemView.findViewById(R.id.textTotal);
            TotalDst.setText("Total Distance: " + currJourney.getTotalDistance());

            TextView CO2 = (TextView)itemView.findViewById(R.id.textCO2);
            double CO2Emitted = currJourney.getCarbonEmitted();
            DecimalFormat df = new DecimalFormat("#.##");
            CO2Emitted = Double.valueOf(df.format(CO2Emitted));
            CO2.setText("CO2 Emitted: " + CO2Emitted);
            return itemView;
        }
    }
}
