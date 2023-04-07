package com.example.stalk.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stalk.R;
import com.example.stalk.databinding.FragmentCodeforcesBinding;


public class CodeforcesFragment extends Fragment {


    public CodeforcesFragment() {
        // Required empty public constructor
    }

    FragmentCodeforcesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCodeforcesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}