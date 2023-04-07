package com.example.stalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.stalk.Model.Users;
import com.example.stalk.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");



        binding.signInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,SignInActivity.class));
                finish();
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        if(auth.getCurrentUser()!=null)
        {
            if(auth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                finish();
            }
        }

    }

    private void registerUser()
    {
        if(binding.textInputEmail.getText().toString().isEmpty())
        {
            binding.textInputEmail.setError("Email is Required");
            binding.textInputEmail.requestFocus();
            return ;
        }
        if(binding.textInputFirstName.getText().toString().isEmpty())
        {
            binding.textInputFirstName.setError("Name is Required");
            binding.textInputFirstName.requestFocus();
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
        auth.createUserWithEmailAndPassword(binding.textInputEmail.getText().toString().trim(),binding.textInputPassword.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userid = firebaseUser.getUid();
                           DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            progressDialog.dismiss();
                            HashMap<String, Object> hashMap = new HashMap<>();
                             hashMap.put("atcoderId","");
                             hashMap.put("codechefId","");
                             hashMap.put("codeforceId","");
                             hashMap.put("emaill",binding.textInputEmail.getText().toString());
                             hashMap.put("fullName",binding.textInputFirstName.getText().toString());
                             hashMap.put("leetcodeId","");
                             hashMap.put("password",binding.textInputPassword.getText().toString());
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        sendVerificationEmail();
                                        Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,"Please Verify Your Email Address ",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,SignInActivity.class));
                            finish();

                        }
                        else
                        {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    }
                });

    }

}