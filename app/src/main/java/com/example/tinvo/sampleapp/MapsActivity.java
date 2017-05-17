package com.example.tinvo.sampleapp;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    int[] mDrawables = {
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3
    };

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
    HashMap<String, String> maps = new HashMap<String, String>();
    HashMap<String, String> addressesMap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String city = getIntent().getExtras().getString("city");
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override public void onInfoWindowClick(Marker arg0) {

            }
        });

        try {
            // Get the city and its Lat Lng
            List<Address> addresses = geoCoder.getFromLocationName(city, 1);
            Double lat = addresses.get(0).getLatitude();
            Double lng = addresses.get(0).getLongitude();
            LatLng cityLatLng = new LatLng(lat, lng);

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
            for(int index = 0; index < places.length; index++) {
                String place = places[index];
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.getZIndex() == 1.0f) {
                            marker.setZIndex(0.0f);
                        } else if(marker.getZIndex() == 0.0f) {
                            marker.setZIndex(1.0f);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        return true;
                    }
                });
//                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//                    @Override
//                    public View getInfoWindow(Marker marker) {
//                        return null;
//                    }
//
//                    @Override
//                    public View getInfoContents(Marker marker) {
//                        Collection<Marker> userCollection = mClusterManager.getMarkerCollection().getMarkers();
//                        ArrayList<Marker> cityList = new ArrayList<Marker>(userCollection);
//
//                        for(Marker city: cityList) {
//                            Log.i("info", city.getId());
//                        }
//
//                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
//                        TextView textView = (TextView) v.findViewById(R.id.textViewCity);
//                        TextView addressView = (TextView) v.findViewById(R.id.addressView);
//                        ImageView imageView = (ImageView) v.findViewById(R.id.imageViewMain);
//
//                        Resources res = getResources();
//                        String mDrawableName = maps.get(marker.getTitle());
//                        int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
//                        imageView.setImageResource(resID);
//                        textView.setText(marker.getTitle());
//                        addressView.setText(marker.getSnippet());
//
//                        return v;
//                    }
//                });
                List<Address> temp = geoCoder.getFromLocationName(place, 1);
                Double latTmp = temp.get(0).getLatitude();
                Double lngTmp = temp.get(0).getLongitude();
                String addr = addressesMap.get(place);

                MyItem item = new MyItem(latTmp, lngTmp, place, addr);
                mClusterManager.setRenderer(new IconRenderer(this.getApplicationContext(), mMap, mClusterManager));
                mClusterManager.addItem(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

//            List<Address> ntdb = geoCoder.getFromLocationName("nha tho duc ba", 1);
//            List<Address> cbt = geoCoder.getFromLocationName("cho ben thanh", 1);
//            List<Address> bdtp = geoCoder.getFromLocationName("Saigon Post Office", 1);
//            List<Address> sgz = geoCoder.getFromLocationName("sai gon zoo", 1);
//            List<Address> ds = geoCoder.getFromLocationName("Dam Sen Park", 1);
//            List<Address> btxc = geoCoder.getFromLocationName("Bitexco Financial Tower", 1);
//            List<Address> pnl = geoCoder.getFromLocationName("Phạm Ngũ Lão Street", 1);
//            List<Address> nhtp = geoCoder.getFromLocationName("Saigon Opera House", 1);

//            LatLng ntdbLL = new LatLng(ntdb.get(0).getLatitude(), ntdb.get(0).getLongitude());
//            LatLng cbtLL = new LatLng(cbt.get(0).getLatitude(), cbt.get(0).getLongitude());
//            LatLng bdtpLL = new LatLng(bdtp.get(0).getLatitude(), bdtp.get(0).getLongitude());
//            LatLng sgzLL = new LatLng(sgz.get(0).getLatitude(), sgz.get(0).getLongitude());
//            LatLng dsLL = new LatLng(ds.get(0).getLatitude(), ds.get(0).getLongitude());
//            LatLng btxcLL = new LatLng(btxc.get(0).getLatitude(), btxc.get(0).getLongitude());
//            LatLng pnlLL = new LatLng(pnl.get(0).getLatitude(), pnl.get(0).getLongitude());
//            LatLng nhtpLL = new LatLng(nhtp.get(0).getLatitude(), nhtp.get(0).getLongitude());

//            mMap.addMarker(new MarkerOptions().position(ntdbLL).title("Nhà Thờ Đức Bà"));
//            mMap.addMarker(new MarkerOptions().position(cbtLL).title("Chợ Bến Thành"));
//            mMap.addMarker(new MarkerOptions().position(bdtpLL).title("Bưu Điện Thành Phố"));
//            mMap.addMarker(new MarkerOptions().position(sgzLL).title("Sở Thú"));
//            mMap.addMarker(new MarkerOptions().position(dsLL).title("Đầm Sen"));
//            mMap.addMarker(new MarkerOptions().position(btxcLL).title("Tòa Nhà Bitexco"));
//            mMap.addMarker(new MarkerOptions().position(pnlLL).title("Đường Phạm Ngũ Lão"));
//            mMap.addMarker(new MarkerOptions().position(nhtpLL).title("Nhà Hát Thành Phố"));
