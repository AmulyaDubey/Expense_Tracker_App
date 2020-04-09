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
    private ArrayList<Expenditure> list;
    private ArrayAdapter<Expenditure> adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //edit=(EditText)findViewById(R.id.edit);
        mAuth = FirebaseAuth.getInstance();

        UserAdapter adapter = new UserAdapter(this, list);
        listView.setAdapter(adapter);

        //list.add("Hello");
        String user_id=mAuth.getCurrentUser().getUid();
        reference=FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Addition").child("Regular");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Expenditure obj=snapshot.getValue(Expenditure.class);
                    Toast.makeText(ViewActivity.this,""+obj.getAmt()+" "+obj.getOption()+" "+obj.getDes(),Toast.LENGTH_LONG).show();
                    //String txt="";
                    //txt= obj.getDes() +" "+ obj.getAmt() +" "+ obj.getOption();
                    // list.add(txt);
                    list.add(obj);
                }

                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(ViewActivity.this,""+e,Toast.LENGTH_SHORT).show();
            }
        });
    }


}

