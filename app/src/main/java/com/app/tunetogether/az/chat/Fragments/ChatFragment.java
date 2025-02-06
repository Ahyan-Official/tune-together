package com.app.tunetogether.az.chat.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tunetogether.az.chat.ChatActivity;
import com.app.tunetogether.az.chat.Conv;
import com.app.tunetogether.az.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;



public class ChatFragment extends Fragment {

    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_chat, container, false);

        mConvList = (RecyclerView)mMainView.findViewById(R.id.chatRecycleList);

        mAuth= FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(mCurrent_user_id);

        mConvDatabase.keepSynced(true);

        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query conversationQuery = mConvDatabase.orderByChild("time_stamp");
        FirebaseRecyclerOptions<Conv> options = new FirebaseRecyclerOptions.Builder<Conv>().setQuery(conversationQuery, Conv.class)
                .build();


        FirebaseRecyclerAdapter ff = new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(options) {
            @Override
            public ConvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_single_user, parent, false);


                return new ConvViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(final ConvViewHolder convViewHolder, int position, final Conv users) {
                final String list_user_id=getRef(position).getKey();
                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);


                Log.e("lpllp",list_user_id);
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("image").getValue().toString();

                        convViewHolder.setName(userName);

                        convViewHolder.setUserImage(userThumb,getContext());

                        //--OPENING CHAT ACTIVITY FOR CLICKED USER----
                        convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.e("pppp", "onClick: "+list_user_id);
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("user_id",list_user_id);
                                chatIntent.putExtra("user_name",userName);
                                startActivity(chatIntent);
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
        mConvList.setAdapter(ff);
        ff.startListening();

    }

    //--- DATA IS ADDING WITHIN SINGLE HOLDER-----
    public static class ConvViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ConvViewHolder(View itemView) {
            super(itemView);
            mView =itemView;
        }


        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListName);
            userNameView.setText(name);
        }


        public void setUserImage(String userThumb, Context context) {

            RoundedImageView userImageView = mView.findViewById(R.id.circleImageViewUserImage);

            //--SETTING IMAGE FROM USERTHUMB TO USERIMAGEVIEW--- IF ERRORS OCCUR , ADD USER_IMG----
            Picasso.get().load(userThumb).placeholder(R.drawable.user_img).into(userImageView);
        }



    }

}
