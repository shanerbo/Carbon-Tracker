package com.example.olive.carbon_tracker.UI;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.DatabaseHelper;
import com.example.olive.carbon_tracker.Model.MonthlyUtilitiesData;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * displays monthly utility bills
 */
public class DisplayMonthlyUtilities extends AppCompatActivity {
    private static final long NUM_DAYS_REMINDER = 43;
    Singleton singleton = Singleton.getInstance();
    private List<MonthlyUtilitiesData> MonthlyUtilitiesList = new ArrayList<>();
    private SQLiteDatabase myDataBase;
    private enum databaseCountMode {
        NoRecentJourneys,
        MoreJourneys,
        NoRecentUtilities,
        MoreUtilities
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_display_monthly_utilities);

        showAllBills();
        SetupAddBtn();
        EditBill();
        checkNotifications();

        setToolBar();
    }

    private void SetupAddBtn() {
        final FloatingActionButton AddBill = (FloatingActionButton) findViewById(R.id.add_bill_btn);
        AddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.userAddMonthlyUtilities();
                startActivityForResult(new Intent(DisplayMonthlyUtilities.this, AddMonthlyUtilities.class), 1);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showAllBills();
    }

    private void EditBill(){
        ListView BillList = (ListView) findViewById(R.id.ID_Bill_List);
        BillList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                singleton.setEditPosition_bill(MonthlyUtilitiesList.get(position).getUtilityDBId());
                singleton.userEditMonthlyUtilities();
                startActivityForResult(new Intent(DisplayMonthlyUtilities.this, AddMonthlyUtilities.class), 0);
                finish();
                return true;
            }
        });


    }


    private void showAllBills() {
        //TODO show all bills using database
        List<MonthlyUtilitiesData> MonthlyUtilitiesFromDB = new ArrayList<>();
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DatabaseHelper.DB_PATH +
                DatabaseHelper.DB_NAME,null);
        Cursor cursor = myDataBase.rawQuery("select * from " +
                "UtilityInfoTable order by UtilityEndDate asc",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String starDate = cursor.getString(1);
            String endDate = cursor.getString(2);
            long totalDays = cursor.getLong(5);
            long totalPerson = cursor.getLong(6);
            double indEle = cursor.getDouble(3)/totalPerson/totalDays;
            double indGas = cursor.getDouble(4)/totalPerson/totalDays;
            double co2 = cursor.getDouble(7);
            long UtilityDBId = cursor.getLong(cursor.getColumnIndex("_id"));
            MonthlyUtilitiesData tmpUtility = new MonthlyUtilitiesData(starDate,endDate,
                    totalDays,indEle,indGas,totalPerson,co2,UtilityDBId);
            MonthlyUtilitiesFromDB.add(tmpUtility);
            cursor.moveToNext();
        }
        cursor.close();
        MonthlyUtilitiesList = MonthlyUtilitiesFromDB;
        singleton.setBillList(MonthlyUtilitiesList);
        ArrayAdapter<MonthlyUtilitiesData> adapter = new myArrayAdapter();
        ListView list = (ListView) findViewById(R.id.ID_Bill_List);
        list.setAdapter(adapter);
    }

    private class myArrayAdapter extends ArrayAdapter<MonthlyUtilitiesData>{
        public myArrayAdapter(){
            super(DisplayMonthlyUtilities.this, R.layout.single_element_bill_list, MonthlyUtilitiesList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.single_element_bill_list, parent, false);
            }
            MonthlyUtilitiesData currentBill = MonthlyUtilitiesList.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.BillImage);
            imageView.setImageResource(currentBill.getIconID());

            TextView startDate = (TextView) itemView.findViewById(R.id.StartingDate);
            startDate.setText("Starting date: " + currentBill.getStartDate() +"");

            TextView endDate = (TextView) itemView.findViewById(R.id.EndingDate);
            endDate.setText("Ending date: " + currentBill.getEndDate() +"");

            TextView indElec = (TextView) itemView.findViewById(R.id.IndElecUsage);
            String roundElec = String.format("%.2f", currentBill.getIndElecUsage());
            indElec.setText("Electricity usage: " + roundElec +"kWh/day/person");

            TextView indGas = (TextView) itemView.findViewById(R.id.IndGasUsage);
            String roundGas = String.format("%.2f", currentBill.getIndGasUsage());
            indGas.setText("Natural gas usage: " + roundGas +"GJ/day/person");

            if(singleton.checkCO2Unit() == 0) {
                TextView indCO2 = (TextView) itemView.findViewById(R.id.IndCO2);
                String roundCO2 = String.format("%.2f", currentBill.getIndCO2());
                indCO2.setText("CO2 emission: " + roundCO2 + "kg/day/person");
            }
            else{
                TextView indCO2 = (TextView) itemView.findViewById(R.id.IndCO2);
                String HumanCO2 = String.format("%.2f", currentBill.getIndCO2()/2.06);
                //human-relatable unit conversion: 1 kg of garbage can produce 2.06 kg of CO2
                indCO2.setText("CO2 emission equivalent to:\n       " + HumanCO2 + "kg of garbage/day/person");
            }

            return itemView;

        }
    }




    public void onBackPressed(){
        checkNotifications();
        startActivity(new Intent(DisplayMonthlyUtilities.this, MainMenu.class));
    }

    // Remake notifications when necessary
    private void checkNotifications() {
        int journeys = getDatabaseCount(databaseCountMode.MoreJourneys);
        int utilities = getDatabaseCount(databaseCountMode.MoreUtilities);
        long dateDiff = getDateDifference(singleton.getLatestBill(), singleton.getCurrentDate());
        if (!singleton.isAddJourneyToday()) {
            singleton.setNotification(makeNotification(databaseCountMode.NoRecentJourneys));
        } else if (dateDiff >= NUM_DAYS_REMINDER) {
            singleton.setNotification(makeNotification(databaseCountMode.NoRecentUtilities));
        } else if (journeys > utilities) {
            singleton.setNotification(makeNotification(databaseCountMode.MoreUtilities, utilities));
        } else {
            singleton.setNotification(makeNotification(databaseCountMode.MoreJourneys, journeys));
        }
    }

    private int getDatabaseCount(databaseCountMode mode) {
        int count;
        if (mode == databaseCountMode.MoreJourneys) {
            String countQuery = "SELECT  * FROM " + "JourneyInfoTable";
            Cursor cursor = myDataBase.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } else if (mode == databaseCountMode.MoreUtilities) {
            String countQuery = "SELECT  * FROM " + "UtilityInfoTable";
            Cursor cursor = myDataBase.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } else {
            count = -1;
        }
        return count;
    }

    private Notification makeNotification(databaseCountMode mode) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        String msg;
        if (mode == databaseCountMode.NoRecentJourneys) {
            msg = getString(R.string.no_recent_journeys_notification);
        } else {
            msg = getString(R.string.no_utilities_in_a_month_and_a_half);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(makeNotificationIntent(mode));
        builder.setStyle(new Notification.BigTextStyle().bigText(msg));
        return builder.build();
    }

    private Notification makeNotification(databaseCountMode mode, int count) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        String msg;
        if (mode == databaseCountMode.MoreUtilities) {
            msg = getString(R.string.more_utilities_notification, count);
        } else {
            msg = getString(R.string.more_journeys_notification, count);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(makeNotificationIntent(mode));
        builder.setStyle(new Notification.BigTextStyle().bigText(msg));
        return builder.build();
    }

    private PendingIntent makeNotificationIntent(databaseCountMode mode) {
        Intent intent;
        if (mode == databaseCountMode.MoreUtilities
                || mode == databaseCountMode.NoRecentUtilities) {
            intent = new Intent(this, DisplayMonthlyUtilities.class);
        } else {
            intent = new Intent(this, SelectTransportationModeAndDate.class);
        }
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private long getDateDifference(String StartDate, String EndDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(StartDate);
            Date end = sdf.parse(EndDate);

            long dateDifference = end.getTime() - start.getTime();
            return dateDifference / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            Toast.makeText(
                    DisplayMonthlyUtilities.this,
                    getString(R.string.date_difference_error, "DisplayMonthlyUtilities"),
                    Toast.LENGTH_LONG
            ).show();
        }
        return -1;
    }

    private void setToolBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_bill);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.tool_change_unit){
            if(singleton.checkCO2Unit() == 0)
                singleton.humanRelatableUnit();
            else
                singleton.originalUnit();
            Toast.makeText(getApplicationContext(), "CO2 unit has been changed", Toast.LENGTH_SHORT).show();
            ArrayAdapter<MonthlyUtilitiesData> adapter = new myArrayAdapter();
            ListView list = (ListView) findViewById(R.id.ID_Bill_List);
            list.setAdapter(adapter);
            saveCO2UnitStatus(singleton.checkCO2Unit());
            return true;
        }
        if(id == R.id.tool_about){
            startActivity(new Intent(DisplayMonthlyUtilities.this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCO2UnitStatus(int status) {
        SharedPreferences prefs = this.getSharedPreferences("CO2Status", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("CO2 status", status);
        editor.apply();
    }

}
