package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.olive.carbon_tracker.Model.MonthlyUtilitiesData;
import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DisplayMonthlyUtilities extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    private List<MonthlyUtilitiesData> MonthlyUtilitiesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_monthly_utilities);
        MonthlyUtilitiesList = singleton.getBillList();

        SetupAddBtn();
        EditBill();
        showAllBills();

    }

    private void SetupAddBtn() {
        final FloatingActionButton AddBill = (FloatingActionButton) findViewById(R.id.add_bill_btn);
        AddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.userAddMonthlyUtilities();
                startActivityForResult(new Intent(DisplayMonthlyUtilities.this, MonthlyUtilities.class), 1);
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
                singleton.setEditPosition_bill(position);
                singleton.userEditMonthlyUtilities();
                startActivityForResult(new Intent(DisplayMonthlyUtilities.this, MonthlyUtilities.class), 0);
                finish();
                return true;
            }
        });


    }


    private void showAllBills() {
        //TODO show all bills using database
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
            indElec.setText("Ind. electricity usage: " + roundElec +"kWh");
            TextView indGas = (TextView) itemView.findViewById(R.id.IndGasUsage);
            String roundGas = String.format("%.2f", currentBill.getIndGasUsage());
            indGas.setText("Ind. natural gas usage: " + roundGas +"GJ");
            TextView indCO2 = (TextView) itemView.findViewById(R.id.IndCO2);
            String roundCO2 = String.format("%.2f", currentBill.getIndCO2());
            indCO2.setText("Ind. CO2 emission: " + roundCO2 +"kg");

            return itemView;

        }
    }

    public void onBackPressed(){
        startActivity(new Intent(DisplayMonthlyUtilities.this, MainMenu.class));
    }
}
