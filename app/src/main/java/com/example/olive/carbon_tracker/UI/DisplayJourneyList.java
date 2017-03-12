package com.example.olive.carbon_tracker.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.olive.carbon_tracker.Model.Journey;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayJourneyList extends AppCompatActivity {
    private Singleton singleton = Singleton.getInstance();
    private List<Journey> JourneyList = new ArrayList<Journey>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_journey_list);
//        setListView();
    }

    private void setListView() {
        ArrayAdapter<Journey> adapter = new ArrayAdapter<>(
                DisplayJourneyList.this,
                R.layout.single_element_journey_list,
                JourneyList
        );

    }
}
