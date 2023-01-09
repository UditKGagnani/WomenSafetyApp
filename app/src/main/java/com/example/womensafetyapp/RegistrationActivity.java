package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    EditText reg_name, reg_age, reg_number, reg_password;
    CountryCodePicker ccp;
    TextView reg_login;
    Button register;
    String gender="female";

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        reg_name = findViewById(R.id.reg_name);
        reg_age = findViewById(R.id.reg_age);
        reg_number = findViewById(R.id.log_phone);
        reg_password = findViewById(R.id.log_pass);

        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(reg_number);

        mAuth = FirebaseAuth.getInstance();

        reg_login = findViewById(R.id.reg_log);
        register = findViewById(R.id.log_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                String fullname = reg_name.getText().toString();
                String age = reg_age.getText().toString();
                String number = reg_number.getText().toString().replace(" ","");
                String password = reg_password.getText().toString();

                if(fullname.isEmpty()){
                    reg_name.setError("This field cannot be empty");
                }
                if(age.isEmpty()){
                    reg_age.setError("This field cannot be empty");
                }
                if(!age.isEmpty()){
                    try {
                        int check_age = Integer.parseInt(age);
                        if(check_age>100)
                        {
                            reg_age.setError("Enter a Valid Age");
                        }
                        if(check_age<5)
                        {
                            reg_age.setError("Too young to use the app");
                        }
                        else {
                            age = String.valueOf(check_age);
                        }
                    }catch (Exception e){
                        reg_age.setError("Enter a Valid Age");
                    }
                }
                if(number.isEmpty()){
                    reg_number.setError("This field cannot be empty");
                }
                if(number.length() != 10){
                    reg_number.setError("Enter a Valid Phone Number");
                }
                if(password.isEmpty()){
                    reg_password.setError("This field cannot be empty");
                }
                if(password.length() < 8){
                    reg_password.setError("Password should be atleast 8 characters long");
                }
                else {
                    Query checkUser = reference.orderByChild("number").equalTo(ccp.getFullNumberWithPlus().replace(" ", ""));

                    String finalAge = age;
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(RegistrationActivity.this, "This Number is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                                String phoneNo = ccp.getFullNumberWithPlus().replace(" ", "");

                                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                                        Toast.makeText(RegistrationActivity.this, "REGISTRATION SUCCESSFULLY COMPLETED", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        Toast.makeText(RegistrationActivity.this, "REGISTRATION FAILED. PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        Intent intent = new Intent(RegistrationActivity.this, OTPActivity.class);
                                        intent.putExtra("number", ccp.getFullNumberWithPlus().replace(" ", ""));
                                        intent.putExtra("verificationId",verificationId);
                                        intent.putExtra("name",fullname);
                                        intent.putExtra("age", finalAge);
                                        intent.putExtra("gender",gender);
                                        intent.putExtra("password",password);
                                        startActivity(intent);
                                    }
                                };

                                PhoneAuthOptions options =
                                        PhoneAuthOptions.newBuilder(mAuth)
                                                .setPhoneNumber(phoneNo)       // Phone number to verify
                                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                .setActivity(RegistrationActivity.this)                 // Activity (for callback binding)
                                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                .build();
                                PhoneAuthProvider.verifyPhoneNumber(options);
//                    Toast.makeText(RegistrationActivity.this, "SUCCESSFULLY REGISTERED", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    String phoneNo = ccp.getFullNumberWithPlus().replace(" ", "");
//
//                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//                        @Override
//                        public void onVerificationCompleted(PhoneAuthCredential credential) {
//
//                        }
//
//                        @Override
//                        public void onVerificationFailed(FirebaseException e) {
//                            Toast.makeText(RegistrationActivity.this, "REGISTRATION FAILED. PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCodeSent(@NonNull String verificationId,
//                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                            Intent intent = new Intent(RegistrationActivity.this, OTPActivity.class);
//                            intent.putExtra("number", ccp.getFullNumberWithPlus().replace(" ", ""));
//                            intent.putExtra("verificationId",verificationId);
//                            intent.putExtra("name",fullname);
//                            intent.putExtra("age",fullname);
//                            intent.putExtra("gender",gender);
//                            intent.putExtra("password",password);
//                            startActivity(intent);
//                        }
//                    };
//
//                    PhoneAuthOptions options =
//                            PhoneAuthOptions.newBuilder(mAuth)
//                                    .setPhoneNumber(phoneNo)       // Phone number to verify
//                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                                    .setActivity(RegistrationActivity.this)                 // Activity (for callback binding)
//                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                                    .build();
//                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
//                reference.setValue("First Data Stored");

            }
        });

        reg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void female(View view) {
        gender = "female";
    }

    public void male(View view) {
        gender = "male";
    }

    public void other(View view) {
        gender = "other";
    }
}