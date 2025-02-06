package com.app.tunetogether.az.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.tunetogether.az.FavouriteActivity;
import com.app.tunetogether.az.LoginActivity;
import com.app.tunetogether.az.ProfileActivity;
import com.app.tunetogether.az.R;
import com.app.tunetogether.az.chat.Fragments.MyFragmentPagerAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mauth;
    ViewPager mviewPager;
    MyFragmentPagerAdapter mFragmentPagerAdapter;
    TabLayout mtabLayout;
    DatabaseReference mDatabaseReference;
    AHBottomNavigation bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        mauth=FirebaseAuth.getInstance();

        mviewPager=(ViewPager)findViewById(R.id.viewPager);

        mFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(mFragmentPagerAdapter);
        mviewPager.setCurrentItem(1);

        mtabLayout=(TabLayout)findViewById(R.id.tabLayout);
        mtabLayout.setupWithViewPager(mviewPager);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");



        bottomNavigation = findViewById(R.id.bottom_nav);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.teal_200);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Chats", R.drawable.chat, R.color.teal_200);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Favourite", R.drawable.heart, R.color.teal_200);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Profile", R.drawable.user, R.color.teal_200);
        int qq = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._9sdp);
        int qqa = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp);
        bottomNavigation.setTitleTextSize(qqa,qq);


        bottomNavigation.setTitleTextSize(qqa,qq);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        bottomNavigation.setVisibility(View.VISIBLE);
        bottomNavigation.setAccentColor(Color.parseColor("#5742F4"));
        bottomNavigation.setInactiveColor(Color.parseColor("#838383"));
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#1A1A1A"));

        bottomNavigation.enableItemAtPosition(1);
        bottomNavigation.setCurrentItem(1);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.teal_200));
        bottomNavigation.setNotificationBackgroundColorResource(R.color.teal_200);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch(position) {

                    case 0:


                        Intent intent = new Intent(getApplicationContext(), com.app.tunetogether.az.MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);


                        break;

                    case 1:


                        Intent intent5 = new Intent(getApplicationContext(), com.app.tunetogether.az.chat.MainActivity.class);
                        startActivity(intent5);
                        overridePendingTransition(0, 0);

                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), FavouriteActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);


                        break;
                    case 3:
                        Intent intent6 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent6);
                        overridePendingTransition(0, 0);


                        break;

                }
                return true;
            }

        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Really Exit ??");
        builder.setTitle("Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",new MyListener());
        builder.setNegativeButton("Cancel",null);
        builder.show();

    }
    public class MyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mauth.getCurrentUser();
        if(user==null){
            startfn();
        }
        else{

        }
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);




        if(item.getItemId()==R.id.logout){
            mDatabaseReference.child(mauth.getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        FirebaseAuth.getInstance().signOut();
                        startfn();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Try again..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        return true;
    }

    private void startfn(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
