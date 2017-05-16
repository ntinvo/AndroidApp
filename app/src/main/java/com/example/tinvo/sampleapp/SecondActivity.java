package com.example.tinvo.sampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textView = (TextView) findViewById(R.id.textViewFromSecond);
        TextView textViewLat = (TextView) findViewById(R.id.textViewLat);
        TextView textViewLng = (TextView) findViewById(R.id.textViewLng);

        textView.setText(getIntent().getExtras().getString("city"));
        textViewLat.setText(getIntent().getExtras().getDouble("lat") + "");
        textViewLng.setText(getIntent().getExtras().getDouble("lng") + "");

    }
}
