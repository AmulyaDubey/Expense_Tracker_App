package com.example.expense;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SpendActivity extends AppCompatActivity {

    private TextView mDisplayDate;
    private FirebaseAuth mAuth;
    private long maxid = 0;
    private FirebaseDatabase db;
    private DatabaseReference reff;
    private TextView name_add, enter_amt, details_add;
    private EditText giver, amt;
    private Button save_btn;
    private String id;
    private int next=-1;
    private FloatingActionButton fab;
    private static final String TAG = "SpendActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spend);

        name_add = (TextView) findViewById(R.id.name_add2);
        details_add = (TextView) findViewById(R.id.details_add2);
        enter_amt = (TextView) findViewById(R.id.enter_amt2);
        giver = (EditText) findViewById(R.id.giver2);
        amt = (EditText) findViewById(R.id.amt2);
        save_btn = (Button) findViewById(R.id.save_btn2);
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        final Spinner myspinner = (Spinner) findViewById(R.id.spinner1);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SpendActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.drop));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);

        mDisplayDate = (TextView) findViewById(R.id.tvDate2);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SpendActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date;
                if(month<=9){
                    date = dayOfMonth + "-0" + month + "-" + year;
                }
                else{
                    date = dayOfMonth + "-" + month + "-" + year;
                }

                mDisplayDate.setText(date);
            }


        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpendActivity.this, HomeActivity.class));
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = myspinner.getSelectedItem().toString();
                final String a = giver.getText().toString().trim();
                final String b = amt.getText().toString().trim();
                final String c = mDisplayDate.getText().toString().trim();
                String user_id = mAuth.getCurrentUser().getUid();


                reff = FirebaseDatabase.getInstance().getReference("users").child(user_id);

                reff.child("Index_Expense").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        id = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (validate_amt(b)) {
                    if (c.length() != 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(AddActivity.this, id, Toast.LENGTH_SHORT).show();
                                next=Integer.parseInt(id);
                                //Toast.makeText(AddActivity.this,""+next, Toast.LENGTH_SHORT).show();
                                reff.child("Index_Expense").setValue(""+(next+1));
                                int stamp = Integer.parseInt(c.substring(6) + c.substring(3, 5) + c.substring(0, 2));
                                stamp = stamp * (-1);
                                final Expenditure obj = new Expenditure(c, a, b, text, stamp, "Expense");
                                reff.child("Expense").child(String.valueOf(next)).setValue(obj).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SpendActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SpendActivity.this, "Money Deducted Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SpendActivity.this, HomeActivity.class));
                                    }
                                });
                            }
                        }, 500);
                    } else {
                        Toast.makeText(SpendActivity.this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SpendActivity.this, "Amount should be a number", Toast.LENGTH_SHORT).show();
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