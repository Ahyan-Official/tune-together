package com.app.tunetogether.az.chat;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


public class MyApplication extends Application{

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate() {
        super.onCreate();

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();


        if(mUser!=null){

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            mAuth=FirebaseAuth.getInstance();
            Picasso.Builder builder = new Picasso.Builder(this);
           // builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(true);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);





       }

    }



}
