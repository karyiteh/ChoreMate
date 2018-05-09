package com.example.teh_k.ChoreMate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NotificationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
    }

    Spinner dropdown = (Spinner) findViewById(R.id.spinner);

    String[] items = new String[]{"1 hour", "1 day", "1 year", "Forever"};

    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

    //dropdown.setAdapter(adapter);

}
