package com.example.Shahajjer_Haat;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.Shahajjer_Haat.databinding.ActivityDonatorMapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonatorMap extends FragmentActivity implements OnMapReadyCallback {

    private Button msetLocation;
    private GoogleMap map;
    private ActivityDonatorMapBinding binding;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastKnownLocation;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference lat_ref, lon_ref;
    private static final int REQUEST_CODE_FOR_FINE_LOCATION = 99;
    private boolean PERMISSION = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDonatorMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        msetLocation = (Button) findViewById(R.id.donatemap_setLocation);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if (ContextCompat.checkSelfPermission(DonatorMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }


        msetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();

                String lat = String.valueOf(latitude);
                String lon = String.valueOf(longitude);
                String currentUserId = firebaseAuth.getCurrentUser().getUid();
                lat_ref = database.getReference("Map");
                lat_ref.child(currentUserId).child("latitude").setValue(lat);

                lon_ref = database.getReference("Map");
                lon_ref.child(currentUserId).child("longitude").setValue(lon);

                Toast.makeText(DonatorMap.this, "Location added successfully", Toast.LENGTH_SHORT).show();

                map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.foodmarker)));

            }
        });
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(DonatorMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DonatorMap.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(DonatorMap.this).setMessage("Required permission for location").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(DonatorMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FOR_FINE_LOCATION);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(DonatorMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FOR_FINE_LOCATION);
            }

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_FOR_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PERMISSION = true;

            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(DonatorMap.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {
                    PERMISSION = true;
                }

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        this.map = googleMap;

        updateLocationUI();

        getDeviceLocation();

        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();


    }

    private void updateLocationUI(){
        if(map==null){
            return;
        }
        try{
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }catch (SecurityException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getDeviceLocation(){
        try{

            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()){
                        lastKnownLocation = task.getResult();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15));
                    }else{
                        Toast.makeText(DonatorMap.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }catch (SecurityException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}