package com.app.tunetogether.az;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {


    //Firebase auth
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        auth = FirebaseAuth.getInstance();


        //====== Delay of 2 seconds =========
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {

                if(auth.getCurrentUser()!=null){

                    // if user exist go to main activity
                    Intent intent  = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{

                    //Else go to login
                    Intent intent  = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();

                }


            }
        },2000);
    }


}