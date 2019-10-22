package com.example.trondev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String BOX_ID_KEY = "box_id";
    private static final String UUID_KEY = "uuid";
    EditText orderIdEt;
    ToggleButton toggleButton;

    private String boxId, uuID;
    private int toggleValue = -1;
    private LinearLayout progressBar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orderIdEt = findViewById(R.id.edit_orderId);
        toggleButton = findViewById(R.id.tb1);
        progressBar = findViewById(R.id.progressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference("boxes");

        boxId = getIntent().getExtras().getString(BOX_ID_KEY);
        uuID = getIntent().getExtras().getString(UUID_KEY);

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

    private void setFirebaseData() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("$");
        stringBuilder.append(boxId);
        stringBuilder.append(",");
        String orderId;

        if (toggleValue == 1) {
            orderId = orderIdEt.getText().toString();
            stringBuilder.append(orderId.substring(orderId.length() - 4));
            stringBuilder.append(",");
        } else {
            stringBuilder.append("####");
            stringBuilder.append(",");
        }
        stringBuilder.append(toggleValue);

        Log.e("Neha", stringBuilder.toString());

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(uuID).push().setValue(stringBuilder.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    if (toggleValue == 1)
                        Toast.makeText(MainActivity.this, "Parcel box is open", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "Parcel box is close", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(MainActivity.this, "In Failure", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
