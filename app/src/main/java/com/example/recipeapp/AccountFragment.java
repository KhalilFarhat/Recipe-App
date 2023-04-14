package com.example.recipeapp;

import android.annotation.SuppressLint;
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

    private ProgressDialog dialog;
    private RequestManager manager;
    private RandomRecipeAdapter randomRecipeAdapter;
    private RecyclerView recyclerView;
    private final List<String> tags = new ArrayList<>();
//    List<Integer> IDS;
    List<RandomRecipe> recipeList;
    dbHelper myDB;
    TextView txt_welcome;
    List<Integer> timelist;
    TextView txtview;
    Button SignOutBtn;
    RandomRecipe randomrecipe_obj;
    public AccountFragment() {
        // Required empty public constructor

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        RequestManager manager = new RequestManager(getActivity().getApplicationContext());
//        IDS = new ArrayList<>();
        recipeList = new ArrayList<>();
        dbHelper db = new dbHelper(getContext());
        SharedPreferences sp = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sp.getString("email", "");
        ArrayList<Integer> IDS = db.getBookmarks(email);
        SignOutBtn = view.findViewById(R.id.SignOutBtn);
        SignOutBtn.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email", "");
            editor.putBoolean("IsSignedIn", false);
            editor.commit();
            startActivity(new Intent(getActivity(),WelcomeActivity.class));
        });

//        ArrayList<Integer> IDS = new ArrayList<>();
//        IDS.add(716429);
//        IDS.add(638488);
//        IDS.add(652417);
//        IDS.add(715391);
        IDS.removeAll(Collections.singleton(0));
        Toast.makeText(getActivity().getApplicationContext(), IDS.size()+"", Toast.LENGTH_SHORT).show();
        for(int i = 0; i < IDS.size(); i++) {

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
                            recyclerView = requireView().findViewById(R.id.bookmark_rv);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity().getApplicationContext(), 1));
                            randomRecipeAdapter = new RandomRecipeAdapter(requireActivity().getApplicationContext(), recipeList, recipeClickListener());
                            recyclerView.setAdapter(randomRecipeAdapter);
                        }
                    }

                    @Override
                    public void didError(String message) {
                        Toast.makeText(getActivity().getApplicationContext(), "message", Toast.LENGTH_SHORT).show();
                    }
                }, IDS.get(i));
            }

        txtview = view.findViewById(R.id.textView8);
//        Toast.makeText(getActivity().getApplicationContext(), recipeList.size() + "", Toast.LENGTH_SHORT).show();

        return view;
    }
    private RecipeClickListener recipeClickListener() {
        return recipeId -> {
            // handle click on recipe
        };
    }

}
