package com.example.olive.carbon_tracker.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.util.TypedValue
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.carbon_tracker.Model.Singleton;
import com.example.olive.carbon_tracker.R;

public class AboutActivity extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
        setTestView(R.id.txtVersion);
        setToolBar();
    }

    private void setTestView(int id) {
//        TextView textView = (TextView) findViewById(id);
//        float currentVersion = getResources().getDimension(R.dimen.current_version);
//        String msg = getString(R.string.app_version, currentVersion);
//        textView.setText(msg);
    }

    public void onBackPressed() {
        finish();
    }

    private void setToolBar(){
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_about);
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
            saveCO2UnitStatus(singleton.checkCO2Unit());
            Toast.makeText(getApplicationContext(), "CO2 unit has been changed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.tool_about){
            Toast.makeText(getApplicationContext(), "You're already at About screen",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCO2UnitStatus(int status) {
        SharedPreferences prefs = this.getSharedPreferences("CO2Status", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("CO2 status", status);
        editor.apply();
        TextView textView = (TextView) findViewById(id);
        TypedValue temp = new TypedValue();
        getResources().getDimension(R.dimen.current_version, temp, true);
        float currentVersion = temp.getFloat();
        String msg = getString(R.string.app_version, currentVersion);
        textView.setText(msg);
    }
}
