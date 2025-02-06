package com.app.tunetogether.az;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;
    FirebaseAuth auth;
    RecyclerView recyclerView,recyclerViewTop;
    public List<Event> eventArrayList = new ArrayList<>();
    EventsAdapter adapter;
    EventTopAdapter adapterTop;


    EditText et_search;
    public List<EventTop> eventTopArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_search =  findViewById(R.id.et_search);

        //bottom navigation
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
        bottomNavigation.enableItemAtPosition(0);
        bottomNavigation.setCurrentItem(0);
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

        if(auth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        recyclerView.setNestedScrollingEnabled(false);


        recyclerViewTop = findViewById(R.id.recyclerViewTop);
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTop.setNestedScrollingEnabled(false);


        //==Here
        EventTop eventtop = new EventTop("Zedd",R.drawable.zedd,"zedd");
        EventTop eventtop2 = new EventTop("Taylor Swift",R.drawable.taylor,"taylowswift");
        EventTop eventtop3 = new EventTop("Sam Smith",R.drawable.sam,"samsmith");
        EventTop eventtop4 = new EventTop("Ed Sheeran",R.drawable.ed,"edsheeran");
        EventTop eventtop5 = new EventTop("Katy Perry",R.drawable.katy,"katyperry");
        EventTop eventtop6 = new EventTop("Louis Tomlinson",R.drawable.louis,"LouisTomlinson");
        EventTop eventtop8 = new EventTop("Drake",R.drawable.drake,"drake");
        EventTop eventtop9 = new EventTop("Charlie Puth",R.drawable.charlie,"charlieputh");
        EventTop eventtop0 = new EventTop("The Weeknd",R.drawable.weekend,"TheWeeknd");

        String url = "https://rest.bandsintown.com/artists/taylorswift/events/?app_id=42154f2886cee560cda21d51d9de0243";
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//
//            public void run() {
//
//                events(url);
//
//
//
//            }
//        },5000);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("artists");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String nnnn="taylorswift";
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                        String name2 = postSnapshot.child("name").getValue().toString();
                        String username = postSnapshot.child("username").getValue().toString();

                        Toast.makeText(MainActivity.this, name2, Toast.LENGTH_SHORT).show();

                        nnnn = username;

                        EventTop eventtop = new EventTop(name2,R.drawable.zedd,username);

                        eventTopArrayList.add(eventtop);



                        // here you can access to name property like university.name

                    }
                    Log.e("eeeee", "onDataChange: "+nnnn );

//                    String url = "https://rest.bandsintown.com/artists/"+nnnn+"/events/?app_id=42154f2886cee560cda21d51d9de0243";
//                    events(url);

                    topArtist(nnnn);


                    recyclerViewTop.setAdapter(new EventTopAdapter(MainActivity.this,eventTopArrayList, new EventTopAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(EventTop item) {

                            topArtist(item.getUsername());
                            Toast.makeText(MainActivity.this, item.getName()+" Selected", Toast.LENGTH_LONG).show();

                        }


                    }));


//                    String url = "https://rest.bandsintown.com/artists/"+nnnn+"/events/?app_id=42154f2886cee560cda21d51d9de0243";
//                    events(url);

                }else{
                    eventTopArrayList.add(eventtop);
                    eventTopArrayList.add(eventtop2);
                    eventTopArrayList.add(eventtop3);
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                    recyclerViewTop.setAdapter(new EventTopAdapter(MainActivity.this,eventTopArrayList, new EventTopAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(EventTop item) {

                            topArtist(item.getUsername());
                            Toast.makeText(MainActivity.this, item.getName()+" Selected", Toast.LENGTH_LONG).show();

                        }


                    }));


                    String url = "https://rest.bandsintown.com/artists/"+"taylowswift"+"/events/?app_id=42154f2886cee560cda21d51d9de0243";
                    events(url);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        //StringBuilder url = new StringBuilder("https://rest.bandsintown.com/artists/taylorswift/events/?app_id=42154f2886cee560cda21d51d9de0243");





        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                adapter.getFilter().filter(editable.toString());
            }
        });

    }

    public void  events(String url){
        eventArrayList = new ArrayList<Event>();
        eventArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            // Get current json Array
                            //JSONArray profile = new JSONArray(response);

                            for(int i=0;i<response.length();i++){

                                JSONObject jresponse = response.getJSONObject(i);
                                JSONObject venue = jresponse.getJSONObject("venue");
                                String location = venue.getString("location");
                                String name_venue = venue.getString("name");

                                String latitude = venue.getString("latitude");
                                String longitude = venue.getString("longitude");
                                String country = venue.getString("country");

                                String starts_at = jresponse.getString("starts_at");
                                Log.d("ooooo", "onResponse: "+starts_at);
                                String id = jresponse.getString("id");
                                Log.d("ooooo", "onResponse: "+starts_at);


                                JSONObject artist = jresponse.getJSONObject("artist");
                                String image_url = artist.getString("image_url");
                                String artist_name = artist.getString("name");


                                Log.e("TAG", "onResponse: ");
                                Event event = new Event(location,name_venue,latitude,longitude,country, starts_at,id,image_url,artist_name);

                                eventArrayList.add(event);


                            }

                            adapter = new EventsAdapter(MainActivity.this, eventArrayList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();




//                            nick = jresponse.getString("nick");
//                            age = jresponse.getString("age");
//                            city = jresponse.getString("city");
//                            mainpic = jresponse.getString("mainpic");
//                            numpics = jresponse.getString("numpics");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                        Log.e("ll", "onErrorResponse: "+error.getMessage());
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


    }

    public void topArtist(String name){
        StringBuilder url = new StringBuilder("https://rest.bandsintown.com/artists/"+name+"/events/?app_id=42154f2886cee560cda21d51d9de0243");




        eventArrayList = new ArrayList<Event>();
        eventArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url.toString(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            // Get current json Array
                            //JSONArray profile = new JSONArray(response);

                            for(int i=0;i<response.length();i++){

                                JSONObject jresponse = response.getJSONObject(0);
                                JSONObject venue = jresponse.getJSONObject("venue");
                                String location = venue.getString("location");
                                String name_venue = venue.getString("name");

                                String latitude = venue.getString("latitude");
                                String longitude = venue.getString("longitude");
                                String country = venue.getString("country");

                                String starts_at = jresponse.getString("starts_at");
                                String id = jresponse.getString("id");


                                JSONObject artist = jresponse.getJSONObject("artist");
                                String image_url = artist.getString("image_url");
                                String artist_name = artist.getString("name");

                                Event event = new Event(location,name_venue,latitude,longitude,country, starts_at,id,image_url,artist_name);

                                eventArrayList.add(event);


                            }

                            Log.e("ddddd", "onResponse " +eventArrayList.size());
                            adapter = new EventsAdapter(MainActivity.this, eventArrayList);
                            recyclerView.setAdapter(adapter);

//                            int secondsDelayed = 3;
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//
//                                }
//                            }, secondsDelayed * 1000);
//




//                            nick = jresponse.getString("nick");
//                            age = jresponse.getString("age");
//                            city = jresponse.getString("city");
//                            mainpic = jresponse.getString("mainpic");
//                            numpics = jresponse.getString("numpics");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                        Log.d("ll", "onErrorResponse: "+error.getMessage());
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }




    //View holder class for recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Views
        View mView;
        TextView tvDes,tvDate;
        TextView tvName,btnEdit,btnDelete;
        ImageView img;
        LinearLayout lladmin;

        public ViewHolder(View itemView) {
            super(itemView);
            mView =itemView;

            //init
            lladmin = mView.findViewById(R.id.lladmin);
            tvDes = mView.findViewById(R.id.tvDes);
            tvName = mView.findViewById(R.id.tvName);
            img = mView.findViewById(R.id.img);
            tvDate = mView.findViewById(R.id.tvDate);
            btnEdit = mView.findViewById(R.id.btnEdit);
            btnDelete = mView.findViewById(R.id.btnDelete);



        }



    }
}