package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.recipeapp.Listeners.RecipeDetailsListener;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.IngredientsAdapter;
import com.example.recipeapp.Adapters.RandomRecipeAdapter;
import com.example.recipeapp.Listeners.RandomRecipeResponseListener;
import com.example.recipeapp.Listeners.RecipeClickListener;
import com.example.recipeapp.Listeners.RecipeDetailsListener;
import com.example.recipeapp.Models.RandomRecipe;
import com.example.recipeapp.Models.RandomRecipeResponse;
import com.example.recipeapp.Models.RecipeDetailsResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountFragment extends Fragment {


    private RequestManager manager;
    private RandomRecipeAdapter randomRecipeAdapter;
    private RecyclerView recyclerView;

    ProgressDialog dialog;
    List<RandomRecipe> recipeList;
    Button SignOutBtn;
    TextView Welcome;

    public AccountFragment() {
        // Required empty public constructor

    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        RequestManager manager = new RequestManager(getActivity().getApplicationContext());
        dbHelper db = new dbHelper(getContext());
        SharedPreferences sp = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sp.getString("email", "");
        Welcome = view.findViewById(R.id.txt_Welcome);
        Welcome.setText("Welcome, " + db.getUsername(email));
        SignOutBtn = view.findViewById(R.id.SignOutBtn);
        SignOutBtn.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email", "");
            editor.putBoolean("IsSignedIn", false);
            editor.commit();
            startActivity(new Intent(getActivity(),WelcomeActivity.class));
        });
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading Details...");

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        dialog.show();
        RequestManager manager = new RequestManager(getActivity().getApplicationContext());
        Toast.makeText(getActivity().getApplicationContext(), "CALLED ON RESUME", Toast.LENGTH_SHORT).show();
        recipeList = new ArrayList<>();
            dbHelper db = new dbHelper(getContext());
            SharedPreferences sp = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String email = sp.getString("email", "");
            ArrayList<Integer> IDS = db.getBookmarks(email);
            IDS.removeAll(Collections.singleton(0));
            Toast.makeText(getActivity().getApplicationContext(), IDS.size() + "", Toast.LENGTH_SHORT).show();
            if(IDS.size()==0) {
                //Solves bug that causes the RV to show the last thing it held instead of removing it
                dialog.dismiss();
                recyclerView = requireView().findViewById(R.id.bookmark_rv);
                recyclerView.setAdapter(null);
            }

            for (int i = 0; i < IDS.size(); i++) {
                RandomRecipe randomRecipeObj = new RandomRecipe(); // create new instance for each iteration
                manager.getRecipeDetails(new RecipeDetailsListener() {
                    @Override
                    public void didFetch(RecipeDetailsResponse response, String message) {
                        randomRecipeObj.id = response.id;
                        randomRecipeObj.image = response.image;
                        randomRecipeObj.imageType = response.imageType;
                        randomRecipeObj.title = response.title;
                        randomRecipeObj.servings = response.servings;
                        randomRecipeObj.readyInMinutes = response.readyInMinutes;
                        recipeList.add(randomRecipeObj);
                        if (recipeList.size() == IDS.size()) {
                            // all recipe details fetched, update recycler view
                            dialog.dismiss();
                            recyclerView = requireView().findViewById(R.id.bookmark_rv);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity().getApplicationContext(), 1));
                            randomRecipeAdapter = new RandomRecipeAdapter(requireActivity().getApplicationContext(), recipeList, recipeClickListener());
                            recyclerView.setAdapter(randomRecipeAdapter);
                        }

                    }

                    @Override
                    public void didError(String message) {
                    }
                }, IDS.get(i));
            }
        }

    private RecipeClickListener recipeClickListener() {
        return id -> startActivity(new Intent(requireActivity(), RecipeDetailsActivity.class).putExtra("id", id));
    }

}
