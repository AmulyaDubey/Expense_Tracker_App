package com.example.expense;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        listView=(ListView)findViewById(R.id.listView);
        edit=(EditText)findViewById(R.id.edit);
        mAuth = FirebaseAuth.getInstance();

        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter adapter=new ArrayAdapter<String>(ViewActivity.this,R.layout.activity_view,R.id.listView ,list);
        listView.setAdapter(adapter);
        list.add("Hello");
        String user_id=mAuth.getCurrentUser().getUid();
        //Toast.makeText(ViewActivity.this,user_id,Toast.LENGTH_SHORT).show();
        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition").child("Regular");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Toast.makeText(ViewActivity.this,""+dataSnapshot.getChildrenCount(),Toast.LENGTH_LONG).show();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                     Expenditure obj=snapshot.getValue(Expenditure.class);
                    String txt="";
                    txt= obj.getDes() +" "+ obj.getAmt() +" "+ obj.getOption();
                    list.add(txt);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(ViewActivity.this,""+e,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
