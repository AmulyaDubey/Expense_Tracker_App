package com.example.expense;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private TextView mDisplayDate;
    long maxid;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference reff;
    private TextView name_add,enter_amt,details_add;
    private EditText giver,amt;
    private Button save_btn;
    private static final String TAG="AddActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name_add = (TextView) findViewById(R.id.name_add);
        details_add = (TextView) findViewById(R.id.details_add);
        enter_amt = (TextView) findViewById(R.id.enter_amt);
        giver = (EditText) findViewById(R.id.giver);
        amt = (EditText) findViewById(R.id.amt);
        save_btn = (Button) findViewById(R.id.save_btn);
        final Spinner myspinner = (Spinner) findViewById(R.id.spinner2);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.drop2));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: " + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "-" + month + "-" + year;
                mDisplayDate.setText(date);
            }
        };


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = myspinner.getSelectedItem().toString();
                String user_id = mAuth.getCurrentUser().getUid();
                reff = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition");

                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            maxid = dataSnapshot.getChildrenCount();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        ;
                    }
                });


                String a = giver.getText().toString().trim();
                String b = amt.getText().toString().trim();
                String c = mDisplayDate.getText().toString().trim();
                if (validate_amt(b)) {
                    if(c.length()!=0) {
                        //Toast.makeText(AddActivity.this, c.substring(5)+c.substring(3,4)+c.substring(0,2), Toast.LENGTH_SHORT).show();
                        int stamp = Integer.parseInt(c.substring(5) + c.substring(3, 4) + c.substring(0, 2));
                        stamp = stamp * (-1);
                        final Expenditure obj = new Expenditure(c, a, b, text, stamp);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                reff.child(String.valueOf(maxid + 1)).setValue(obj).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AddActivity.this, "Money Added Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddActivity.this, HomeActivity.class));
                                    }
                                });
                            }
                        }, 500);
                    }
                    else {
                        Toast.makeText(AddActivity.this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AddActivity.this, "Amount should be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    boolean validate_amt(String s) {
        boolean success = true;
        if(s.length()==0) {
            success = false;
            Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') ;
            else success = false;
        }
        return success;
    }
}
