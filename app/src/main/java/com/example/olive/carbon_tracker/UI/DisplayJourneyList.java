package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.List;

// ListView Icon: Icon made by Puppets (http://www.flaticon.com/authors/puppets) from www.flaticon.com

public class DisplayJourneyList extends AppCompatActivity {
    private Singleton singleton = Singleton.getInstance();
    private List<Journey> JourneyList = singleton.getUsersJourneys();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_journey_list);
        setListView();
    }

    private void setListView() {
        ArrayAdapter<Journey> adapter = new myArrayAdapter();
        ListView listView = (ListView) findViewById(R.id.listJourneys);
        listView.setAdapter(adapter);
        setListClickListener(listView);
    }

    private void setListClickListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Establish a way to access a journey
                Intent showActivity = new Intent(DisplayJourneyList.this, EditJourney.class);
                showActivity.putExtra("Position", position);
                startActivity(showActivity);
            }
        });
    }

    private void setTextView(int id, Journey journey) {
        TextView textView = (TextView) findViewById(id);
        String msg;
        if (id == R.id.txtVehicle) {
            msg = "Vehicle: " + journey.getVehicleName();
        } else if (id == R.id.txtRoute) {
            msg = "Route: " + journey.getRouteName();
        } else {
            msg = "Date: " + journey.getDateOfTrip();
        }
        textView.setText(msg);
    }

    private void setImageView(View itemView, Journey journey) {
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgJourney);
        imageView.setImageResource(journey.getIconID());
    }

    private class myArrayAdapter extends ArrayAdapter<Journey> {
        public myArrayAdapter(){
            super(DisplayJourneyList.this, R.layout.single_element_journey_list, JourneyList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.single_element_journey_list, parent, false);
            }

            Journey currJourney = JourneyList.get(position);
            setImageView(itemView, currJourney);
            setTextView(R.id.txtVehicle, currJourney);
            setTextView(R.id.txtRoute, currJourney);
            setTextView(R.id.txtDate, currJourney);
            return itemView;
        }
    }
}
