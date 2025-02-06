package com.app.tunetogether.az.chat.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tunetogether.az.chat.ChatActivity;
import com.app.tunetogether.az.chat.ProfileActivity;
import com.app.tunetogether.az.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;



public class FriendFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    public FriendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_friend, container, false);

        mFriendsList = (RecyclerView)mMainView.findViewById(R.id.friendRecycleList);
        mAuth=FirebaseAuth.getInstance();

        mCurrent_user_id=mAuth.getCurrentUser().getUid();
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(mCurrent_user_id);
        mFriendDatabase.keepSynced(true);

        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Friends> options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(mFriendDatabase, Friends.class)
                .build();


        FirebaseRecyclerAdapter ff = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(options) {
            @Override
            public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_single_user, parent, false);


                return new FriendsViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(final FriendsViewHolder friendViewHolder, int position, final Friends friends) {
                friendViewHolder.setDate(friends.getDate());
                final String list_user_id=getRef(position).getKey();
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        final String userName=dataSnapshot.child("name").getValue().toString();
                        String userthumbImage=dataSnapshot.child("image").getValue().toString();

                        if(dataSnapshot.hasChild("online")){
                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendViewHolder.setOnline(userOnline);

                        }

                        friendViewHolder.setName(userName);
                        friendViewHolder.setUserImage(userthumbImage,getContext());


                        friendViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getContext(), ChatActivity.class);
                                intent.putExtra("user_id",list_user_id);
                                intent.putExtra("user_name",userName);
                                startActivity(intent);


                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }



        };

        ff.notifyDataSetChanged();
        mFriendsList.setAdapter(ff);
        ff.startListening();

    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }
        public void setDate(String date){

            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListStatus);
            userNameView.setText(date);

        }
        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListName);
            userNameView.setText(name);

        }
        public void setUserImage(String userThumbImage, Context ctx){
            RoundedImageView userImageview=mView.findViewById(R.id.circleImageViewUserImage);
            Picasso.get().load(userThumbImage).placeholder(R.drawable.user_img).into(userImageview);
        }
        public void setOnline(String isOnline){
            ImageView online=(ImageView)mView.findViewById(R.id.userSingleOnlineIcon);
            if(isOnline.equals("true")){
                online.setVisibility(View.VISIBLE);
            }
            else{
                online.setVisibility(View.INVISIBLE);
            }
        }
    }
}
