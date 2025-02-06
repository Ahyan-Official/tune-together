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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tunetogether.az.chat.ProfileActivity;
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

public class ParticipantsActivity extends AppCompatActivity {


    FirebaseAuth auth;
    RecyclerView recyclerView;

    String documentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);


        documentId = getIntent().getStringExtra("documentId");


        auth = FirebaseAuth.getInstance();
        //recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        com.google.firebase.firestore.Query query1 = FirebaseFirestore.getInstance().collection("events_participated").whereEqualTo("documentId",documentId);
        FirestoreRecyclerOptions<Participant> options = new FirestoreRecyclerOptions.Builder<Participant>()
                .setQuery(query1, Participant.class)
                .build();

        events(options);

    }


    public void events(FirestoreRecyclerOptions<Participant> options){


        //Firebase adapter
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Participant, ViewHolder>(options) {
            @Override
            public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position, Participant model) {




                if(model.getUserId().equals(FirebaseAuth.getInstance().getUid())){

                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

                }else{
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                }
                //Check if event is added in favourite
                DatabaseReference asdasdas = FirebaseDatabase.getInstance().getReference().child("users").child(model.getUserId());
                asdasdas.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            String name = snapshot.child("name").getValue().toString();
                            holder.tv_name.setText(name);
                            String image = snapshot.child("image").getValue().toString();

                            Picasso.get().load(image).placeholder(R.drawable.loadw).error(R.drawable.loadw).into(holder.img);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //loading image



                //on clicking image
                holder.btn_connect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Goto View event
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("user_id",model.getUserId());

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
                        .inflate(R.layout.participate_row, group, false);

                return new ViewHolder(view);
            }
        };


        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView tv_name;
        RoundedImageView img;
        TextView btn_connect;

        public ViewHolder(View itemView) {
            super(itemView);
            mView =itemView;

            btn_connect = mView.findViewById(R.id.btn_connect);
            tv_name = mView.findViewById(R.id.tv_name);
            img = mView.findViewById(R.id.img);




        }



    }

}