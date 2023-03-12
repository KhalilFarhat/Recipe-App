package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private RecipeAdapter adapter;

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

        new GetRecipesTask().execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class GetRecipesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://tasty.p.rapidapi.com/recipes/list?from=0&size=20&tags=under_30_minutes")
                    .get()
                    .addHeader("X-RapidAPI-Key", "aa6ae0dc19msh6e0cc07dbbaf6e0p1a9929jsndebdafb85b5b")
                    .addHeader("X-RapidAPI-Host", "tasty.p.rapidapi.com")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("GetRecipesTask", "IOException occurred during API call: " + e.getMessage());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("GetRecipesTask", "Exception occurred during API call: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String responseBody) {
            if (responseBody != null) {
                try {
                    JSONArray jsonArray = new JSONObject(responseBody).getJSONArray("results");
                    Log.d("GetRecipesTask", "JSON Array length: " + jsonArray.length()); // add logging statement
                    ArrayList<RecipeAdapter.Recipe> recipeList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String url = jsonObject.getString("url");
                        recipeList.add(new RecipeAdapter.Recipe(name, url));
                    }
                    adapter.setRecipeList(recipeList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

