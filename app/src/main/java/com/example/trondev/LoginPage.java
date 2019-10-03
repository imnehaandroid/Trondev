package com.example.trondev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    EditText usreId,userPassword;
    TextView forgotPassword,goToSignUp;
    Button submit;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        usreId=findViewById(R.id.user_Id);
        userPassword=findViewById(R.id.user_Password);
        forgotPassword=findViewById(R.id.password_Forgot);
        submit=findViewById(R.id.submit);
        progressBar=findViewById(R.id.progressBar);
        goToSignUp=findViewById(R.id.goToSignUp);

        firebaseAuth = FirebaseAuth.getInstance();

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUpPage = new Intent(LoginPage.this,SignUpPage.class);
                startActivity(goToSignUpPage);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usreId.getText().toString();
                final String password= userPassword.getText().toString();
                if (TextUtils.isEmpty(email)){
                   Toast.makeText(getApplicationContext(),"Please Enter Email Id",Toast.LENGTH_SHORT).show();
                   return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


            }
        });




    }
}
