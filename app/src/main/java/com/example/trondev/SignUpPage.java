package com.example.trondev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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


    private String email, name, phonenumber, boxId, password;

    private void registerNewUser() {


        name = editName.getText().toString().trim();
        phonenumber = editPhoneNumber.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        password = editUserPassword.getText().toString().trim();
        boxId = editBoxId.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter name...", Toast.LENGTH_LONG).show();
            return;
        }
        if (phonenumber.isEmpty() || phonenumber.length() < 10) {
            editPhoneNumber.setError("Enter a valid number");
            editPhoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter Email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            editEmail.setError("Invalid Email");
            editEmail.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(boxId)) {
            Toast.makeText(getApplicationContext(), "Please enter boxId!", Toast.LENGTH_LONG).show();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                            String boxStatus = "aaaa@,0";
                            User information = new User(name, email, phonenumber, boxId, boxStatus);
                            FirebaseUser user = mAuth.getCurrentUser();
                            databaseReference.child(user.getUid()).setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);

                                    //Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                                    //  startActivity(intent);


                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);


                        }
                    }
                });
                         editName.getText().clear();
                         editBoxId.getText().clear();
                         editEmail.getText().clear();
                         editUserPassword.getText().clear();
                         editPhoneNumber.getText().clear();

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

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(SignUpPage.this, "Registration successful,Verification mail send !", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(SignUpPage.this, LoginPage.class));
                    } else {
                        Toast.makeText(SignUpPage.this, "verification mail hasn't been send", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    public void hideKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}











