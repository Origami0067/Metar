package com.example.metar;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.metar.databinding.ActivityMapAirportsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MapAirports extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapAirportsBinding binding;
    String oaci;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        oaci = intent.getStringExtra("oaci");

        binding = ActivityMapAirportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(MapAirports.this);
        try {
            addressList = geocoder.getFromLocationName(oaci, 1);
            Address address = addressList.get(0);

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.addMarker(new MarkerOptions().position(latLng).title(oaci));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}