package com.example.tinvo.sampleapp;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    String[] places = {"Nhà Thờ Đức Bà", "Chợ Bến Thành", "Saigon Post Office", "Sài Gòn Zoo", "Bitexco Financial Tower", "Phạm Ngũ Lão Street", "Bến Nhà Rồng", "Công Viên Văn Hóa Đầm Sen", "Địa Đạo Củ Chi", "Khu Du Lịch Văn Thánh",
                       "Công Viên Gia Định", "Công Viên Lê Thị Riêng", "Dinh Độc Lập"};
//                        "Ha Long Bay", "Cu Chi Tunnels", "Hoan Kiem Lake", "War Remnants Museum", "Phong Nha Kẻ Bàng", "Phú Quốc", "Fansipan", "Hanoi Opera House", "Ba Be National Park",
//                        "Long son Pagoda", "Cát Tiên National Park", "hoa lo prison", "hoi an", "nha trang", "hue"};

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
        try {
            // Get the city and its Lat Lng
            List<Address> addresses = geoCoder.getFromLocationName(city, 1);
            Double lat = addresses.get(0).getLatitude();
            Double lng = addresses.get(0).getLongitude();
            LatLng cityLatLng = new LatLng(lat, lng);

            // Position the camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, 10));

            // Add to cluster
            mClusterManager = new ClusterManager<MyItem>(this, mMap);
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);

            for(String place: places) {
                List<Address> temp = geoCoder.getFromLocationName(place, 1);
                Double latTmp = temp.get(0).getLatitude();
                Double lngTmp = temp.get(0).getLongitude();
                MyItem item = new MyItem(latTmp, lngTmp, place, "");
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
