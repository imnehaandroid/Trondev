package com.example.trondev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.InputQueue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {
    EditText userId, userPassword;
    TextView forgotPassword, goToSignUp;
    Button submit;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    private SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_login_page);
            userId =  findViewById(R.id.edt_email);
            userPassword  =  findViewById(R.id.edt_password);
            submit =  findViewById(R.id.btn_submit);
            progressBar =  findViewById(R.id.progressBar);
            goToSignUp= findViewById(R.id.goToSignUp);
            signInButton = findViewById(R.id.sign_in_button);
            forgotPassword=findViewById(R.id.password_Forgot);
            mAuth=FirebaseAuth.getInstance();

            goToSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginPage.this, SignUpPage.class));

                }
            });


            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   showResetPasswordDialog();
                }
            });


            // Checking the email id and password is Empty
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = userId.getText().toString();
                    final String password = userPassword.getText().toString();
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        userId.setError("Invalid Email");
                        userId.setFocusable(true);

                    }else
                    if (password.length()<6) {
                        userPassword.setError("Password length at least 6 characters");
                        userPassword.setFocusable(true);
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    //authenticate user
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);                 }
                                }
                            });
                }
            });

                progressBar =new ProgressBar(this);
        }

    private void showResetPasswordDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        LinearLayout linearLayout = new LinearLayout(this);

        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        linearLayout.addView(emailEt);

        emailEt.setMinEms(10);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = emailEt.getText().toString().trim();
                beginReset(email);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
                   builder.create().show();

    }

    private void beginReset(String email) {
          progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        Toast.makeText(LoginPage.this,"Email Sent",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginPage.this, "Failed....", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginPage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
    }
}
