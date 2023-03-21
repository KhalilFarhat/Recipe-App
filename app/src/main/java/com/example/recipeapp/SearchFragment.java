package com.example.recipeapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    EditText search_bar;
    Button searchButton;
//    String url = "https://tasty.p.rapidapi.com/recipes/auto-complete?prefix=";
    String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
    String API_KEY = "aa6ae0dc19msh6e0cc07dbbaf6e0p1a9929jsndebdafb85b5b";

    private RecipeAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        search_bar = view.findViewById(R.id.search_bar);
        searchButton = view.findViewById(R.id.search_btn);
        RecyclerView recyclerView = view.findViewById(R.id.search_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = search_bar.getText().toString();
                GetRecipe(query);
            }
        });

    return view;
    }

    public void GetRecipe(String recipe) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+recipe)
                .get()
                .build();
//                .addHeader("X-RapidAPI-Key", API_KEY)
//                .addHeader("X-RapidAPI-Host", "tasty.p.rapidapi.com")

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error during API call: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.d(TAG, "API Response: " + responseString);
                ArrayList<RecipeAdapter.Recipe> recipeNames = decodeResponse(responseString);
                displayResults(recipeNames);
            }
        });
    }
    public void displayResults(ArrayList<RecipeAdapter.Recipe> recipeNames) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setRecipeList(recipeNames);
            }
        });
    }
    private ArrayList<RecipeAdapter.Recipe> decodeResponse(String json) {
        ArrayList<RecipeAdapter.Recipe> recipes = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                String type = resultObject.getString("type");
                Log.d(TAG, "API Response: " + type);

                if (type.equals("ingredient")) {
                    String recipeName = resultObject.getString("display");
                    RecipeAdapter.Recipe recipe = new RecipeAdapter.Recipe(recipeName,"LOL");
                    recipes.add(recipe);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
