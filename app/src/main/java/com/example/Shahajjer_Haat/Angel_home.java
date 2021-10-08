package com.example.Shahajjer_Haat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

public class Angel_home extends AppCompatActivity {

    private Button BnearbyDonators;
    private TextView mlatitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    public static final int PERMISSION_FINE_LOCATION = 99;
    private int PERMISSION_granted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angel_home);
        BnearbyDonators = (Button) findViewById(R.id.angel_home_nearbyDonators_bt);


        BnearbyDonators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Angel_home.this, AngelMap.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){


                }else{
                    PERMISSION_granted = 0;
                    Toast.makeText(this, "App requires permision to fucntion properly", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


}