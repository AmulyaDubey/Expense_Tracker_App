package com.example.expense;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context context;
    ArrayList<Expenditure> profiles;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText giver_amt ;
    private EditText giver_name ;
    private TextView update_date;
    private Button update_set;


    public UserAdapter(Context c, ArrayList<Expenditure> p) {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.items_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.mOption.setText(profiles.get(position).getDes());
        holder.mDate.setText(profiles.get(position).getDate());
        holder.mAmt.setText("Rs. " + (profiles.get(position).getAmt()));
        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final String date=profiles.get(position).getDate();
                        final String description=profiles.get(position).getDes();
                        final String amt=profiles.get(position).getAmt();
                        final String option=profiles.get(position).getOption();
                        final String type=profiles.get(position).getType();
                        final int stamp=profiles.get(position).getStamp();

                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        final String user_id=mAuth.getCurrentUser().getUid();
                        final DatabaseReference db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child(type);

                        db.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                Expenditure obj=new Expenditure(date, description, amt.substring(1).trim() ,option,stamp,type);
                                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                                {
                                    Expenditure obj1=snapshot.getValue(Expenditure.class);
                                    String same="";

                                    if(!obj1.getDes().equals(description)) same+="1";
                                    if(!obj1.getAmt().equals(obj.getAmt())) same+="2";
                                    if(obj1.getStamp()!=stamp) same+="3";
                                    if(!obj1.getType().equals(type)) same+="4";
                                    if(!obj1.getDate().equals(date))  same+="5";
                                    if(!obj1.getOption().equals(option)) same+="6";

                                   if(same.equals(""))
                                   {
                                       snapshot.getRef().removeValue();
                                       notifyDataSetChanged();
                                       Toast.makeText(context, "Entry Deleted Successfully", Toast.LENGTH_SHORT).show();
                                       break;
                                   }
                                   else {
                                       //Toast.makeText(context, ""+same, Toast.LENGTH_SHORT).show();
                                   }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });

       holder.update_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

               LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View myView = inflater.inflate(R.layout.activity_update, null);
               dialog.setView(myView);



               giver_name = (EditText) myView.findViewById(R.id.giver_name);
               giver_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
               //dialog.setView(giver_amt);
               giver_amt = (EditText) myView.findViewById(R.id.giver_amt);
               update_date = (TextView) myView.findViewById(R.id.update_date);
               update_set = myView.findViewById(R.id.update_set);

               dialog.setTitle("Update Details");

               final AlertDialog alertDialog = dialog.create();
               alertDialog.show();
               alertDialog.getWindow().setLayout(1050, 1050);

               //set default values to text
               giver_amt.setText(profiles.get(position).getAmt().substring(1));
               giver_name.setText(profiles.get(position).getDes());
               update_date.setText(profiles.get(position).getDate());

               //Set date picker dialogue

               update_date.setOnClickListener(new View.OnClickListener() {
                   @RequiresApi(api = Build.VERSION_CODES.N)
                   @Override
                   public void onClick(View v) {
                       Calendar cal = Calendar.getInstance();
                       int year = cal.get(Calendar.YEAR);
                       int month = cal.get(Calendar.MONTH);
                       int day = cal.get(Calendar.DAY_OF_MONTH);

                       DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                       dialog.show();
                   }
               });

               mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       month = month + 1;
                       String date = dayOfMonth + "-" + month + "-" + year;
                       update_date.setText(date);
                   }
               };

               //Set spinner
               final Spinner myspinner = (Spinner) myView.findViewById(R.id.spinner_upd);
               if (profiles.get(position).getType().equals("Addition")) {
                   String arr[] = {"Regular", "Self Salary", "Gift", "Others"};
                   ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arr);
                   myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   myspinner.setAdapter(myAdapter);
               } else {
                   String arr[] = {"Home EMI", "Monthly Ration", "Bills", "Fees", "Purchases", "Staples", "Other"};
                   ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arr);
                   myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   myspinner.setAdapter(myAdapter);
               }

                // All set.. Now read values
               update_set.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v)
                   {
                       FirebaseAuth mAuth=FirebaseAuth.getInstance();
                       final String user_id=mAuth.getCurrentUser().getUid();
                       final DatabaseReference db= FirebaseDatabase.getInstance().getReference("users").child(user_id).child(profiles.get(position).getType());


                       final String date=profiles.get(position).getDate();
                       final String description=profiles.get(position).getDes();
                       final String amt=profiles.get(position).getAmt();
                       final String option=profiles.get(position).getOption();
                       final String type=profiles.get(position).getType();
                       final int stampp=profiles.get(position).getStamp();


                       db.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                           {
                               for(DataSnapshot snapshot: dataSnapshot.getChildren())
                               {
                                   Expenditure obj1=snapshot.getValue(Expenditure.class);
                                   String same="";

                                   if(!obj1.getDes().equals(description)) same+="1";
                                   if(!obj1.getAmt().equals(amt.substring(1))) same+="2";
                                   if(obj1.getStamp()!=stampp) same+="3";
                                   if(!obj1.getType().equals(type)) same+="4";
                                   if(!obj1.getDate().equals(date))  same+="5";
                                   if(!obj1.getOption().equals(option)) same+="6";

                                   if(same.equals(""))
                                   {
                                       final String a=giver_name.getText().toString();
                                       String b=giver_amt.getText().toString();
                                       String c=update_date.getText().toString();
                                       String d=myspinner.getSelectedItem().toString();
                                       int stamp = Integer.parseInt(c.substring(5) + c.substring(3, 4) + c.substring(0, 2));
                                       stamp = stamp * (-1);
                                       String e=profiles.get(position).getType();

                                       if((!a.equals("")) && (!b.equals("")) && (!c.equals(""))){
                                           final Expenditure obj=new Expenditure(c,a,b,d,stamp,e);

                                           snapshot.getRef().setValue(obj);
                                           notifyDataSetChanged();
                                           Toast.makeText(context, "Entry Updated Successfully", Toast.LENGTH_SHORT).show();
                                           alertDialog.dismiss();
                                           break;
                                       }
                                       else{
                                           Toast.makeText(context, "You cannot leave entries empty!", Toast.LENGTH_SHORT).show();
                                       }

                                   }
                                   else
                                       {

                                   }


                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                   }
               });
           }
       });
    }


    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        Button del_btn,update_btn;
        TextView mOption, mDate, mAmt;
        RecyclerView parentlayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            mOption = itemView.findViewById(R.id.mOption);
            mAmt = itemView.findViewById(R.id.mAmt);
            mDate = itemView.findViewById(R.id.mDate);
            update_btn=itemView.findViewById(R.id.update_btn);
            del_btn=itemView.findViewById(R.id.del_btn);
            parentlayout = itemView.findViewById(R.id.recyclerView);
        }
    }


}