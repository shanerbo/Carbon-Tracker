package com.example.olive.carbon_tracker.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.example.olive.carbon_tracker.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTestView(R.id.txtVersion);
    }

    private void setTestView(int id) {
        TextView textView = (TextView) findViewById(id);
        TypedValue temp = new TypedValue();
        getResources().getValue(R.dimen.current_version, temp, true);
        float currentVersion = temp.getFloat();
        String msg = getString(R.string.app_version, currentVersion);
        textView.setText(msg);
    }
}
