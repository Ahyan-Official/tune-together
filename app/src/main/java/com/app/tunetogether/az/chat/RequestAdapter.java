package com.app.tunetogether.az.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.tunetogether.az.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;



public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    private List<String> requestList;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference ;

    private Context ctx;

    public RequestAdapter(List<String> requestList) {
        this.requestList = requestList;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_single_user,parent,false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        return new RequestViewHolder(view);

    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName;
        public TextView displayStatus;
        public RoundedImageView displayImage;
        public ImageView imageView;

        public RequestViewHolder(View itemView) {
            super(itemView);

            ctx = itemView.getContext();

            displayName = (TextView)itemView.findViewById(R.id.textViewSingleListName);
            displayStatus = (TextView) itemView.findViewById(R.id.textViewSingleListStatus);
            displayImage = itemView.findViewById(R.id.circleImageViewUserImage);
            imageView = (ImageView)itemView.findViewById(R.id.userSingleOnlineIcon);
            imageView.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(ctx, com.app.tunetogether.az.chat.ProfileActivity.class);
                    intent.putExtra("user_id",requestList.get(pos));
                    ctx.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {

        String user_id = requestList.get(position);
        mDatabaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.child("name").getValue().toString();
                String userThumbImage = dataSnapshot.child("image").getValue().toString();
               // String userStatus =dataSnapshot.child("status").getValue().toString();

                holder.displayName.setText(userName);
                //holder.displayStatus.setText(userStatus);
                //Picasso.with(holder.displayImage.getContext()).load(userThumbImage).placeholder(R.drawable.user_img).into(holder.displayImage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
