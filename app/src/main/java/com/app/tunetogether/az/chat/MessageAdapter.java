package com.app.tunetogether.az.chat;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;



public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference ;
    Context context;

    public MessageAdapter(List<Messages> mMessagesList) {
        this.mMessagesList = mMessagesList;
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test123,parent,false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        return new MessageViewHolder(view);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView displayName;
        public TextView displayTime;
        public RoundedImageView profileImage;
        public ImageView messageImage;


        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            displayName = (TextView)itemView.findViewById(R.id.name_text_layout);
            displayTime = (TextView) itemView.findViewById(R.id.time_text_layout);
            profileImage = itemView.findViewById(R.id.message_profile_layout);
            messageImage = (ImageView)itemView.findViewById(R.id.message_image_layout);

            context = itemView.getContext();

            //---DELETE FUNCTION---
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence options[] = new CharSequence[]{ "Delete","Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete this message");
                    builder.setItems(options,new AlertDialog.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(which == 0){

                                long mesPos = getAdapterPosition();
                                String mesId = mMessagesList.get((int)mesPos).toString();
                                Log.e("Message Id is ", mesId);
                                Log.e("Message is : ",mMessagesList.get((int)mesPos).getMessage());

                            }

                            if(which == 1){

                            }

                        }
                    });
                    builder.show();

                    return true;
                }
            });

        }


    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {


        String current_user_id = mAuth.getCurrentUser().getUid();
        Messages mes = mMessagesList.get(position);
        String from_user_id = mes.getFrom();
        String message_type = mes.getType();


        long timeStamp = mes.getTime();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String cal[] = calendar.getTime().toString().split(" ");
        String time_of_message = cal[1]+","+cal[2]+"  "+cal[3].substring(0,5);
        Log.e("TIME IS : ",calendar.getTime().toString());

        holder.displayTime.setText(time_of_message);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(from_user_id);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                holder.displayName.setText(name);
                //Picasso.with(holder.profileImage.getContext()).load(image).placeholder(R.drawable.user_img).into(holder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


            holder.messageText.setText(mes.getMessage());





        if(from_user_id.equals(current_user_id)){

            holder.messageText.setTextColor(Color.BLACK);

        }


    }


    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

}
