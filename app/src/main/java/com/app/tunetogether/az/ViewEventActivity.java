package com.app.tunetogether.az;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView pickImage;
    TextView etTitle;
    Button participate_btn;
    Context context;
    FirebaseStorage storage;
    private StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String documentId;
    ProgressDialog pd2;
    String pic;
    String userid;
    double lat,lng;
    Button fav_btn,btn_connect;

    String image,artist_name,location,latstr,lngstr,start_at,venue_name;
    TextView tv_venue,date,tv_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        context = this;
        pd2 = new ProgressDialog(this);
        documentId = getIntent().getStringExtra("documentId");
        image = getIntent().getStringExtra("image");
        artist_name = getIntent().getStringExtra("artist_name");
        location = getIntent().getStringExtra("location");
        latstr = getIntent().getStringExtra("lat");
        lngstr = getIntent().getStringExtra("lng");

        start_at = getIntent().getStringExtra("start_at");
        venue_name = getIntent().getStringExtra("venue_name");

//        ActionBar actionBar = getSupportActionBar();
//            actionBar.setTitle("Event Details");
//        actionBar.setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(context);
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        auth = FirebaseAuth.getInstance();
        pickImage = findViewById(R.id.pickImage);
        etTitle = findViewById(R.id.etTitle);
        fav_btn = findViewById(R.id.fav_btn);
        btn_connect = findViewById(R.id.btn_connect);


        tv_venue = findViewById(R.id.tv_venue);
        date = findViewById(R.id.date);
        tv_location = findViewById(R.id.tv_location);

        userid  = auth.getCurrentUser().getUid();



        //Maps fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        participate_btn = findViewById(R.id.participate_btn);


        tv_location.setText(location);
        date.setText(start_at);
        tv_venue.setText(venue_name);
        etTitle.setText(artist_name);
        Picasso.get().load(image).into(pickImage);


        //Loading data of event
//        db.collection("events").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    return;
//                }
//
//                //checking if data exist in database
//                if (snapshot != null && snapshot.exists()) {
//
//                    userid = snapshot.get("userid").toString();
//                    /**
//                     * check pic
//                     */
//
//                    if(snapshot.contains("pic")){
//
//                        pic = snapshot.get("pic").toString();
//                        Picasso.get().load(pic).placeholder(R.drawable.loadw).into(pickImage);
//
//
//                    }
//                    /**
//                     * check title
//                     */
//
//                    if(snapshot.contains("title")){
//
//                        String title = snapshot.get("title").toString();
//                        etTitle.setText(title);
//
//                    }
//
//                    /**check des
//                     *
//                     */
//                    if(snapshot.contains("des")){
//
//                        String transport = snapshot.get("des").toString();
//                      //  etDes.setText(transport);
//
//                    }
//                    /**check lat and lng
//                     *
//                     */
//                    if(snapshot.contains("lat") && snapshot.contains("lng")){
//
//                        String latt = snapshot.get("lat").toString();
//                        String lngg = snapshot.get("lng").toString();
//
//                        lat = Double.parseDouble(latt);
//                        lng = Double.parseDouble(lngg);
//
//                    }
//                }else{
//
//
//                }
//
//
//
//            }
//
//
//
//
//        });
//



        /**Participate Check
         *
         */
        DatabaseReference aa = FirebaseDatabase.getInstance().getReference().child("participated").child(auth.getCurrentUser().getUid());
        aa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    /**check if event id exist in "participated"
                     *
                     */
                    if(snapshot.hasChild(documentId)){

                        participate_btn.setText("Leave Event");

                    }else{
                        participate_btn.setText("Participate");

                    }
                }else{
                    participate_btn.setText("Participate");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /**favourite check
         *
         */
        DatabaseReference aafav = FirebaseDatabase.getInstance().getReference().child("favourite").child(auth.getCurrentUser().getUid());
        aafav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    /**
                     * check if event id exist in "favourite"
                     *
                     */
                    if(snapshot.hasChild(documentId)){

                        fav_btn.setText("Remove Favourite");

                    }else{
                        fav_btn.setText("Add to Favourite");

                    }
                }else{
                    fav_btn.setText("Add to Favourite");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),ParticipantsActivity.class);
                intent.putExtra("documentId",documentId);
                startActivity(intent);
            }
        });


        /**On clicking favourite button
         *
         */
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(auth.getCurrentUser().getUid()!=null && userid!=null){



                    if(fav_btn.getText().toString().equalsIgnoreCase("Add to Favourite")){

                        /**Add child in favourite
                         *
                         */
                        DatabaseReference aa = FirebaseDatabase.getInstance().getReference().child("favourite").child(auth.getCurrentUser().getUid());
                        aa.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){

                                    if(snapshot.hasChild(documentId)){

                                    }else{
                                        FirebaseDatabase.getInstance().getReference().child("favourite").child(auth.getCurrentUser().getUid()).child(documentId).setValue(documentId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                add_fav();
                                                Toast.makeText(getApplicationContext(), "Added to Favourite", Toast.LENGTH_SHORT).show();



                                            }
                                        });

                                    }
                                }else{
                                    FirebaseDatabase.getInstance().getReference().child("favourite").child(auth.getCurrentUser().getUid()).child(documentId).setValue(documentId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            add_fav();

                                            Toast.makeText(getApplicationContext(), "Added to Favourite", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else if (fav_btn.getText().toString().equalsIgnoreCase("Remove Favourite")){

                        FirebaseDatabase.getInstance().getReference().child("favourite").child(auth.getCurrentUser().getUid()).child(documentId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                remove_fav();
                                Toast.makeText(ViewEventActivity.this, "Removed from favourite", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }


                }else{

                    Toast.makeText(getApplicationContext(),"no user login",Toast.LENGTH_SHORT).show();

                }


            }
        });


        /**on clicking participate button
         *
         */
        participate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(auth.getCurrentUser().getUid()!=null && userid!=null){

                    if(participate_btn.getText().toString().equalsIgnoreCase("participate")){
                        DatabaseReference aa = FirebaseDatabase.getInstance().getReference().child("participated").child(auth.getCurrentUser().getUid());
                        aa.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                /** Check if event id exist
                                 *
                                 */
                                if(snapshot.exists()){

                                    if(snapshot.hasChild(documentId)){

                                    }else{
                                        /**
                                         * Add joined
                                         */
                                        FirebaseDatabase.getInstance().getReference().child("participated").child(auth.getCurrentUser().getUid()).child(documentId).setValue(documentId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                //display toast
                                                Toast.makeText(getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                                                add_part();
                                            }
                                        });

                                    }
                                }else{

                                    /**
                                     * if user dont exist then "joined" too
                                     */
                                    FirebaseDatabase.getInstance().getReference().child("participated").child(auth.getCurrentUser().getUid()).child(documentId).setValue(documentId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            /**display toast
                                             *
                                             */

                                            add_part();

                                            Toast.makeText(getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        /**
                         * if equals to "leave event", delete entry
                         */
                    }else if (participate_btn.getText().toString().equalsIgnoreCase("Leave Event")){

                        /**
                         * Delete
                         */
                        FirebaseDatabase.getInstance().getReference().child("participated").child(auth.getCurrentUser().getUid()).child(documentId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {

                                 remove_part();
                                 Toast.makeText(ViewEventActivity.this, "Left", Toast.LENGTH_SHORT).show();



                             }
                         });


                    }


                }else{

                    //no user toast
                    Toast.makeText(getApplicationContext(),"no user login",Toast.LENGTH_SHORT).show();

                }


            }
        });






    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        /**
         * Read event lat lng from dataabse
         */
        db.collection("events").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {


                    if(snapshot.contains("lat") && snapshot.contains("lng")){

                        String latt = snapshot.get("lat").toString();
                        String lngg = snapshot.get("lng").toString();

                        lat = Double.parseDouble(latt);
                        lng = Double.parseDouble(lngg);
                        /**
                         * add lat lag to marker
                         */
                        MarkerOptions markerOptions = new MarkerOptions();

                        LatLng latLng = new LatLng(lat,lng);
                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Clears the previously touched position
                        googleMap.clear();

                        // Animating to the touched position
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        googleMap.addMarker(markerOptions);
                    }
                }



            }




        });


    }

    public void add_fav(){


        Map<String, Object> doc = new HashMap<>();
        doc.put("documentId", documentId);
        doc.put("image", image);
        doc.put("artist_name", artist_name);
        doc.put("location",location);
        doc.put("lat", latstr);
        doc.put("lng", lngstr);
        doc.put("starts_at", start_at);
        doc.put("name_venue", venue_name);
        doc.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());



        FirebaseFirestore.getInstance().collection("events_favourite").whereEqualTo("documentId",documentId).whereEqualTo("userId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            QuerySnapshot document = task.getResult();
                            if(document.size()==0){

                                FirebaseFirestore.getInstance().collection("events_favourite").document().set(doc, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                        Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }

                    }
                });
    }


    public void add_part(){

        Map<String, Object> city = new HashMap<>();
        city.put("documentId", documentId);
        city.put("image", image);
        city.put("artist_name", artist_name);
        city.put("location",location);
        city.put("lat", latstr);
        city.put("lng", lngstr);
        city.put("starts_at", start_at);
        city.put("name_venue", venue_name);
        city.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());



        FirebaseFirestore.getInstance().collection("events_participated").whereEqualTo("documentId",documentId).whereEqualTo("userId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            QuerySnapshot document = task.getResult();
                            if(document.size()==0){

                                FirebaseFirestore.getInstance().collection("events_participated").document().set(city, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                        Toast.makeText(context, "Added to Participated", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }

                    }
                });

    }
    public void remove_part(){




        FirebaseFirestore.getInstance().collection("events_participated").whereEqualTo("documentId",documentId).whereEqualTo("userId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    QuerySnapshot document = task.getResult();
                                    if(document.size()!=0){

                                        DocumentReference d = document.getDocuments().get(0).getReference();
                                        d.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context, "Added to Participated", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }
                                }

                            }
                        });


    }



    public void remove_fav(){

        FirebaseFirestore.getInstance().collection("events_favourite").whereEqualTo("documentId",documentId).whereEqualTo("userId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            QuerySnapshot document = task.getResult();
                            if(document.size()!=0){

                                DocumentReference d = document.getDocuments().get(0).getReference();
                                d.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Added to Favourite", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }

                    }
                });


    }

}
