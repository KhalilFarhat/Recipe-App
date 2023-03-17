package com.example.recipeapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    String url = "https://tasty.p.rapidapi.com/recipes/auto-complete?prefix=";
//    String url = "https://tasty.p.rapidapi.com/recipes/list?from=0&size=20&tags=under_30_minutes";
//    String url = "https://tasty.p.rapidapi.com/recipes/detail?id=5586";
    String API_KEY = "aa6ae0dc19msh6e0cc07dbbaf6e0p1a9929jsndebdafb85b5b";
    private RecipeAdapter adapter;
    private TextView txtv;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);

//       Get recipe depending on URL
//        GetRecipe(Recipe);
        return view;
    }

    public void GetRecipe(String recipe) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+recipe)
                .get()
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", "tasty.p.rapidapi.com")
                .build();

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

