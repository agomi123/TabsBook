package com.example.stalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.stalk.Fragments.AtcoderFragment;
import com.example.stalk.Fragments.CodechefFragment;
import com.example.stalk.Fragments.CodeforcesFragment;
import com.example.stalk.Fragments.EditProfileFragment;
import com.example.stalk.Fragments.HomeFragment;
import com.example.stalk.Fragments.LeetcodeFragment;
import com.example.stalk.databinding.ActivityMainBinding;
import com.example.stalk.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer,new HomeFragment());
        transaction.commit();

        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(i){
                    case 0:
                        transaction.replace(R.id.fragmentContainer,new AtcoderFragment());
                        break;
                    case 1:
                        transaction.replace(R.id.fragmentContainer,new CodechefFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.fragmentContainer,new HomeFragment());
                        break;
                    case 3:
                        transaction.replace(R.id.fragmentContainer,new CodeforcesFragment());
                        break;
                    default:
                        transaction.replace(R.id.fragmentContainer,new LeetcodeFragment());
                        break;
                }
                transaction.commit();

            }
        });




    }
}