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

import com.example.olive.carbon_tracker.R;
import com.example.olive.carbon_tracker.Model.Route;
import com.example.olive.carbon_tracker.Model.Singleton;

import java.util.ArrayList;
import java.util.List;

public class DisplayRouteList extends AppCompatActivity {
//    private RouteCollection allRoutes = new RouteCollection();
    private List<Route> RouteList = new ArrayList<Route>();
    Singleton singleton  = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_route_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ChooseRoute);
        setSupportActionBar(toolbar);
//        allRoutes = singleton.getUserRoutes();
        RouteList = singleton.getRouteList();
        AddRoute();
        EditRoute();
        showAllRoute();

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
                startActivityForResult(EditIntent,0);//case 1 means add route
                //case 2 means edit route
                return true;
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
                startActivityForResult(AddIntent,1);//case 1 means add route
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
    public static Intent makeIntent(Context context) {
        return new Intent(context, DisplayRouteList.class);
    }
}


