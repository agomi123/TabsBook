package com.example.stalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.stalk.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
ActivitySignInBinding binding;
private FirebaseAuth auth;
FirebaseDatabase database;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("LogIn Account");
        progressDialog.setMessage("We are logging you");

        binding.registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,RegisterActivity.class));
                finish();
            }
        });

        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignIn();
            }
        });

        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(SignInActivity.this,MainActivity.class));
            finish();
        }
    }

    protected void userSignIn(){
        if(binding.textInputEmail.getText().toString().isEmpty())
        {
            binding.textInputEmail.setError("Email is Required");
            binding.textInputEmail.requestFocus();
            return ;
        }
        if(binding.textInputPassword.getText().toString().isEmpty())
        {
            binding.textInputPassword.setError("Password is Required");
            binding.textInputPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(binding.textInputEmail.getText().toString().trim()).matches())
        {

            binding.textInputEmail.setError("Please Provide Valid Email");
            binding.textInputEmail.requestFocus();
            return ;
        }
        progressDialog.show();
        auth.signInWithEmailAndPassword(binding.textInputEmail.getText().toString().trim(),
                binding.textInputPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    user.reload();
                    if(user.isEmailVerified()) {
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    } else {
                        user.sendEmailVerification()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                                    Toast.makeText(SignInActivity.this,"Verify Your Email Address to Procced",Toast.LENGTH_SHORT).show();
                                                }
                                        ///ll
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(SignInActivity.this,"erre",Toast.LENGTH_SHORT).show();

                            }
                        });
//                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful())
//                                {
//                                    Toast.makeText(SignInActivity.this, "Please verify your email.\n Email Verification link is send on gmail"+
//                                            user.getEmail(), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(SignInActivity.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });

                    }
                } else {
                    Toast.makeText(SignInActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}