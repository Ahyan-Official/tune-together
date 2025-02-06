package com.app.tunetogether.az;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText emailField;
    EditText passField;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    LinearLayout btnSignUp;
    Button btnSignIn;
    TextView mStatusTextView;
    ProgressBar mProgressBar;

    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);




        btnSignUp = findViewById(R.id.btnSignUp);
        emailField = findViewById(R.id.emailField);
        passField = findViewById(R.id.passField);
        mStatusTextView = findViewById(R.id.textViewStatus);
        btnSignIn = findViewById(R.id.btnSignIn);
        mProgressBar = findViewById(R.id.determinateBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(0x3F51B5 ,android.graphics.PorterDuff.Mode.MULTIPLY);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //ProgressBar


        if (currentUser != null){
            mProgressBar.setVisibility(View.VISIBLE);
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            mProgressBar.setVisibility(View.INVISIBLE);
            finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                login(emailField.getText().toString(), passField.getText().toString());
            }
        });


//        forget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String email = emailTextInputLayout.getEditText().getText().toString();
//                if(TextUtils.isEmpty(email)){
//                    Toast.makeText(com.app.tunetogether.az.chat.LoginActivity.this, "Please Fill Email", Toast.LENGTH_SHORT).show();
//
//                }else{
//                    Toast.makeText(com.app.tunetogether.az.chat.LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
//
//                    mAuth.sendPasswordResetEmail(email)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//
//                                        Toast.makeText(getApplicationContext(),"Email sent",Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//
//                }
//
//            }
//        });
//




    }

    void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {

            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            finish();

        } else {
            mStatusTextView.setText("Invalid Credentials");
        }
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.INVISIBLE);

    }
    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

}
