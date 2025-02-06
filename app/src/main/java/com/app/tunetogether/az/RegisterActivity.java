package com.app.tunetogether.az;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.tunetogether.az.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String username = "username";

    EditText emailField;
    EditText passField;
    EditText passField2;
    EditText nameField,phoneField;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    LinearLayout btnBack;
    Button btnSignUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        btnBack = findViewById(R.id.btnBack);
        btnSignUp = findViewById(R.id.btnSignUp);

        emailField = findViewById(R.id.emailField);
        passField = findViewById(R.id.passField);
        passField2 = findViewById(R.id.passField2);
        nameField = findViewById(R.id.nameField);
        phoneField = findViewById(R.id.phoneField);






        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameField.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();

                } else {
                    if (passField.getText().toString().equals(passField2.getText().toString())) {


                        createUser(emailField.getText().toString(), passField.getText().toString());

                    } else {
                        Toast.makeText(RegisterActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();

                        hideProgressBar();

                    }
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

    void createUser(String email, String password) {
        showProgressBar();
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    String email = emailField.getText().toString();
                    String firstName = nameField.getText().toString();
                    String phone = phoneField.getText().toString();

                    Map userMap=new HashMap();
                    userMap.put("name",firstName);
                    userMap.put("phone",phone);
                    userMap.put("image","default");
                    userMap.put("email",email);
                    userMap.put("fullname",firstName);

                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if(task1.isSuccessful()){
                                hideProgressBar();
                                progressDialog.dismiss();

                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                mainIntent.putExtra("username", nameField.getText().toString());
                                startActivity(mainIntent);
                                finish();





                            }


                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();

                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                // ...
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        progressDialog.dismiss();
        if (user != null) {

//            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//            mainIntent.putExtra("username", nameField.getText().toString());
//            startActivity(mainIntent);
//            RegisterActivity.this.finish();

        } else {

            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }

    }

    private void hideProgressBar(){
        progressDialog.dismiss();
        //mProgressBar.setVisibility(View.VISIBLE);

    }
    private void showProgressBar(){
        //mProgressBar.setVisibility(View.INVISIBLE);

    }
}