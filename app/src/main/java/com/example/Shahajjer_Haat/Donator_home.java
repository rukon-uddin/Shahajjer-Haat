package com.example.Shahajjer_Haat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

public class Donator_home extends AppCompatActivity {
    private Button donateFood;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    public static final int PERMISSION_FINE_LOCATION = 99;
    public int PERMISSION_GRANTED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_home);

        donateFood = (Button) findViewById(R.id.button);

        donateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Donator_home.this, DonatorMap.class));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    PERMISSION_GRANTED = 1;
                }else{
                    PERMISSION_GRANTED = 0;
                    Toast.makeText(this, "App requires permision to fucntion properly", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

}