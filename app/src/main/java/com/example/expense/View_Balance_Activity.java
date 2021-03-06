package com.example.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class View_Balance_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference db,db1;
    private int addition=0,expenditure=0,balance=0;
    private TextView bal_amt;
    private Button button_total_add, button_total_exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__balance_);

        mAuth = FirebaseAuth.getInstance();
        String user_id=mAuth.getCurrentUser().getUid();
        db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition");
        db1=FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Expense");
        bal_amt=(TextView)findViewById(R.id.mbal);
        button_total_exp=(Button)findViewById(R.id.button_total_exp);
        button_total_add=(Button)findViewById(R.id.button_total_add);


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount()!=0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            Expenditure obj = snapshot.getValue(Expenditure.class);
                            String str = "" + obj.getAmt();
                            addition += Integer.parseInt(str);
                            //Toast.makeText(View_Balance_Activity.this,""+balance, Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(View_Balance_Activity.this,""+balance, Toast.LENGTH_SHORT).show();
                    balance = addition;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Expenditure obj = snapshot.getValue(Expenditure.class);
                    String str=""+obj.getAmt();
                    expenditure += Integer.parseInt(str);
                    //Toast.makeText(View_Balance_Activity.this,""+balance, Toast.LENGTH_SHORT).show();
                }
                balance=balance-expenditure;
                bal_amt.setText("Rs. "+balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button_total_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(View_Balance_Activity.this, ViewActivity.class));
            }
        });
         button_total_exp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(View_Balance_Activity.this, View_Exp_Activity.class));
             }
         });

    }
}
