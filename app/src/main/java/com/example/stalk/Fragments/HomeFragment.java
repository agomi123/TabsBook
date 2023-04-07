package com.example.stalk.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.stalk.Model.Users;
import com.example.stalk.R;
import com.example.stalk.SignInActivity;
import com.example.stalk.databinding.FragmentHomeBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment  {

    public HomeFragment() {
        // Required empty public constructor
    }

    FragmentHomeBinding binding;
    FirebaseUser user;
    DatabaseReference reference;
    String uid;
    ProgressBar bar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  FragmentHomeBinding.inflate(inflater, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();


        reference.child(uid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users user = snapshot.getValue(Users.class);
                if(user!=null)
                {
                    String fullName = user.fullName;
                    binding.userName.setText(fullName);
                    binding.email.setText(user.emaill);
                   if(snapshot.hasChild("codechefId"))
                   {
                       binding.codechefId.setText(user.codechefId);
                   }
                   if(snapshot.hasChild("codeforceId"))
                   {
                       binding.codeforcesId.setText(user.codeforceId);
                   }
                   if(snapshot.hasChild("atcoderId"))
                   {
                       binding.atcoderId.setText(user.atcoderId);
                   }
                   if(snapshot.hasChild("leetcodeId"))
                   {
                       binding.leetcodeId.setText(user.leetcodeId);
                   }
                    if(snapshot.hasChild("profileImg"))
                    {
                        binding.profileImageUser.setImageURI(Uri.parse(user.profileImg));
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer,new EditProfileFragment());
                transaction.commit();
            }
        });

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),SignInActivity.class));
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }
}