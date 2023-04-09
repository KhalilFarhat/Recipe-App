package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {


    TextView txt_welcome;
    public AccountFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        String username = "Ismail"; //WE fetch user name using intents
        txt_welcome = view.findViewById(R.id.txt_Welcome);
        txt_welcome.setText("Welcome, "+username);


        return view;
    }

}
