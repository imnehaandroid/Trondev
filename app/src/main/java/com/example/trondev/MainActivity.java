package com.example.trondev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String BOX_ID_KEY = "box_id";
    private static final String UUID_KEY = "uuid";

    EditText orderIdEt;
    ToggleButton toggleButton;
    Button setOrder;

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
        setOrder=findViewById(R.id.set_orderId);
        progressBar = findViewById(R.id.progressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference("boxes");

        boxId = getIntent().getExtras().getString(BOX_ID_KEY);
        uuID = getIntent().getExtras().getString(UUID_KEY);

        setOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String edtext1 =orderIdEt.getText().toString();
                    String edtext2 = orderIdEt.getText().toString();
                    String edtext3 = orderIdEt.getText().toString();
                    databaseReference.setValue(edtext1);
                    databaseReference.setValue(edtext2);
                    databaseReference.setValue(edtext3);
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
            stringBuilder.append("####");
            stringBuilder.append("@");
            stringBuilder.append(",");
        }

        stringBuilder.append(toggleValue);

        Log.e("Neha", stringBuilder.toString());

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(uuID).setValue(stringBuilder.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
