package com.example.tinvo.sampleapp;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    private GoogleMapsBottomSheetBehavior behavior;
    private ImageView parallax;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private Distance myDistance;
    private Duration myDuration;
//    private Circle lastUserCircle;
//    private long pulseDuration = 1000;
    private ValueAnimator lastPulseAnimator;
    private LocationManager locationManager;
    private Location currentLocation;
    private TextView distanceCustomHeader;
    private TextView durationCustomHeader;
//
//
//    private List<Marker> originMarkers = new ArrayList<>();
//    private List<Marker> destinationMarkers = new ArrayList<>();
//    private List<Polyline> polylinePaths = new ArrayList<>();
//    private ProgressDialog progressDialog;

    HashMap<String, String> maps = new HashMap<String, String>();
    HashMap<String, String> addressesMap = new HashMap<String, String>();
    HashMap<String, String> descriptionsMap = new HashMap<String, String>();
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
    String[] descriptions = {"Nhà thờ chính tòa Đức Bà Sài Gòn (tên chính thức: Vương cung thánh đường chính tòa Đức Mẹ Vô nhiễm Nguyên tội, tiếng Anh: Immaculate Conception Cathedral Basilica, tiếng Pháp: Cathédrale Notre-Dame de Saïgon, gọi tắt là Nhà thờ Đức Bà) là nhà thờ chính tòa của Tổng giáo phận Thành phố Hồ Chí Minh, một trong những công trình kiến trúc độc đáo của Sài Gòn, điểm đến của du khách trong và ngoài nước, nét đặc trưng của du lịch Việt Nam.",
                            "Chợ Bến Thành là một ngôi chợ nằm tại Quận 1, Thành phố Hồ Chí Minh. Trong nhiều trường hợp, hình ảnh tháp đồng hồ ở cửa nam của ngôi chợ này được xem là biểu tượng không chính thức của Thành phố Hồ Chí Minh.",
                            "Bưu điện trung tâm Sài Gòn là một trong những công trình kiến trúc tiêu biểu tại Thành phố Hồ Chí Minh, tọa lạc tại số 2, Công trường Công xã Paris, Quận 1. Đây là tòa nhà được người Pháp xây dựng trong khoảng năm 1886–1891 với phong cách châu Âu theo đồ án thiết kế của kiến trúc sư Villedieu cùng phụ tá Foulhoux. Đây là công trình kiến trúc mang phong cách phương Tây kết hợp với nét trang trí phương Đông.",
                            "Thảo Cầm Viên Sài Gòn (tên gọi tắt: Thảo Cầm Viên, người dân quen gọi Sở thú) là công viên bảo tồn động vật - thực vật ở Thành phố Hồ Chí Minh, Việt Nam. Đây là vườn thú có tuổi thọ đứng hàng thứ 8 trên thế giới.[1] Khuôn viên rộng lớn này hiện tọa lạc gần hạ lưu kênh Nhiêu Lộc - Thị Nghè với hai cổng vào nằm ở số 2B đường Nguyễn Bỉnh Khiêm và số 1 đường Nguyễn Thị Minh Khai phường Bến Nghé, quận 1, Thành phố Hồ Chí Minh.",
                            "Tòa nhà Bitexco Financial, Bitexco Financial Tower hay Tháp Tài chính Bitexco là một tòa nhà chọc trời được xây dựng tại trung tâm Quận 1, Thành phố Hồ Chí Minh do Bitexco làm chủ đầu tư. Tòa nhà được xây trên diện tích gần 6.100 m². Tổng vốn đầu tư ước tính khoảng 400 triệu đôla Mỹ, do một tập đoàn đầu tư của Việt Nam là Bitexco Group, đơn vị chủ đầu tư các dự án Bitexco Office Building, The Manor Hà Nội, The Manor Thành phố Hồ Chí Minh và The Garden.",
                            "Phạm Ngũ Lão Đề Thám Bùi Viên (Q.1) có số lượng không nhỏ những cô gái luôn sẵn sàng đáp ứng nhu cầu tình dục cho khách Tây. Họ luôn ẩn mình dưới nhiều dạng nhưng hình thức mồi chài của họ khá lộ liễu.",
                            "Bến Nhà Rồng, hay Bảo tàng Hồ Chí Minh, khởi đầu là một thương cảng lớn của Sài Gòn. Thương cảng này nằm trên sông Sài Gòn và được xây dựng từ 1862, và hơn 2 năm sau đó, năm 1864, ngôi nhà Rồng này được hoàn thành, trên khu vực gần cầu Khánh Hội, nay thuộc quận 4.",
                            "Công viên Văn hoá Đầm Sen là công viên giải trí nằm trên đường Hòa Bình, Quận 11, Thành phố Hồ Chí Minh. Công viên có diện tích 50 hecta gồm 20% là mặt hồ và 60% cây xanh và vườn hoa.",
                            "Địa đạo Củ Chi là một hệ thống phòng thủ trong lòng đất ở huyện Củ Chi, cách Thành phố Hồ Chí Minh 70 km về hướng tây-bắc. Hệ thống này được Việt Minh và Mặt trận Dân tộc Giải phóng miền Nam Việt Nam đào trong thời kỳ Chiến tranh Đông Dương và Chiến tranh Việt Nam.",
                            "Cách trung tâm thành phố khoảng 2km, khu du lịch Văn Thánh có tổng diện tích diện tích 77.000 m2, phần hồ chiếm khoảng 2 ha, khu du lịch mát mẻ, rộng rãi phù hợp với nhiều hoạt động giải trí thư giãn.",
                            "Công viên Gia Định là một công viên cây xanh ở quận Gò Vấp, thành phố Hồ Chí Minh. Đây được xem là lá phổi của thành phố với diện tích phủ xanh khá lớn.",
                            "Với hàng cây xanh mướt mắt, hồ câu cá, khu trò chơi thiếu nhi, … công viên Lê Thị Riêng vốn là trung tâm vui chơi, giải trí quen thuộc của người dân thành phố Hồ Chí Minh.",
                            "Dinh Độc Lập (tên gọi trước đây là dinh Norodom, ngày nay còn gọi là dinh Thống Nhất hay hội trường Thống Nhất) là một công trình kiến trúc, tòa nhà ở Thành phố Hồ Chí Minh. Nó từng là nơi ở và làm việc của Tổng thống Việt Nam Cộng hòa. Hiện nay, nó đã được chính phủ Việt Nam xếp hạng là di tích quốc gia đặc biệt."};

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
                CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(parallax.getMeasuredWidth(), behavior.getAnchorOffset());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be granted", Toast.LENGTH_LONG);
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        behavior.anchorMap(mMap);
        String city = getIntent().getExtras().getString("city");
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        try {
            List<Address> cityList = geoCoder.getFromLocationName(city, 1);
            Double lat = cityList.get(0).getLatitude();
            Double lng = cityList.get(0).getLongitude();

            // Position the camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.7797838, 106.6968061), 13));

            // Add to cluster
            mClusterManager = new ClusterManager<MyItem>(this, mMap);
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);

            // Add to maps
            int i = 0;
            for (String abb : placesAbbs) {
                if (maps.get(abb) == null) {
                    maps.put(places[i], abb);
                }
                if (addressesMap.get(places[i]) == null) {
                    addressesMap.put(places[i], placesAddresses[i]);
                }
                if (descriptionsMap.get(places[i]) == null) {
                    descriptionsMap.put(places[i], descriptions[i]);
                }

                i++;
            }

            // Add
            for (String place : places) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        if (marker.getZIndex() == 1.0f) {
                            marker.setZIndex(0.0f);
                        } else if (marker.getZIndex() == 0.0f) {
                            marker.setZIndex(1.0f);
                        }


                        View btn = findViewById(R.id.directionBtnCustomHeader);
                        if (btn != null) {
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (behavior != null) {
                                        behavior.setHideable(true);
                                        behavior.setState(GoogleMapsBottomSheetBehavior.STATE_COLLAPSED);
                                        getDirection(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), marker.getPosition());
                                        distanceCustomHeader = (TextView) findViewById(R.id.distanceCustomHeader);
                                        durationCustomHeader = (TextView) findViewById(R.id.durationCustomHeader);
                                        TextView placeNameCustomHeader = (TextView) findViewById(R.id.placeNameCustomHeader);
                                        placeNameCustomHeader.setTextSize(17f);
                                        distanceCustomHeader.setVisibility(View.VISIBLE);
                                        durationCustomHeader.setVisibility(View.VISIBLE);
                                        if(!polylinePaths.isEmpty()) {
                                            Polyline temp = polylinePaths.remove(0);
                                            temp.remove();
                                        }
                                    }
                                }
                            });
                        }
                        behavior.setState(GoogleMapsBottomSheetBehavior.STATE_COLLAPSED);
                        behavior.setHideable(false);
                        TextView placeNameCustomHeader = (TextView) findViewById(R.id.placeNameCustomHeader);
                        TextView placeAddressCustomHeader = (TextView) findViewById(R.id.placeAddressCustomHeader);
                        TextView descriptionCustomContent = (TextView) findViewById(R.id.descriptionCustomContent);
                        ImageView placeImageCustomContent = (ImageView) findViewById(R.id.placeImageCustomContent);
                        descriptionCustomContent.setOnClickListener(null);
                        placeImageCustomContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        placeNameCustomHeader.setText(marker.getTitle());
                        placeAddressCustomHeader.setText(marker.getSnippet());
                        placeNameCustomHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                        distanceCustomHeader = (TextView) findViewById(R.id.distanceCustomHeader);
                        durationCustomHeader = (TextView) findViewById(R.id.durationCustomHeader);
                        distanceCustomHeader.setVisibility(View.INVISIBLE);
                        durationCustomHeader.setVisibility(View.INVISIBLE);
                        descriptionCustomContent.setText(descriptionsMap.get(marker.getTitle()));
                        Resources res = getResources();
                        String mDrawableName = maps.get(marker.getTitle());
                        int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
                        placeImageCustomContent.setImageResource(resID);
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
    }



    private void getDirection(LatLng src, LatLng des) {
        Log.i("info", src.toString());
        Log.i("info", des.toString());
        String requestURL = DIRECTION_URL_API + "origin=" + src.latitude + "," + src.longitude + "&destination=" + des.latitude + "," + des.longitude + "&key=AIzaSyCpE7zGp5M3gRPV60SDrxSKoNA5xpQ5nIQ";
        new GetDirection().execute(requestURL);
    }

    private class GetDirection extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private List<Route> parseJSon(String data) throws JSONException {
        if (data == null) {
            return null;
        }

        List<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            route.points = decodePolyLine(overview_polylineJson.getString("points"));
            distanceCustomHeader = (TextView) findViewById(R.id.distanceCustomHeader);
            durationCustomHeader = (TextView) findViewById(R.id.durationCustomHeader);
            if(distanceCustomHeader != null && durationCustomHeader != null) {
                distanceCustomHeader.setText("Khoảng Cách: " + route.distance.text);
                durationCustomHeader.setText("Thời Gian: " + route.duration.text);
            }
            routes.add(route);
        }
        polylinePaths = new ArrayList<>();
        for (Route route : routes) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

//            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
//                    .title(route.startAddress)
//                    .position(route.startLocation)));
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
//                    .title(route.endAddress)
//                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.CYAN).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }

        return routes;
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
//    private void addPulsatingEffect(){
//        if(lastPulseAnimator != null){
//            lastPulseAnimator.cancel();
//            Log.d("onLocationUpdated: ","cancelled" );
//        }
//        if(lastUserCircle != null)
//            lastUserCircle.setCenter(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
//            lastPulseAnimator = valueAnimate(currentLocation.getAccuracy(), pulseDuration, new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                if(lastUserCircle != null)
//                    lastUserCircle.setRadius((Float) animation.getAnimatedValue());
//                else {
//                    lastUserCircle = mMap.addCircle(new CircleOptions()
//                            .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
//                            .radius((Float) animation.getAnimatedValue())
//                            .strokeColor(Color.RED)
//                            .fillColor(Color.BLUE));
//                }
//            }
//        });
//
//    }
//    protected ValueAnimator valueAnimate(float accuracy,long duration, ValueAnimator.AnimatorUpdateListener updateListener){
//        Log.d( "valueAnimate: ", "called");
//        ValueAnimator va = ValueAnimator.ofFloat(0,accuracy);
//        va.setDuration(duration);
//        va.addUpdateListener(updateListener);
//        va.setRepeatCount(ValueAnimator.INFINITE);
//        va.setRepeatMode(ValueAnimator.RESTART);
//
//        va.start();
//        return va;
//    }
}
