package com.example.Shahajjer_Haat;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.Shahajjer_Haat.databinding.ActivityAngelMapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class AngelMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityAngelMapBinding binding;
    DatabaseReference databaseReference;
    ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    Location location;
    Location angelLocation = new Location("");
    Location donatorLocation = new Location("");
    String userToken;

    private static ArrayList<Double> latitude = new ArrayList<>();
    private static ArrayList<Double> longitude = new ArrayList<>();
    private static final int REQUEST_CODE_FOR_FINE_LOCATION = 1234;
    private boolean PERMISSION = false;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAngelMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(AngelMap.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();

        } else {
            requestPermission();
        }
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(AngelMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AngelMap.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(AngelMap.this).setMessage("Required permission for location").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(AngelMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FOR_FINE_LOCATION);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(AngelMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FOR_FINE_LOCATION);
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
                if (!ActivityCompat.shouldShowRequestPermissionRationale(AngelMap.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {
                    PERMISSION = true;
                }

            }
        }
    }


    public void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                location = task.getResult();
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                LatLng latLng = new LatLng(lat, lon);
                try{
                    angelLocation.setLatitude(lat);
                    angelLocation.setLongitude(lon);
                }catch (Exception e){
                    Log.e("myError", ""+e);
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.75f));
            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        try{
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
        }catch (SecurityException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Map");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getValue().toString();
                Log.v("hello", String.valueOf(key.charAt(0)));

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    String lat = dataSnapshot.child("latitude").getValue().toString();
                    String lon = dataSnapshot.child("longitude").getValue().toString();

                    double lati = Double.parseDouble(lat);
                    double lonn = Double.parseDouble(lon);
                    try{
                        donatorLocation.setLatitude(lati);
                        donatorLocation.setLongitude(lonn);
                        double distance = angelLocation.distanceTo(donatorLocation);
                        if(distance < 2000){
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lati,lonn)).icon(BitmapDescriptorFactory.fromResource(R.drawable.foodmarker)));
                        }
                    }catch (Exception e){
                        Log.e("myError", ""+e);
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lati,lonn)).icon(BitmapDescriptorFactory.fromResource(R.drawable.foodmarker)));
                    }

                }

                dialog = new Dialog(AngelMap.this);
                dialog.setContentView(R.layout.donator_info_popup);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.popup_background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.popupAnimation;

                Button ok = dialog.findViewById(R.id.btn_okay);
                Button cancel = dialog.findViewById(R.id.btn_cancel);

                String title = "Angel Request";
                String message = "An angel has requested for your food Do you want to donate it now?";

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", title, message, getApplicationContext(), AngelMap.this);
                        notificationsSender.SendNotifications();
                        Toast.makeText(AngelMap.this, "A request has been sent please wait", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        String markertitle = "Angel";
                        dialog.show();

                        return false;
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}