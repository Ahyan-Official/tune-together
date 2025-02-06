package com.app.tunetogether.az;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class PreferenceActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView recyclerView;
    RelativeLayout zedd,ts,drake,sam,ed,katy,louis,weekend,charlie;
    RoundedImageView tick_ty,tick_drake,tick_sam,tick_ed,tick_katy,tick_louis,tick_weeknd,tick_charlie, tick_zedd;
    //selecting and de-selcting is workinging on
    boolean b_zedd= true;
    boolean b_ty= true;
    boolean b_drake= true;
    boolean b_sam= true;
    boolean b_ed= true;
    boolean b_katy= true;
    boolean b_louis= true;
    boolean b_weeknd= true;
    boolean b_charlie= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("artists");


        getSupportActionBar().setTitle("Preference");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tick_ty = findViewById(R.id.tick_ty);
        tick_drake = findViewById(R.id.tick_drake);
        tick_sam = findViewById(R.id.tick_sam);
        tick_ed = findViewById(R.id.tick_ed);
        tick_katy = findViewById(R.id.tick_katy);
        tick_louis = findViewById(R.id.tick_louis);
        tick_weeknd = findViewById(R.id.tick_weeknd);
        tick_charlie = findViewById(R.id.tick_charlie);
        tick_charlie = findViewById(R.id.tick_charlie);
        tick_zedd = findViewById(R.id.tick_zedd);

        //recyclerview
        zedd = findViewById(R.id.zedd);
        ts = findViewById(R.id.ts);
        drake = findViewById(R.id.drake);
        sam = findViewById(R.id.sam);
        ed = findViewById(R.id.ed);
        katy = findViewById(R.id.katy);
        louis = findViewById(R.id.louis);
        weekend = findViewById(R.id.weekend);
        charlie = findViewById(R.id.charlie);


       // events(options);

        zedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_zedd){
                    tick_zedd.setVisibility(View.VISIBLE);
                    databaseReference.child("zedd").child("name").setValue("Zedd");
                    databaseReference.child("zedd").child("username").setValue("Zedd");

                }else{
                    tick_zedd.setVisibility(View.GONE);
                    databaseReference.child("zedd").removeValue();

                }
                b_zedd = !b_zedd;

            }
        });

        ts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_ty){
                    tick_ty.setVisibility(View.VISIBLE);
                    databaseReference.child("taylorswift").child("name").setValue("Taylor Swift");
                    databaseReference.child("taylorswift").child("username").setValue("taylorswift");

                }else{
                    tick_ty.setVisibility(View.GONE);
                    databaseReference.child("taylorswift").removeValue();

                }
                b_ty = !b_ty;

            }
        });
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_ed){
                    tick_ed.setVisibility(View.VISIBLE);
                    databaseReference.child("edsheeran").child("name").setValue("Ed Sheeran");
                    databaseReference.child("edsheeran").child("username").setValue("edsheeran");

                }else{
                    tick_ed.setVisibility(View.GONE);
                    databaseReference.child("edsheeran").removeValue();

                }
                b_ed = !b_ed;

            }
        });

        sam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_sam){
                    tick_sam.setVisibility(View.VISIBLE);
                    databaseReference.child("samsmith").child("name").setValue("samsmith");
                    databaseReference.child("samsmith").child("username").setValue("Sam Smith");

                }else{
                    tick_sam.setVisibility(View.GONE);
                    databaseReference.child("samsmith").removeValue();

                }
                b_sam = !b_sam;

            }
        });

        charlie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_charlie){
                    tick_charlie.setVisibility(View.VISIBLE);
                    databaseReference.child("charlieputh").child("name").setValue("Charlie Puth");
                    databaseReference.child("charlieputh").child("username").setValue("charlieputh");

                }else{
                    tick_charlie.setVisibility(View.GONE);
                    databaseReference.child("charlieputh").removeValue();

                }
                b_charlie = !b_charlie;

            }
        });

        drake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_drake){
                    tick_drake.setVisibility(View.VISIBLE);
                    databaseReference.child("drake").child("name").setValue("drake");
                    databaseReference.child("drake").child("username").setValue("drake");

                }else{
                    tick_drake.setVisibility(View.GONE);
                    databaseReference.child("drake").removeValue();

                }
                b_drake = !b_drake;

            }
        });


        katy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_katy){
                    tick_katy.setVisibility(View.VISIBLE);
                    databaseReference.child("katyperry").child("name").setValue("Katy Parry");
                    databaseReference.child("katyperry").child("username").setValue("katyperry");

                }else{
                    tick_katy.setVisibility(View.GONE);
                    databaseReference.child("katyperry").removeValue();

                }
                b_katy = !b_katy;

            }
        });

        louis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_louis){
                    tick_louis.setVisibility(View.VISIBLE);
                    databaseReference.child("louis").child("name").setValue("Louis Tomlinson");
                    databaseReference.child("louis").child("username").setValue("LouisTomlinson");

                }else{
                    tick_louis.setVisibility(View.GONE);
                    databaseReference.child("louis").removeValue();

                }
                b_louis = !b_louis;

            }
        });

        weekend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(b_weeknd){
                    tick_weeknd.setVisibility(View.VISIBLE);
                    databaseReference.child("weeknd").child("name").setValue("The Weeknd");
                    databaseReference.child("weeknd").child("username").setValue("TheWeeknd");


                }else{
                    tick_weeknd.setVisibility(View.GONE);
                    databaseReference.child("weeknd").removeValue();

                }
                b_weeknd = !b_weeknd;

            }
        });
    }


    public void events(FirestoreRecyclerOptions<Items> options){


        //Firebase adapter
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Items, MainActivity.ViewHolder>(options) {
            @Override
            public void onBindViewHolder(MainActivity.ViewHolder holder, @SuppressLint("RecyclerView") int position, Items model) {

                //setting name
                holder.tvName.setText(model.getTitle());
                //setting description
                holder.tvDes.setText(model.getDes());
                //setting date
                holder.tvDate.setText(model.getDate());

                //Check if event is added in favourite
                DatabaseReference asdasdas = FirebaseDatabase.getInstance().getReference().child("favourite").child(auth.getCurrentUser().getUid()).child(model.getId());
                asdasdas.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            holder.itemView.setVisibility(View.VISIBLE);

                        }else{

                            holder.itemView.setVisibility(View.GONE);
                            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                            params.height = 0;
                            params.width = 0;
                            holder.itemView.setLayoutParams(params);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //loading image
                Picasso.get().load(model.getPic()).placeholder(R.drawable.loadw).error(R.drawable.loadw).into(holder.img);



                //on clicking image
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Goto View event
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        String documentId = getSnapshots().getSnapshot(position).getId();
                        intent.putExtra("documentId",documentId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                });


            }

            @NonNull
            @Override
            public MainActivity.ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.event_row, group, false);

                return new MainActivity.ViewHolder(view);
            }
        };


        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView tvDes;
        TextView tvName;
        ImageView img;
        LinearLayout lladmin;

        public ViewHolder(View itemView) {
            super(itemView);
            mView =itemView;

            lladmin = mView.findViewById(R.id.lladmin);
            tvDes = mView.findViewById(R.id.tvDes);
            tvName = mView.findViewById(R.id.tvName);
            img = mView.findViewById(R.id.img);




        }



    }
}