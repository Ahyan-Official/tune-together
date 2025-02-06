package com.app.tunetogether.az;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePhoneActivity extends AppCompatActivity
{
    Button save;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);

        save = findViewById(R.id.save);
        phone = findViewById(R.id.phone_form);
        phone.setText(getIntent().getStringExtra("phone"));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (TextUtils.isEmpty(phone.getText().toString()))
                {
                    phone.setError("Field is required!");
                }
                else if (phone.getText().toString().length() == 10)
                {
                    phone.setError("Incorrect Phone Number");
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(user.getUid())
                            .child("phone")
                            .setValue(phone.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                finish();
                                Toast.makeText(ChangePhoneActivity.this, "Profile Updated Succesfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
