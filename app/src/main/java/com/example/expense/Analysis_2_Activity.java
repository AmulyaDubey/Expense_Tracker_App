package com.example.expense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Analysis_2_Activity extends AppCompatActivity {

    private PieChart pieChart;
    private int in=0,out=0;
    ArrayList<PieEntry> values;
    ArrayList<PieEntry> pieEntries;
    private int s1=0,s2=0,mon=0;
    int sum[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_2_);

        Intent intent=getIntent();
        String year=intent.getStringExtra("Year");
        String month=intent.getStringExtra("Month");
        mon=Integer.parseInt(month);
        String brr[]= new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        month=brr[mon];
        TextView heading=findViewById(R.id.heading_analysis);
        heading.setText("Analysis For: "+month+" "+year);
        pieChart=(PieChart)findViewById(R.id.piechart);
        Description description=new Description();
        description.setText("Expenditure in Various Areas");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(10f);
        pieChart.setDrawEntryLabels(true);
        pieChart.setTransparentCircleAlpha(0);

        pieEntries=new ArrayList<>();
        pieEntries.add(new PieEntry(2f, "ONE"));
        pieEntries.add(new PieEntry(4f, "TWO"));
        pieEntries.add(new PieEntry(6f, "THREE"));
        pieEntries.add(new PieEntry(8f, "FOUR"));
        pieEntries.add(new PieEntry(7f, "FIVE"));
        pieEntries.add(new PieEntry(3f, "SIX"));


       final String crr[]={"Home EMI","Monthly Ration","Bills","Fees","Purchases","Staples","Others"};
       values=new ArrayList<>();
        sum=new int[7];
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String user_id=mAuth.getCurrentUser().getUid();
        mon++;
        if(mon<=9){
            s1=Integer.parseInt(""+year+"0"+mon+"01");
            s2=Integer.parseInt(""+year+"0"+mon+"31");
        }
        else{
            s1=Integer.parseInt(""+year+""+mon+"01");
            s2=Integer.parseInt(""+year+""+mon+"31");
        }
        if(s1>0) s1*= -1;
        if(s2>0) s2*= -1;
        //Toast.makeText(this, ""+s1+" "+s2, Toast.LENGTH_SHORT).show();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Expense");
        db.orderByChild("stamp").startAt(s2).endAt(s1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //Toast.makeText(Analysis_2_Activity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                for(int i=0;i<7;i++)
                {
                    int total=0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Expenditure obj=snapshot.getValue(Expenditure.class);
                        if(obj.getOption().equals(crr[i]))
                        {
                           // total+= Integer.parseInt(obj.getAmt());
                            total+= Integer.parseInt(obj.getAmt());
                        }
                        sum[i]=total;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                //Toast.makeText(Analysis_2_Activity.this, ""+sum[0], Toast.LENGTH_SHORT).show();
                for(int i=0;i<7;i++){
                    if(sum[i]==0)continue;
                    else values.add(new PieEntry(sum[i],crr[i]));
                }
                PieDataSet pieDataSet = new PieDataSet(values, "");
                PieData pieData=new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                pieDataSet.setSliceSpace(2f);
                pieDataSet.setValueTextColor(Color.WHITE);
                pieDataSet.setValueTextSize(8f);
                pieData.setValueTextSize(20f);
                pieDataSet.setSliceSpace(5f);
            }
        },1500);
    }
}
