package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class AccountFragment extends Fragment {


    //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
    TextView txt_welcome;
    Button SignOutBtn;
    public AccountFragment() {
        // Required empty public constructor

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", 0);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        dbHelper myDB = new dbHelper(getActivity());
        String username = myDB.getUsername(sp.getString("email", ""));
        txt_welcome = view.findViewById(R.id.txt_Welcome);
        txt_welcome.setText("Welcome, " + username);

        SignOutBtn = view.findViewById(R.id.SignOutBtn);
        SignOutBtn.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("IsSignedIn", false);
            editor.putString("email", "");
            editor.commit();
            startActivity(new Intent(getActivity(),WelcomeActivity.class));
        });

        return view;
    }

}
