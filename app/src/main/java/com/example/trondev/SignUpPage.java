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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class SignUpPage extends AppCompatActivity {
    EditText editName;
    EditText editPhoneNumber;
    EditText editEmail;
    EditText editBoxId;
    EditText editUserPassword;
    Button btnSubmit;
    TextView goTologin;
    DatabaseReference databaseReference;
    FirebaseAuth mFirebaseAuth;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);


        editPhoneNumber=findViewById(R.id.edit_phone);
        editEmail=findViewById(R.id.edit_email);
        editUserPassword=findViewById(R.id.edit_password);
        editBoxId=findViewById(R.id.edit_boxId);
        btnSubmit=findViewById(R.id.btn_submit);
        goTologin=findViewById(R.id.goToLogin);

        mFirebaseAuth=FirebaseAuth.getInstance();


        initializeUI();


        goTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLogpage = new Intent(SignUpPage.this,LoginPage.class);
                startActivity(goToLogpage);

            }
        });
        

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

        final String email, password, name, phonenumber, boxId;
        name = editName.getText().toString();
        phonenumber = editPhoneNumber.getText().toString();
        email = editEmail.getText().toString();
        password = editUserPassword.getText().toString();
        boxId = editBoxId.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(boxId)) {
            Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_LONG).show();
            return;

        }


        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            User information = new User(name, email, boxId);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);

                                    Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                                    startActivity(intent);

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
    private void initializeUI() {
        editName= findViewById(R.id.edit_name);
        editUserPassword = findViewById(R.id.edit_password);
        editPhoneNumber=findViewById(R.id.edit_phone);
        editEmail=findViewById(R.id.edit_email);
        editBoxId=findViewById(R.id.edit_boxId);
        btnSubmit = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressBar);
    }
}







