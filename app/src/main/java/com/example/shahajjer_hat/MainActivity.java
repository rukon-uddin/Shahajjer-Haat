package com.example.shahajjer_hat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //variables

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;
    //for swiping
    private LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //animation
        topAnim    = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim =AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image =findViewById(R.id.imageview1);
        logo =findViewById(R.id.textView);
        slogan=findViewById(R.id.textView2);
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(topAnim);
        //swiping

        new Handler().postDelayed(()->{
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }, 5*1000 );
    }
}