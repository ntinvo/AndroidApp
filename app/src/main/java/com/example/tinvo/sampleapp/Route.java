package com.example.tinvo.sampleapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by TinVo on 5/20/2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}