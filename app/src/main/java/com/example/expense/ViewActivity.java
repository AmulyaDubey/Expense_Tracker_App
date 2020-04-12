package com.example.expense;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewActivity extends AppCompatActivity {

    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private ArrayList<Expenditure> list;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private TextView from_date,to_date;
    private Button search;
    private int stamp1=0,stamp2=0;
    private DatePickerDialog.OnDateSetListener mDateSetListener1,mDateSetListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mAuth = FirebaseAuth.getInstance();
        final String user_id=mAuth.getCurrentUser().getUid();

        from_date=(TextView)findViewById(R.id.from_date);
        to_date=(TextView)findViewById(R.id.to_date);
        search=(Button)findViewById(R.id.search);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stamp1>0)stamp1 = stamp1 * (-1);
                if(stamp2>0)stamp2 = stamp2 * (-1);
                if(stamp1!=0 && stamp2!=0)
                {
                    db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition");
                    // Toast.makeText(ViewActivity.this, ""+stamp1+" "+stamp2, Toast.LENGTH_SHORT).show();
                    db.orderByChild("stamp").startAt(stamp2).endAt(stamp1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<>();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Expenditure obj=snapshot.getValue(Expenditure.class);
                                Expenditure obj2=new Expenditure(obj.getDate(),obj.getDes(),"+"+obj.getAmt(),obj.getOption(),obj.getStamp(),"Addition");
                                list.add(obj2);
                            }
                            adapter = new UserAdapter(ViewActivity.this,list);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError e) {
                            Toast.makeText(ViewActivity.this,""+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(stamp1==0 && stamp2!=0)
                {
                    db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition");
                    // Toast.makeText(ViewActivity.this, ""+stamp1+" "+stamp2, Toast.LENGTH_SHORT).show();
                    db.orderByChild("stamp").startAt(stamp2).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<>();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Expenditure obj=snapshot.getValue(Expenditure.class);
                                Expenditure obj2=new Expenditure(obj.getDate(),obj.getDes(),"+"+obj.getAmt(),obj.getOption(),obj.getStamp(),"Addition");
                                list.add(obj2);
                            }
                            adapter = new UserAdapter(ViewActivity.this,list);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError e) {
                            Toast.makeText(ViewActivity.this,""+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(stamp1!=0 && stamp2==0)
                {
                    db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition");
                    // Toast.makeText(ViewActivity.this, ""+stamp1+" "+stamp2, Toast.LENGTH_SHORT).show();
                    db.orderByChild("stamp").endAt(stamp1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<>();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Expenditure obj=snapshot.getValue(Expenditure.class);
                                Expenditure obj2=new Expenditure(obj.getDate(),obj.getDes(),"+"+obj.getAmt(),obj.getOption(),obj.getStamp(),"Addition");
                                list.add(obj2);
                            }
                            adapter = new UserAdapter(ViewActivity.this,list);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError e) {
                            Toast.makeText(ViewActivity.this,""+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    {
                        db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition");
                        // Toast.makeText(ViewActivity.this, ""+stamp1+" "+stamp2, Toast.LENGTH_SHORT).show();
                        db.orderByChild("stamp").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Expenditure obj=snapshot.getValue(Expenditure.class);
                                    Expenditure obj2=new Expenditure(obj.getDate(),obj.getDes(),"+"+obj.getAmt(),obj.getOption(),obj.getStamp(),"Addition");
                                    list.add(obj2);
                                }
                                adapter = new UserAdapter(ViewActivity.this,list);
                                recyclerView.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError e) {
                                Toast.makeText(ViewActivity.this,""+e,Toast.LENGTH_SHORT).show();
                            }
                        });

                }

            }
        });

        from_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ViewActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener1, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener1= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "-" + month + "-" + year;
                from_date.setText(date);
                stamp1 = Integer.parseInt(date.substring(5) + date.substring(3, 4) + date.substring(0, 2));
            }
        };

        to_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ViewActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener2, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "-" + month + "-" + year;
                to_date.setText(date);
                stamp2 = Integer.parseInt(date.substring(5) + date.substring(3, 4) + date.substring(0, 2));
            }
        };
    }


}


