package com.example.trondev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity {
    private static final String BOX_STATUS_KEY = "bo_xstatus";
    private static final String UUID_KEY = "uuid";
    private static final String ORDER_ID_EXISTS = "order_id_exists";

    EditText orderIdEt;
    ToggleButton toggleButton;
    Button setOrder;

    private String boxStatus, uuID;
    private int toggleValue = -1;
    private LinearLayout progressBar;

    private ApiInterface apiInterface;
    private DatabaseReference databaseReference;
    private boolean isOrderIDsExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orderIdEt = findViewById(R.id.edit_orderId);
        toggleButton = findViewById(R.id.tb1);
        setOrder = findViewById(R.id.set_orderId);
        progressBar = findViewById(R.id.progressBar);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boxStatus = bundle.getString(BOX_STATUS_KEY);
            uuID = bundle.getString(UUID_KEY);
            isOrderIDsExists = bundle.getBoolean(ORDER_ID_EXISTS);
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        setOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uuID).child("orderIds");

                progressBar.setVisibility(View.VISIBLE);
                if (isOrderIDsExists) {
                    checkNumberOfOrderIds();
                } else {
                    isOrderIDsExists = true;
                    addOrderIdToFireBase();
                }

            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleValue = 1;
                } else {
                    toggleValue = 0;
                }
                setFirebaseData();
            }
        });
    }

    private void checkNumberOfOrderIds() {

        Call<JsonObject> call = apiInterface.getUserOIds(uuID, "orderIds");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    JsonObject orderIDsJsonObject = response.body();

                    int size = orderIDsJsonObject.size();
                    if (size < 3) {
                        addOrderIdToFireBase();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.orderId_exist_msg), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.response_successful), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.in_faliure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addOrderIdToFireBase() {
        databaseReference.push().setValue(orderIdEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "OrderId Set", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MainActivity.this, "In Failure", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void setFirebaseData() {
        StringBuilder stringBuilder = new StringBuilder();
        //  stringBuilder.append("$");
        //  stringBuilder.append(boxId);
        //   stringBuilder.append(",");
        String orderId;

        if (toggleValue == 1) {
            orderId = orderIdEt.getText().toString();
            stringBuilder.append(orderId.substring(orderId.length() - 4));
            stringBuilder.append("@");
            stringBuilder.append(",");
        } else {
            stringBuilder.append("aaaa");
            stringBuilder.append("@");
            stringBuilder.append(",");
        }

        stringBuilder.append(toggleValue);

        Log.e("Neha", stringBuilder.toString());
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uuID).child("boxStatus");
        
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.setValue(stringBuilder.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    if (toggleValue == 1)
                        Toast.makeText(MainActivity.this, "Parcel box is open", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "Parcel box is close", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivity.this, "In Failure", Toast.LENGTH_SHORT).show();

            }

        });


    }
}
