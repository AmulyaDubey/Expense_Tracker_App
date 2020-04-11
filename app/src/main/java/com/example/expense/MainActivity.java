package com.example.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText NameText;
    private EditText passwordText;
    private Button Loginbutton;
    private Button RegisterTextView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NameText=(EditText)findViewById(R.id.NameText);
        passwordText=(EditText)findViewById(R.id.passwordText);
        Loginbutton=(Button)findViewById(R.id.Loginbutton);
        RegisterTextView=(Button)findViewById(R.id.RegisterTextView);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!= null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }


        RegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.example.expense.RegistrationActivity.class));

            }
        });
         Loginbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String user_name = NameText.getText().toString().trim();
                 String user_password = passwordText.getText().toString().trim();
                 if (user_name.length() != 0 && user_password.length() != 0)
                 {
                     mAuth.signInWithEmailAndPassword(user_name, user_password)
                             .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()) {
                                         // Sign in success, update UI with the signed-in user's information
                                         FirebaseUser user = mAuth.getCurrentUser();
                                         startActivity(new Intent(MainActivity.this, HomeActivity.class));

                                     } else {
                                         // If sign in fails, display a message to the user.
                                         Toast.makeText(MainActivity.this, "Authentication failed.",
                                                 Toast.LENGTH_SHORT).show();
                                     }

                                 }
                             });
                 }
                 else {
                     Toast.makeText(MainActivity.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }

}
