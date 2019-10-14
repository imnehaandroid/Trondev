package com.example.trondev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {
    EditText editName;
    EditText editPhoneNumber;
    EditText editEmail;
    EditText editBoxId;
    EditText editUserPassword;
    Button btnSubmit;
    DatabaseReference databaseReference;
    FirebaseAuth mFirebaseAuth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);


        editPhoneNumber=findViewById(R.id.edit_phonenumber);
        editEmail=findViewById(R.id.edit_Email);
        editUserPassword=findViewById(R.id.edit_Password);
        editBoxId=findViewById(R.id.edit_BoxId);
        btnSubmit=findViewById(R.id.btn_submit);

        databaseReference= FirebaseDatabase.getInstance().getReference("SignUpData");
        mFirebaseAuth=FirebaseAuth.getInstance();


        initializeUI();

      btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

         final String email,password,name,phonenumber,boxId;
        name =editName.getText().toString();
        phonenumber=editPhoneNumber.getText().toString();
        email = editEmail.getText().toString();
        password = editUserPassword.getText().toString();
        boxId=editBoxId.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter name...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(getApplicationContext(), "Please enter phonenumber!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(boxId)){
            Toast.makeText(getApplicationContext(), "Please enter boxId!", Toast.LENGTH_LONG).show();
            return;

        }


        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            SignUpData information = new SignUpData(
                                    name,email,boxId
                            );

                            FirebaseDatabase.getInstance().getReference("SignUpData")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);

                                    Intent intent = new Intent(SignUpPage.this,LoginPage.class);
                                    startActivity(intent);

                                }
                            });

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void initializeUI() {
        editName= findViewById(R.id.edit_Name);
        editUserPassword = findViewById(R.id.edit_Password);
        editPhoneNumber=findViewById(R.id.edit_phonenumber);
        editEmail=findViewById(R.id.edit_Email);
        editBoxId=findViewById(R.id.edit_BoxId);
        btnSubmit = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressBar);
    }
}







