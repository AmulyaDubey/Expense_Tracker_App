package com.example.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText NameText, passwordtext, email, repassword;
    private static final String TAG = "RegistrationActivity,";
    private Button Register;
    private TextView LoginRedirect;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    String user_id;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //Toast.makeText(RegistrationActivity.this, "Noted", Toast.LENGTH_SHORT).show();
                    final String user_email =email.getText().toString().trim();
                    final String user_name=NameText.getText().toString().trim();
                    String user_password = passwordtext.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){
                              user_id=mAuth.getCurrentUser().getUid();
                              User obj=new User(user_name,user_email);
                               DatabaseReference myRef = db.getReference("users");
                               myRef.child(user_id).setValue(obj).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(RegistrationActivity.this,""+e,Toast.LENGTH_SHORT).show();
                                   }
                               });
                              Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT ).show();
                              startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                          }
                          else {
                              String s = "Sign up Failed" + task.getException();
                              Toast.makeText(RegistrationActivity.this, s,
                                      Toast.LENGTH_SHORT).show();
                          }
                          }
                    });
                }
            }
        });
        LoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

    }

    ;

    private void setupUIViews() {
        NameText = (EditText) findViewById(R.id.NameText);
        passwordtext = (EditText) findViewById(R.id.passeditText);
        email = (EditText) findViewById(R.id.emailText);
        Register = (Button) findViewById(R.id.regbutton);
        LoginRedirect = (TextView) findViewById(R.id.Login);
        repassword= (EditText) findViewById(R.id.repassword);
    }

    private Boolean validate() {
        Boolean result = false;

        String name = NameText.getText().toString();
        String password = passwordtext.getText().toString();
        String password2= repassword.getText().toString();
        String user_email = email.getText().toString();

        if (name.isEmpty() || password.isEmpty() || user_email.isEmpty() ) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else if(password.length()<6){
            Toast.makeText(this, "Password Length must be greater than 6 characters", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(password2)){
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
        }
        else{
            result=true;
        }
        return result;
    }



}

