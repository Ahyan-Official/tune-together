package com.app.tunetogether.az;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class FavouriteActivity extends AppCompatActivity {


    AHBottomNavigation bottomNavigation;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);



        //Bottom navigation bar
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_nav);

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
        bottomNavigation.enableItemAtPosition(2);
        bottomNavigation.setCurrentItem(2);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.teal_200));
        bottomNavigation.setNotificationBackgroundColorResource(R.color.teal_200);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch(position) {

                    case 0:


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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


        //Firebase auth
        auth = FirebaseAuth.getInstance();
        //recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        com.google.firebase.firestore.Query query1 = FirebaseFirestore.getInstance().collection("events_favourite").whereEqualTo("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Reading all EventDBs
        FirestoreRecyclerOptions<EventDB> options = new FirestoreRecyclerOptions.Builder<EventDB>()
                .setQuery(query1, EventDB.class)
                .build();

        EventDBs(options);

    }



    public void EventDBs(FirestoreRecyclerOptions<EventDB> options){


        //Firebase adapter
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<EventDB, ViewHolder>(options) {
            @Override
            public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position, EventDB model) {

                Picasso.get().load(model.getImage()).into(holder.img);
                holder.tv_title.setText(model.getArtist_name());
                holder.tv_location.setText(model.getLocation()+" "+model.getCountry());


                Log.e("TAG", "onBindViewHolder: "+model.getName_venue() );
                holder.date.setText(model.getStarts_at());

                holder.tv_venue.setText(model.getName_venue());


                holder.btn_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(FavouriteActivity.this,ViewEventActivity.class);
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("artist_name", model.getArtist_name());
                        intent.putExtra("documentId", model.getDocumentId());
                        intent.putExtra("location", model.getLocation()+" "+model.getCountry());
                        intent.putExtra("lat", model.getLatitude());
                        intent.putExtra("lng", model.getLongitude());
                        intent.putExtra("start_at", model.getStarts_at());
                        intent.putExtra("venue_name", model.getName_venue());
                        startActivity(intent);



                    }
                });


               


            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.event_roww, group, false);

                return new ViewHolder(view);
            }
        };


        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title,date,tv_location,tv_venue;
        RoundedImageView img;
        CardView layout;
        Button btn_detail;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_venue = itemView.findViewById(R.id.tv_venue);

            btn_detail = itemView.findViewById(R.id.btn_detail);

            tv_title = itemView.findViewById(R.id.tv_title);
            date = itemView.findViewById(R.id.date);
            tv_location = itemView.findViewById(R.id.tv_location);
            layout = itemView.findViewById(R.id.layout);


        }


    }
}