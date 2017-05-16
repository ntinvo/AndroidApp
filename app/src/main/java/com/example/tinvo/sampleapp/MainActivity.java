package com.example.tinvo.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView myListView;
    String[] cities = {"Sài Gòn","Đà Nẵng","Hà Nội","Nha Trang", "Vũng Tàu","Đà Lạt"};
    Double[] lats = {10.7680338, 16.0483774, 21.022703, 12.2572396, 10.4029708, 11.902986};
    Double[] lngs = {106.4141696, 108.1775019, 105.8194541, 109.1281574, 107.0679295, 108.3945097};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListView = (ListView) findViewById(R.id.citiesListView);
        ListAdapter citiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        myListView.setAdapter(citiesAdapter);
        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String city = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("city", city);
                        intent.putExtra("lat", lats[position]);
                        intent.putExtra("lng", lngs[position]);
                        startActivity(intent);
                    }
                }
        );
    }

}
