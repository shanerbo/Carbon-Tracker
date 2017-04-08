package com.example.olive.carbon_tracker.UI;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.util.TypedValue;
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
        setTextView(R.id.txtVersion);
        setTextView(R.id.txtSFULink);
        setTextView(R.id.txtImageCitations);
        setToolBar();
    }

    private void setTextView(int id) {
        TextView textView = (TextView) findViewById(id);
        String msg;
        if (id == R.id.txtVersion) {
            TypedValue temp = new TypedValue();
            getResources().getValue(R.dimen.current_version, temp, true);
            float currentVersion = temp.getFloat();
            msg = getString(R.string.app_version, currentVersion);
            textView.setText(msg);
        } else {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public void onBackPressed() {
        finish();
    }

    private void setToolBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
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
            if(singleton.checkCO2Unit() == 0) {
                singleton.humanRelatableUnit();
                Toast.makeText(getApplicationContext(), R.string.UnitChangedToGarbageUnit, Toast.LENGTH_SHORT).show();
            }
            else {
                singleton.originalUnit();
                Toast.makeText(getApplicationContext(), R.string.UnitChangedToKG, Toast.LENGTH_SHORT).show();
            }
            saveCO2UnitStatus(singleton.checkCO2Unit());
            return true;
        }
        if(id == R.id.tool_about){
            Toast.makeText(getApplicationContext(), R.string.alreadyAtAboutScreen,
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

    }
}
