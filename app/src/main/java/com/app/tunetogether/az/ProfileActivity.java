package com.app.tunetogether.az;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ProfileActivity extends AppCompatActivity {

    ImageView imageProfile, edit_profile_image;
    ConstraintLayout fullname, logout, phone, password,preference;
    TextView fullname_txt, phone_txt, email_txt, password_txt, profile_name;
    ProgressBar profile_progress;
    FirebaseUser firebaseUser;

    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageRef;
    int genderId = 1;
    AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //init
        fullname = findViewById(R.id.fullname_layout);

        preference = findViewById(R.id.preference);
        logout = findViewById(R.id.logout);
        password = findViewById(R.id.password_layout);
        phone = findViewById(R.id.phone_layout);
        imageProfile = findViewById(R.id.image_profile);
        fullname_txt = findViewById(R.id.fullname);
        phone_txt = findViewById(R.id.phone);
        email_txt = findViewById(R.id.email);
        password_txt = findViewById(R.id.password);
        profile_name = findViewById(R.id.profile_name);
        edit_profile_image = findViewById(R.id.edit_profile_image);
        profile_progress = findViewById(R.id.profile_progress);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");


        //Bottom navigation bar
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);

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
        bottomNavigation.enableItemAtPosition(3);
        bottomNavigation.setCurrentItem(3);
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




        //Clicking full name
        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to open change name
                Intent intent = new Intent(ProfileActivity.this, ChageNameActivity.class);
                intent.putExtra("fullname", fullname_txt.getText().toString());
                startActivity(intent);
            }
        });

        // On clicking logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //on clicking phone
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to open change phone

                Intent intent = new Intent(ProfileActivity.this, ChangePhoneActivity.class);
                intent.putExtra("phone", phone_txt.getText().toString());
                startActivity(intent);
            }
        });

        //change image
        edit_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });

        preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to open change phone

                Intent intent = new Intent(ProfileActivity.this, PreferenceActivity.class);
                startActivity(intent);
            }
        });


        userInfo();
    }

    //read user data from database
    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get image
                if (snapshot.child("image").exists())
                {

                    Picasso.get().load(snapshot.child("image").getValue().toString()).error(R.drawable.pro).placeholder(R.drawable.pro).into(imageProfile);

                }


                //get name
                if (snapshot.child("name").exists())
                {
                    fullname_txt.setText(snapshot.child("name").getValue().toString());
                    profile_name.setText(snapshot.child("name").getValue().toString());

                }else{

                    fullname_txt.setText("Name");
                    profile_name.setText("Name");

                }

                //get phone
                if (snapshot.child("phone").exists())
                {

                    phone_txt.setText(snapshot.child("phone").getValue().toString());


                }else{

                    phone_txt.setText("123456789");

                }

                //get email
                if (snapshot.child("email").exists())
                {
                    email_txt.setText(snapshot.child("email").getValue().toString());

                }else{

                    email_txt.setText("Email");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //upload image to database
    private void uploadImage() {
        profile_progress.setVisibility(View.VISIBLE);
        if (mImageUri != null) {
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            mime.getExtensionFromMimeType(contentResolver.getType(mImageUri));

            //get file extension
            String extension = mime.getMimeTypeFromExtension(contentResolver.getType(mImageUri));
                //16123687718236 -37

            //adding file is time and extension
            //Like this 162138941289.png
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + extension);
            uploadTask = fileReference.putFile(mImageUri);
            //upload to database
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        //get download URL
                        String myUrl = downloadUri.toString();

                        // putting that in the database
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("image", "" + myUrl);

                        reference.updateChildren(hashMap);
                        profile_progress.setVisibility(View.GONE);
                        //finish();
                    } else {
                        profile_progress.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    // on database failure
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            profile_progress.setVisibility(View.GONE);
            Toast.makeText(this, "No Image selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get image from gallery
        if (requestCode == 2 && resultCode == RESULT_OK) {
            mImageUri = data.getData();

            // uplaod image
            uploadImage();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   userInfo();
    }
}
