package com.example.stalk.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.stalk.MainActivity;
import com.example.stalk.Model.Users;
import com.example.stalk.R;
import com.example.stalk.databinding.FragmentEditProfileBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfileFragment extends Fragment {


    public EditProfileFragment() {
        // Required empty public constructor
    }

    FragmentEditProfileBinding binding;
    FirebaseUser user;
    DatabaseReference reference;
    String uid;
    FirebaseDatabase database;
    String password;
    Uri profilrPicUri ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();
        database = FirebaseDatabase.getInstance();

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if(users!=null) {
                    String fullName = users.fullName;
                    binding.userName.setText(fullName);
                    binding.email.setText(users.emaill);
                    password = users.password;
                    if (snapshot.hasChild("codechefId")) {
                        binding.codechefId.setText(users.codechefId);
                    }
                    if (snapshot.hasChild("codeforceId")) {
                        binding.codeforcesId.setText(users.codeforceId);
                    }
                    if(snapshot.hasChild("atcoderId"))
                    {
                        binding.atcoderId.setText(users.atcoderId);
                    }
                    if(snapshot.hasChild("leetcodeId"))
                    {
                        binding.leetcodeId.setText(users.leetcodeId);
                    }
                    if(snapshot.hasChild("profileImg"))
                    {
                        binding.profileImageUser.setImageURI(Uri.parse(users.profileImg));
                        profilrPicUri = Uri.parse(users.profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });


        binding.pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditProfileFragment.this).crop()
                        .start();
            }
        });

        return binding.getRoot();
    }


    private void updateUserProfile()
    {
        if(binding.userName.getText().toString().isEmpty())
        {
            binding.userName.setError("Name is required");
            binding.userName.requestFocus();
            return;
        }

        Users users = new Users();
        users.setFullName(binding.userName.getText().toString());
        users.setEmaill(binding.email.getText().toString());
        users.setPassword(password);
        if(!binding.codechefId.getText().toString().isEmpty())
        {
            users.setCodechefId(binding.codechefId.getText().toString());
        }
        if(!binding.codeforcesId.getText().toString().isEmpty())
        {
            users.setCodeforceId(binding.codeforcesId.getText().toString());
        }
        if(!binding.atcoderId.getText().toString().isEmpty())
        {
            users.setAtcoderId(binding.atcoderId.getText().toString());
        }
        if(!binding.leetcodeId.getText().toString().isEmpty())
        {
            users.setLeetcodeId(binding.leetcodeId.getText().toString());
        }
        if(!Uri.EMPTY.equals(profilrPicUri))
        {
            users.setProfileImg(profilrPicUri.toString()+"");
        }

        reference.child(uid).setValue(users);
        Toast.makeText(getContext(), "Updated Profile Successfully", Toast.LENGTH_SHORT).show();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer,new HomeFragment());
        transaction.commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        try{
            if(!uri.equals("")) {
                profilrPicUri = uri;
                binding.profileImageUser.setImageURI(uri);
            }
        } catch (Exception e)
        {
            Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
        }
    }



}