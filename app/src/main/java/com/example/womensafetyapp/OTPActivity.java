package com.example.womensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OTPActivity extends AppCompatActivity {

    Button otp_btn;
    EditText otp_entered;
    DatabaseReference reference;
    FirebaseDatabase rootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        otp_entered = findViewById(R.id.otp_editText);

        String phoneNo = getIntent().getStringExtra("number").toString();
        String verificationId = getIntent().getStringExtra("verificationId").toString();

//        sendVerificationToUser(phoneNo);

        otp_btn = findViewById(R.id.otp_btn);
        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp_entered.getText().toString().isEmpty()){
                    Toast.makeText(OTPActivity.this, "ENTER OTP", Toast.LENGTH_SHORT).show();
                }
                else if(verificationId != null){
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");
                    String code = otp_entered.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            String fullname = getIntent().getStringExtra("name").toString();
                            String age = getIntent().getStringExtra("age").toString();
                            String gender = getIntent().getStringExtra("gender").toString();
                            String number = getIntent().getStringExtra("number").toString();
                            String password = getIntent().getStringExtra("password").toString();
                            credentials user_details = new credentials(fullname,age,gender,number,password);
                            reference.child(number).setValue(user_details);
                            Intent intent = new Intent(OTPActivity.this,DashboardActivity.class);
                            intent.putExtra("name",fullname);
                            intent.putExtra("age", age);
                            intent.putExtra("number", number);
                            intent.putExtra("gender",gender);
                            intent.putExtra("password",password);
                            startActivity(intent);
                            Toast.makeText(OTPActivity.this, "SUCCESSFULLY REGISTERED", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OTPActivity.this, "INVALID OTP ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

//    private void sendVerificationToUser(String phoneNo) {
//
//    }
}