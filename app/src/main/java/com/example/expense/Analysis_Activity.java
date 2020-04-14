package com.example.expense;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Analysis_Activity extends AppCompatActivity {

    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private ArrayList<BarEntry> values;
    private int arr[];
    private TextView year_sel;
    private Button next_analysis;
    private int i, count;
    BarDataSet barDataSet;
    BarData barData;
    String nYear, nMonth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_);

        final BarChart bchart = (BarChart) findViewById(R.id.bargraph);
        arr = new int[12];
        values = new ArrayList<>();
        year_sel=findViewById(R.id.year_sel);
        next_analysis=findViewById(R.id.next_analysis);
        next_analysis.setEnabled(false);
        barData = new BarData();

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Expense");


        year_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                int yearSelected;
                int monthSelected;

                Calendar calendar = Calendar.getInstance();
                yearSelected = calendar.get(Calendar.YEAR);
                monthSelected = calendar.get(Calendar.MONTH);

                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected);

                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear)
                    {
                        String brr[]= new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                        year_sel.setText(""+year);
                        next_analysis.setText("Check Month Analysis of "+brr[monthOfYear]);
                        next_analysis.setEnabled(true);
                        nYear=""+year;
                        nMonth=""+monthOfYear;

                        int start_stamp=Integer.parseInt(""+nYear+"101");
                        int end_stamp=Integer.parseInt(""+nYear+"3112");
                        if(start_stamp>0) start_stamp*= -1;
                        if(end_stamp>0) end_stamp*=-1;
                        //Toast.makeText(Analysis_Activity.this, ""+start_stamp+" "+end_stamp, Toast.LENGTH_SHORT).show();
                        db.orderByChild("stamp").startAt(end_stamp).endAt(start_stamp).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Toast.makeText(Analysis_Activity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                                for (i = 0; i <= 11; i++) {

                                    count = 0;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Expenditure obj = snapshot.getValue(Expenditure.class);
                                        String a = obj.getDate().substring(6);
                                        String b = obj.getDate().substring(3, 5);
                                        int bb = Integer.parseInt(b);
                                        if (bb == (i + 1)) {
                                            count+= Integer.parseInt(obj.getAmt());
                                        }
                                    }
                                    arr[i] = count;
                                   // Toast.makeText(Analysis_Activity.this, ""+arr[2]+" "+arr[3], Toast.LENGTH_SHORT).show();
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
                                values = new ArrayList<>();
                                for (int j = 0; j < 12; j++) {
                                    values.add(new BarEntry(j, arr[j]));
                                }
                                barDataSet = new BarDataSet(values, "");
                                barData.addDataSet(barDataSet);
                                bchart.setData(barData);
                                barData.setBarWidth(0.8f);
                                barDataSet.setColors(new int[]{ContextCompat.getColor(Analysis_Activity.this, R.color.blue),
                                        ContextCompat.getColor(
                                                Analysis_Activity.this, R.color.yellow),
                                        ContextCompat.getColor(
                                                Analysis_Activity.this, R.color.green),
                                        ContextCompat.getColor(
                                                Analysis_Activity.this, R.color.orange),
                                        ContextCompat.getColor(
                                                Analysis_Activity.this, R.color.purple),
                                        ContextCompat.getColor(
                                                Analysis_Activity.this, R.color.red),
                                });
                                String[] xAxisLables = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                                XAxis xAxis = bchart.getXAxis();
                                xAxis.setLabelCount(12);
                                bchart.getLegend().setWordWrapEnabled(true);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLables));
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setGranularityEnabled(true);
                                xAxis.setLabelRotationAngle(-90);
                                barData.setBarWidth(0.8f);
                                bchart.getXAxis().setDrawGridLines(false);
                                bchart.setScaleEnabled(false);
                                bchart.getAxisLeft().setAxisMinimum(0);
                                bchart.getAxisRight().setAxisMinimum(0);
                                bchart.invalidate();
                                Description description=new Description();
                                description.setText("");
                                bchart.setDescription(description);
                                xAxis.setTextSize(10f);
                            }
                        },1500);
                    }
                });


            }
        });

        next_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(Analysis_Activity.this, nYear+" "+nMonth, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Analysis_Activity.this, Analysis_2_Activity.class);
                intent.putExtra("Year",nYear);
                intent.putExtra("Month",nMonth);
                startActivity(intent);
            }
        });
    }
}

