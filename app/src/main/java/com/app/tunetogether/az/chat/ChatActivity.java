package com.app.tunetogether.az.chat;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.app.tunetogether.az.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;


public class ChatActivity extends AppCompatActivity {

    private String mChatUser;

    private FirebaseAuth mAuth;

    String mCurrentUserId;

    DatabaseReference mDatabaseReference;
    private DatabaseReference mRootReference;

    private EditText mMessageView;
    ImageView imageViewAttachment;
    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static String fileName = null;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;

    public static final int TOTAL_ITEM_TO_LOAD = 10;
    private int mCurrentPage = 1;

    //Solution for descending list on refresh
    private int itemPos = 0;
    private String mLastKey="";
    private String mPrevKey="";
    private MediaRecorder recorder = null;

    private static final int GALLERY_PICK=1;
    StorageReference mImageStorage;
    ImageView imageAudio;
    StorageReference mStorage;
    ImageView imageSend;
    AudioRecordView arc;
    String userName="User";
    ProgressDialog pd;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");
        imageViewAttachment = (ImageView)findViewById(R.id.imageViewAttachment);
        mMessageView = (EditText)findViewById(R.id.editTextMessage);

        imageAudio = (ImageView) findViewById(R.id.imageAudio);
        imageSend = (ImageView) findViewById(R.id.imageSend);
        arc = (AudioRecordView) findViewById(R.id.recordingView);




        mStorage = FirebaseStorage.getInstance().getReference();
        fileName =getApplicationContext().getFilesDir().getPath();
        fileName += "/recorded_audio.3gp";



        mChatUser = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("user_name");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(userName);




        mRootReference = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mMessageAdapter = new MessageAdapter(messagesList);

        mMessagesList = (RecyclerView)findViewById(R.id.recycleViewMessageList);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.message_swipe_layout);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayoutManager);

        imageSend.setVisibility(View.VISIBLE);
        imageSend.setImageResource(R.drawable.send22);



        //===========================
        fetch();



        mRootReference.child("chats").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(mChatUser)){


                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("time_stamp",ServerValue.TIMESTAMP);
                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/"+mChatUser+"/"+mCurrentUserId,chatAddMap);
                    chatUserMap.put("chats/"+mCurrentUserId+"/"+mChatUser,chatAddMap);


                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){

                                Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();

                            }
                            else
                                Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();
                        }


                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();
            }
        });


        imageSend.setColorFilter(Color.argb(255, 255, 255, 255));
        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = mMessageView.getText().toString();
                if(!TextUtils.isEmpty(message)){

                    String current_user_ref = "messages/"+mCurrentUserId+"/"+mChatUser;
                    String chat_user_ref = "messages/"+ mChatUser +"/"+mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser).push();
                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message",message);
                    messageMap.put("seen",false);
                    messageMap.put("type","text");
                    messageMap.put("time",ServerValue.TIMESTAMP);
                    messageMap.put("from",mCurrentUserId);


                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                    messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);


                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener(){

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null){

                                Log.e("CHAT_ACTIVITY","Cannot add message to database");

                            }
                            else{

                                mMessageView.setText("");
                                mMessageAdapter.notifyDataSetChanged();


                                mMessagesList.scrollToPosition(mMessagesList.getAdapter().getItemCount()-1);



                            }

                        }
                    });




                }

            }
        });




        imageViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);
            }
        });

        //----LOADING 10 MESSAGES ON SWIPE REFRESH----
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
                mCurrentPage++;
                loadMoreMessages();;

            }
        });







    }

    //---ON REFRESHING 10 MORE MESSAGES WILL LOAD----
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = (Messages) dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();


                if(!mPrevKey.equals(messageKey)){
                    messagesList.add(itemPos++,message);

                }
                else{
                    mPrevKey = mLastKey;
                }

                if(itemPos == 1){
                    String mMessageKey = dataSnapshot.getKey();
                    mLastKey = mMessageKey;
                }


                mMessageAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);

                mLinearLayoutManager.scrollToPositionWithOffset(10,0);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //---THIS FUNCTION IS CALLED WHEN SYSTEM ACTIVITY IS CALLED---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //---FOR PICKING IMAGE FROM GALLERY ACTIVITY AND SENDING---
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            //---GETTING IMAGE DATA IN FORM OF URI--

            pd.show();
            Uri imageUri = data.getData();
            final String current_user_ref = "messages/"+mCurrentUserId+"/"+mChatUser;
            final String chat_user_ref = "messages/"+ mChatUser +"/"+mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();

            //---PUSHING IMAGE INTO STORAGE---
            StorageReference filepath = mImageStorage.child("message_images").child(push_id+".jpg");

            UploadTask rrrr = filepath.putFile(imageUri);

            Toast.makeText(getApplicationContext(),"Uploading image...",Toast.LENGTH_SHORT).show();
            Task<Uri> urlTask = rrrr.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        pd.dismiss();
                        Uri downloadUri2 = task.getResult();

                        Map messageMap = new HashMap();
                        messageMap.put("message",downloadUri2.toString());
                        messageMap.put("seen",false);
                        messageMap.put("type","image");
                        messageMap.put("time",ServerValue.TIMESTAMP);
                        messageMap.put("from",mCurrentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                        messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener(){

                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError != null){
                                    Log.e("CHAT_ACTIVITY","Cannot add message to database");
                                }
                                else{
                                    // Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                    mMessageView.setText("");


                                    mMessagesList.scrollToPosition(messagesList.size()-1);
                                }

                            }
                        });


                    } else {
                        pd.dismiss();

                    }
                }
            });





        }






    }
    private final int MY_PERMISSIONS_RECORD_AUDIO = 100;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }






    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText,messageTextOther;
        public TextView displayTime;
        public RoundedImageView messageImage;

        VoicePlayerView VoicePlayerView1;
        VoicePlayerView VoicePlayerView2;

        public ViewHolder(View itemView) {
            super(itemView);


            messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            messageTextOther = (TextView) itemView.findViewById(R.id.message_text_layout_other);

            displayTime = (TextView) itemView.findViewById(R.id.time_text_layout);
            messageImage = (RoundedImageView)itemView.findViewById(R.id.message_image_layout);

            VoicePlayerView1 = (VoicePlayerView) itemView.findViewById(R.id.VoicePlayerView1);
            VoicePlayerView2 = (VoicePlayerView) itemView.findViewById(R.id.VoicePlayerView2);


        }


    }


    private void fetch() {


        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);


        FirebaseRecyclerOptions<Messages> options = new FirebaseRecyclerOptions.Builder<Messages>().setQuery(messageRef, Messages.class)
                .build();


        FirebaseRecyclerAdapter ff = new FirebaseRecyclerAdapter<Messages, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);


                return new ViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(final ViewHolder viewHolder, int position, final Messages messages) {
                String current_user_id = mAuth.getCurrentUser().getUid();
                String from_user_id = messages.getFrom();

                //viewHolder.messageText.setText(messages.getMessage());

                //168239827897324
                long timeStamp = messages.getTime();
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(timeStamp);
                String cal[] = calendar.getTime().toString().split(" ");
                String time_of_message = cal[1]+","+cal[2]+"  "+cal[3].substring(0,5);
                Log.e("TIME IS : ",calendar.getTime().toString());

                viewHolder.displayTime.setText(time_of_message);



                if(from_user_id.equals(current_user_id)){


                    if(messages.getType().equals("text")){

                        viewHolder.messageText.setVisibility(View.VISIBLE);//Blue
                        viewHolder.messageTextOther.setVisibility(View.GONE); //Gray

                        viewHolder.messageText.setText(messages.getMessage());
                        viewHolder.messageImage.setVisibility(View.GONE);


                    }
                    else if(messages.getType().equals("image")){

                        viewHolder.messageText.setVisibility(View.GONE);

                        viewHolder.messageTextOther.setVisibility(View.GONE);

                        viewHolder.messageImage.setVisibility(View.VISIBLE);

                        viewHolder.messageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ChatActivity.this, ZoomActivity.class);
                                intent.putExtra("image",messages.getMessage());
                                startActivity(intent);
                            }
                        });


                        Picasso.get().load(messages.getMessage()).placeholder(R.drawable.not_found).into(viewHolder.messageImage);

                    }



                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)viewHolder.messageImage.getLayoutParams();
                    params1.gravity = Gravity.END;
                    viewHolder.messageImage.setLayoutParams(params1);







                } else {

                    if(messages.getType().equals("text")){

                        viewHolder.messageTextOther.setVisibility(View.VISIBLE);//Gray

                        viewHolder.messageTextOther.setText(messages.getMessage());
                        viewHolder.messageText.setVisibility(View.GONE); //Blue


                        viewHolder.messageImage.setVisibility(View.GONE);


                    }
                    else if(messages.getType().equals("image")){

                        viewHolder.messageText.setVisibility(View.GONE);
                        viewHolder.messageTextOther.setVisibility(View.GONE);
                        viewHolder.messageImage.setVisibility(View.VISIBLE);

                        Picasso.get().load(messages.getMessage()).placeholder(R.drawable.not_found).into(viewHolder.messageImage);

                        viewHolder.messageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ChatActivity.this,ZoomActivity.class);
                                intent.putExtra("image",messages.getMessage());
                                startActivity(intent);
                            }
                        });
                    }




                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)viewHolder.messageImage.getLayoutParams();
                    params1.gravity = Gravity.START;
                    viewHolder.messageImage.setLayoutParams(params1);





                }





            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                mMessageAdapter.notifyDataSetChanged();


                mMessagesList.scrollToPosition(mMessagesList.getAdapter().getItemCount()-1);

            }
        };

        ff.notifyDataSetChanged();
        mMessagesList.setAdapter(ff);
        ff.startListening();








    }


}

