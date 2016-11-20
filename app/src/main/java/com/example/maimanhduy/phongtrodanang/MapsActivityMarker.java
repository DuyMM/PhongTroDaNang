package com.example.maimanhduy.phongtrodanang;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivityMarker extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static ArrayList<BaiDangObject> ListMarker = new ArrayList<BaiDangObject>();
    ArrayList<String> listKeyArr = new ArrayList<String>();
    DatabaseReference mData;
    String TieuDe, DiaChi;
    List<String> arr = null;
    List<Address> address = null;
    ArrayList<String> title = new ArrayList<String>();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_marker);
        mData = FirebaseDatabase.getInstance().getReference();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void setDataListMarker() {
        mData.child("BaiDang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListMarker.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    listKeyArr.add(messageSnapshot.getKey());
                    TieuDe = (String) messageSnapshot.child("TieuDe").getValue();
                    DiaChi = (String) messageSnapshot.child("DiaChi").getValue();
                    title.add(TieuDe);
                    if (TieuDe == null) {
                        continue;
                    }
                    if (DiaChi == null) {
                        continue;
                    }
                    BaiDangObject marker = new BaiDangObject(TieuDe, "", DiaChi, "", "", "", "");
                    ListMarker.add(marker);
                }

                for (int i = 0; i < ListMarker.size(); i++) {
                    String keys = listKeyArr.get(i);
                    String titles = title.get(i);
                    if (ListMarker.get(i).DiaChi != null) {
                        Geocoder geocoder = new Geocoder(MapsActivityMarker.this);
                        try {
                            address = geocoder.getFromLocationName(ListMarker.get(i).DiaChi + "", 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address addres = address.get(0);
                        LatLng latLngT = new LatLng(addres.getLatitude(), addres.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLngT).title(titles).snippet(keys).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_green_600_24dp)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngT, 10));

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*
    public void addMarker() {
        EditText searchtext = (EditText) findViewById(R.id.textSearch);
        String location = searchtext.getText().toString();
        List<Address> addresses = null;
        if (location != null || location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addresses = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addresses.get(0);
            LatLng latLngT = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLngT).title(location+""));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLngT));
        } else {
            Toast.makeText(this, "Nhap lai", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setDataListMarker();
        mMap.setOnMarkerClickListener(this);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MapsActivityMarker.this, ChiTietBaiDang.class);
        myIntent.putExtra("key", marker.getSnippet());
        startActivity(myIntent);
        return false;
    }
}
