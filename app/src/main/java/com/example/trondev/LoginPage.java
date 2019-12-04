package com.example.trondev;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {
    private static final String BOX_STATUS_KEY = "bo_xstatus";
    private static final String UUID_KEY = "uuid";
    private static final String ORDER_ID_EXISTS = "order_id_exists";
    EditText userId, userPassword;
    TextView forgotPassword, goToSignUp;
    Button submit;
    FirebaseAuth mAuth;
    LinearLayout progressBar;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_page);
        userId = findViewById(R.id.edt_email);
        userPassword = findViewById(R.id.edt_password);
        submit = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressBar);
        goToSignUp = findViewById(R.id.goToSignUp);
        forgotPassword = findViewById(R.id.password_forgot);


        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        mAuth = FirebaseAuth.getInstance();

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userId.getText().toString();
                String password = userPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter Email...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    userId.setError("Invalid Email");
                    userId.setFocusable(true);

                } else if (password.length() < 6) {
                    userPassword.setError("Password length at least 6 characters");
                    userPassword.setFocusable(true);
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    checkEmailVerification();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
             userId.getText().clear();
             userPassword.getText().clear();

    }
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        LinearLayout linearLayout = new LinearLayout(this);

        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        linearLayout.addView(emailEt);

        emailEt.setMinEms(10);
        linearLayout.setPadding(10, 10, 10, 10);

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
                if (task.isSuccessful()) {
                    Toast.makeText(LoginPage.this, "Email Sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginPage.this, "Failed....", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginPage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void checkEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            boolean emailflag = firebaseUser.isEmailVerified();

            if (emailflag) {

                //  Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                Log.e("Neha", mAuth.getUid());

                Call<JsonObject> call = apiInterface.getUserData(mAuth.getUid());
                Log.e("Neha", call.request().url().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.isSuccessful()) {
                            JsonObject userData = response.body();


                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(UUID_KEY, mAuth.getUid());
                            bundle.putBoolean(ORDER_ID_EXISTS, userData.has("orderIds"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            } else {
                Toast.makeText(this, "Verifiy your email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  void hideKeyboard(View view){

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }




 }



