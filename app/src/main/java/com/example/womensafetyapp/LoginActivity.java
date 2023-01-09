package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText number, password;
    Button login;
    TextView log_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        number = findViewById(R.id.log_phone);
        password = findViewById(R.id.log_pass);
        login = findViewById(R.id.log_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEnteredNumber = number.getText().toString().replace(" ","");
                String userEnteredPassword = password.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                Query checkUser = reference.orderByChild("number").equalTo(userEnteredNumber);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String passwordDB = snapshot.child(userEnteredNumber).child("password").getValue(String.class);
                            if(passwordDB.equals(userEnteredPassword)){
                                String fullnameDB = snapshot.child(userEnteredNumber).child("fullname").getValue(String.class);
                                String ageDB = snapshot.child(userEnteredNumber).child("age").getValue(String.class);
                                String genderDB = snapshot.child(userEnteredNumber).child("gender").getValue(String.class);
                                String numberDB = snapshot.child(userEnteredNumber).child("number").getValue(String.class);

                                Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                                intent.putExtra("fullname",fullnameDB);
                                intent.putExtra("age",ageDB);
                                intent.putExtra("gender",genderDB);
                                intent.putExtra("number",numberDB);
                                intent.putExtra("password",passwordDB);
                                startActivity(intent);
                            }
                            else{
                                password.setError("Incorrect Password");
                            }
                        }
                        else{
                            number.setError("No such user exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        log_register = findViewById(R.id.log_reg);
        log_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }
}