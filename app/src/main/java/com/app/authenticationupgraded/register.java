package com.app.authenticationupgraded;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText mUserName,mEmail,mPassword,mProfession,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName   = findViewById(R.id.username);
        mEmail      = findViewById(R.id.emailaddress);
        mPassword   = findViewById(R.id.password);
        mProfession = findViewById(R.id.profession);
        mRegisterBtn= findViewById(R.id.register);
        mLoginBtn   = findViewById(R.id.loginhere);
        mPhone      = findViewById(R.id.phone);
        fStore      = FirebaseFirestore.getInstance();
        fAuth       = FirebaseAuth.getInstance();


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String email = mEmail.getText().toString().trim();
               String password = mPassword.getText().toString().trim();
               String username = mUserName.getText().toString();
               String profession = mProfession.getText().toString();
               String phoneNumber = mPhone.getText().toString();

               if(TextUtils.isEmpty(email)){
                   mEmail.setError("Enter Email");
                   return;
               }
               if(TextUtils.isEmpty(password)){
                   mPassword.setError("Enter Password");
                   return;
               }
               if(password.length() < 6){
                   mPassword.setError("password must be atleast 6 characters");
                   return;
               }

               fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {
                           fAuth.getCurrentUser().sendEmailVerification();
                           Toast.makeText(register.this, "verification link has been sent to your mail,please verify", Toast.LENGTH_SHORT).show();
                           userID = fAuth.getCurrentUser().getUid();
                           DocumentReference documentReference = fStore.collection("Users").document(userID);
                           Map<String,Object> user= new HashMap<>();
                           user.put("userName",username);
                           user.put("Profession",profession);
                           user.put("PhoneNumber",phoneNumber);
                           user.put("email",email);
                           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(register.this, "User data is saved successfully", Toast.LENGTH_SHORT).show();
                               }
                           });
                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                           finish();
                       } else{
                           Toast.makeText(register.this, "Error occured" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }

                   }
               });
            }
        });
    }

    public void gotologin(View view) {
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }
}