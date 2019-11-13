package com.example.trondev;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class SignUpPage extends AppCompatActivity {
    EditText editName, editEmail, editBoxId, editUserPassword;
    private EditText editPhoneNumber;
    Button btnContinue;
    TextView goTologin;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    LinearLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
        initializeUI();

        goTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLogInPage = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(goToLogInPage);
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();

            }
        });
    }


    private String email, password, name, phonenumber, boxId;

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);


        name = editName.getText().toString();
        phonenumber = editPhoneNumber.getText().toString();
        email = editEmail.getText().toString();
        password = editUserPassword.getText().toString();
        boxId = editBoxId.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter name...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(phonenumber)) {

            Toast.makeText(getApplicationContext(), "Please enter phone number!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter Email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(boxId)) {
            Toast.makeText(getApplicationContext(), "Please enter boxId!", Toast.LENGTH_LONG).show();
            return;

        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        String boxStatus = "####@,0";
                                        User information = new User(name, email, phonenumber, boxId, boxStatus);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        databaseReference.child(user.getUid()).setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                finish();
                                            }
                                        });

                                    } else {
                                        Toast.makeText(SignUpPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                           }
                                       }
                                 });
                           } else {
                            Toast.makeText(SignUpPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    private void initializeUI() {
        editName = findViewById(R.id.edit_Name);
        editUserPassword = findViewById(R.id.edit_Password);
        editPhoneNumber = findViewById(R.id.edit_Phone);
        editEmail = findViewById(R.id.edit_Email);
        editBoxId = findViewById(R.id.edit_BoxId);
        btnContinue = findViewById(R.id.btn_continue);
        goTologin = findViewById(R.id.goToLogin);
        progressBar = findViewById(R.id.progressBar);

    }
}











