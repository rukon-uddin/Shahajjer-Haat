package com.example.Shahajjer_Haat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Angel_signin_reg extends AppCompatActivity {
    private Button singin, register;
    private EditText memail, mpassword;

    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener firebaseAuthListener;
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mAuth.removeAuthStateListener(firebaseAuthListener);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(firebaseAuthListener);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angel_signin_reg);

        mAuth = FirebaseAuth.getInstance();

//        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if(user!=null){
//                    startActivity(new Intent(Angel_signin_reg.this, Angel_home.class));
//                }
//            }
//        };

        singin = (Button) findViewById(R.id.angel_signin1);
        register = (Button) findViewById(R.id.angel_register);

        memail = (EditText) findViewById(R.id.angel_email1);
        mpassword = (EditText) findViewById(R.id.angel_pass1);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = memail.getText().toString();
                final String password = mpassword.getText().toString();

                //******Chnage
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Angel_signin_reg.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(Angel_signin_reg.this, "Sign up error", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivity(new Intent(Angel_signin_reg.this, Angel_home.class));
                        Toast.makeText(Angel_signin_reg.this, "Account registered successfully", Toast.LENGTH_SHORT).show();
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("User").child("Angel").child(user_id);
                        currentUserDb.setValue(true);
                    }
                });
            }
        });

        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = memail.getText().toString();
                final String password = mpassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Angel_signin_reg.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(Angel_signin_reg.this, "Sign in error", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivity(new Intent(Angel_signin_reg.this, Angel_home.class));
                    }
                });
            }
        });



    }
}