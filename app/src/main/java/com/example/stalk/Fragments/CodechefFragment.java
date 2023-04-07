package com.example.stalk.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stalk.R;
import com.example.stalk.databinding.FragmentCodechefBinding;


public class CodechefFragment extends Fragment {


    public CodechefFragment() {
        // Required empty public constructor
    }
    FragmentCodechefBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCodechefBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}