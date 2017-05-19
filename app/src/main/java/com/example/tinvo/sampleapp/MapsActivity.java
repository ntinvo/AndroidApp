package com.example.tinvo.sampleapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    private GoogleMapsBottomSheetBehavior behavior;
    private ImageView parallax;
    HashMap<String, String> maps = new HashMap<String, String>();
    HashMap<String, String> addressesMap = new HashMap<String, String>();
    String[] places = {"Nhà Thờ Đức Bà", "Chợ Bến Thành", "Saigon Post Office", "Sài Gòn Zoo", "Bitexco Financial Tower", "Phạm Ngũ Lão Street", "Bến Nhà Rồng", "Công Viên Văn Hóa Đầm Sen", "Địa Đạo Củ Chi", "Khu Du Lịch Văn Thánh",
                       "Công Viên Gia Định", "Công Viên Lê Thị Riêng", "Dinh Độc Lập"};
    String[] placesAddresses = {"1, Công xã Paris, Bến Nghé, Quận 1, Hồ Chí Minh, Vietnam",
                             "36-34-32-30 Phan Bội Châu, Bến Thành, Quận 1, Hồ Chí Minh, Vietnam",
                             "2 Công xã Paris, Bến Nghé, Quận 1, Hồ Chí Minh, Vietnam",
                             "5, 2 Nguyễn Bỉnh Khiêm, Bến Nghé, Hồ Chí Minh, TP. Hồ Chí Minh, Vietnam",
                             "District 1, Ho Chi Minh City",
                             "District 1, Ho Chi Minh City",
                             "1 Nguyễn Tất Thành, phường 12, Quận 4, Hồ Chí Minh, Vietnam",
                             "3 Hòa Bình, phường 3, Quận 11, Hồ Chí Minh, Vietnam",
                             "Ấp Phú Hiệp, Phú Hiệp, Xã Phú Mỹ Hưng, Củ Chi, Bến Tre 733800, Vietnam",
                             "48/10 Điện Biên Phủ, Phường 22, Bình Thạnh, Hồ Chí Minh, Vietnam",
                             "Hoàng Minh Giám, Tân Bình, TP HCM, Vietnam",
                             "Bắc Hải, Cư xá Bắc Hải, Phường 15, Quận 10, Hồ Chí Minh, Vietnam",
                             "135 Nam Kỳ Khởi Nghĩa, Bến Thành, Quận 1, Hồ Chí Minh, Vietnam"};
    String[] placesAbbs = {"ntdb", "cbt", "sgp", "sgz", "btexco", "pnl", "bnr", "ds", "ddcc", "kdlvt", "cvgd", "cvltr", "ddl"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final View bottomsheet = findViewById(R.id.bottomsheet);
        behavior = GoogleMapsBottomSheetBehavior.from(bottomsheet);
        parallax = (ImageView) findViewById(R.id.parallax);
        behavior.setParallax(parallax);
        behavior.anchorView(bottomsheet);

        bottomsheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // set the height of the parallax to fill the gap between the anchor and the top of the screen
                CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(parallax.getMeasuredWidth(),behavior.getAnchorOffset());
                parallax.setLayoutParams(layoutParams);
                bottomsheet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        behavior.setBottomSheetCallback(new GoogleMapsBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @GoogleMapsBottomSheetBehavior.State int newState) {
                // each time the bottomsheet changes position, animate the camera to keep the pin in view
                // normally this would be a little more complex (getting the pin location and such),
                // but for the purpose of an example this is enough to show how to stay centered on a pin
                // mMap.animateCamera(CameraUpdateFactory.newLatLng(SYDNEY));
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
////        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
////            @Override
////            public void onMapClick(LatLng latLng) {
////                Log.i("info", "map clicked");
////                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
////                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
////                }
////            }
////        });
//        String city = getIntent().getExtras().getString("city");
//        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());



////        bottomSheet = (NestedScrollView) findViewById(R.id.bottom_sheet);
////        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//











//        try {
//            // Get the city and its Lat Lng
//            List<Address> addresses = geoCoder.getFromLocationName(city, 1);
//            Double lat = addresses.get(0).getLatitude();
//            Double lng = addresses.get(0).getLongitude();
//            LatLng cityLatLng = new LatLng(lat, lng);
//
//            // Position the camera
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.7797838,106.6968061), 13));
//
//            // Add to cluster
//            mClusterManager = new ClusterManager<MyItem>(this, mMap);
//            mMap.setOnCameraIdleListener(mClusterManager);
//            mMap.setOnMarkerClickListener(mClusterManager);
//
//            // Add to maps
//            int i = 0;
//            for(String abb: placesAbbs) {
//                if(maps.get(abb) == null) {
//                    maps.put(places[i], abb);
//                }
//                if(addressesMap.get(places[i]) == null) {
//                    addressesMap.put(places[i], placesAddresses[i]);
//                }
//                i++;
//            }
//
////            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
////                @Override
////                public void onStateChanged(@NonNull View bottomSheet, int newState) {
////
////                }
////
////                @Override
////                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
////
////                }
////            });
//










//            // Add
//            for(int index = 0; index < places.length; index++) {
//                String place = places[index];
//                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        if(marker.getZIndex() == 1.0f) {
//                            marker.setZIndex(0.0f);
//                        } else if(marker.getZIndex() == 0.0f) {
//                            marker.setZIndex(1.0f);
//                        }
//
//
////                        TextView cityName = (TextView) bottomSheet.findViewById(R.id.bottomView1);
////                        TextView cityAddress = (TextView) bottomSheet.findViewById(R.id.bottomView2);
////                        ImageView slideUpImageView = (ImageView) bottomSheet.findViewById(R.id.slideUpImageView);
////
////
////                        Resources res = getResources();
////                        String mDrawableName = maps.get(marker.getTitle());
////                        int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
////                        slideUpImageView.setImageResource(resID);
//////                        slideUpImageView.setImageResource(R.drawable.cbt);
////                        cityName.setText(maps.get(marker.getTitle()));
//////                        cityAddress.setText(addressesMap.get(marker.getTitle()));
////                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//                        return true;
//                    }
//                });




////              bottomSheetBehavior.setPeekHeight(0);
//                List<Address> temp = geoCoder.getFromLocationName(place, 1);
//                Double latTmp = temp.get(0).getLatitude();
//                Double lngTmp = temp.get(0).getLongitude();
//                String addr = addressesMap.get(place);
//
//                MyItem item = new MyItem(latTmp, lngTmp, place, addr);
//                mClusterManager.setRenderer(new IconRenderer(this.getApplicationContext(), mMap, mClusterManager));
//                mClusterManager.addItem(item);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        behavior.anchorMap(mMap);
        String city = getIntent().getExtras().getString("city");
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> cityList = geoCoder.getFromLocationName(city, 1);
            Double lat = cityList.get(0).getLatitude();
            Double lng = cityList.get(0).getLongitude();

            // Position the camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.7797838,106.6968061), 13));

            // Add to cluster
            mClusterManager = new ClusterManager<MyItem>(this, mMap);
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);

            // Add to maps
            int i = 0;
            for(String abb: placesAbbs) {
                if(maps.get(abb) == null) {
                    maps.put(places[i], abb);
                }
                if(addressesMap.get(places[i]) == null) {
                    addressesMap.put(places[i], placesAddresses[i]);
                }
                i++;
            }

            // Add
            for(String place: places) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.getZIndex() == 1.0f) {
                            marker.setZIndex(0.0f);
                        } else if(marker.getZIndex() == 0.0f) {
                            marker.setZIndex(1.0f);
                        }

                        View btn =  findViewById(R.id.directionBtnCustomHeader);
                        if(btn != null) {
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (behavior != null) {
                                        behavior.setHideable(true);
                                        behavior.setState(GoogleMapsBottomSheetBehavior.STATE_HIDDEN);
                                    }
                                }
                            });
                        }
                        behavior.setState(GoogleMapsBottomSheetBehavior.STATE_COLLAPSED);
                        behavior.setHideable(false);
                        TextView placeNameCustomHeader = (TextView) findViewById(R.id.placeNameCustomHeader);
                        TextView placeAddressCustomHeader = (TextView) findViewById(R.id.placeAddressCustomHeader);
                        placeNameCustomHeader.setText(marker.getTitle());
                        placeAddressCustomHeader.setText(marker.getSnippet());
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        return true;
                    }
                });

                List<Address> temp = geoCoder.getFromLocationName(place, 1);
                Double latTmp = temp.get(0).getLatitude();
                Double lngTmp = temp.get(0).getLongitude();
                String addr = addressesMap.get(place);

                MyItem item = new MyItem(latTmp, lngTmp, place, addr);
                mClusterManager.setRenderer(new IconRenderer(this.getApplicationContext(), mMap, mClusterManager));
                mClusterManager.addItem(item);
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    behavior.setHideable(true);
                    behavior.setState(GoogleMapsBottomSheetBehavior.STATE_HIDDEN);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

//        // Add a marker in Sydney and move the camera
//        mMap.addMarker(new MarkerOptions().position(SYDNEY).title("Marker in Sydney"));
//
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                View btn =  findViewById(R.id.testBtn);
//                if(btn != null) {
//                    btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (behavior != null) {
//                                behavior.setHideable(true);
//                                behavior.setState(GoogleMapsBottomSheetBehavior.STATE_HIDDEN);
//                            }
//                        }
//                    });
//                }
//                behavior.setState(GoogleMapsBottomSheetBehavior.STATE_COLLAPSED);
//                behavior.setHideable(false);
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//                return true;
//            }
//        });
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                behavior.setHideable(true);
//                behavior.setState(GoogleMapsBottomSheetBehavior.STATE_HIDDEN);
//            }
//        });
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
    }
}
