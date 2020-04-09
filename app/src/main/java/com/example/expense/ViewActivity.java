package com.example.expense;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertController;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edit;
    private FirebaseDatabase db;
    private RecyclerView listView;
    private ArrayList<Expenditure> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        listView=(RecyclerView) findViewById(R.id.listView);
        //edit=(EditText)findViewById(R.id.edit);
        mAuth = FirebaseAuth.getInstance();

        final ArrayAdapter adapter=new ArrayAdapter<Expenditure>(ViewActivity.this,R.layout.activity_view,R.id.listView ,list);

        //list.add("Hello");
        String user_id=mAuth.getCurrentUser().getUid();
        //Toast.makeText(ViewActivity.this,user_id,Toast.LENGTH_SHORT).show();
        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition").child("Regular");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //list.clear();
                list = new ArrayList<Expenditure>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                     Expenditure obj=snapshot.getValue(Expenditure.class);
                    Toast.makeText(ViewActivity.this,""+obj.getAmt(),Toast.LENGTH_LONG).show();
                    //String txt="";
                    //txt= obj.getDes() +" "+ obj.getAmt() +" "+ obj.getOption();
                   // list.add(txt);
                }
                adapter = new intern_adapter(ViewActivity.this,list,listView);
                listView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(ViewActivity.this,""+e,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
