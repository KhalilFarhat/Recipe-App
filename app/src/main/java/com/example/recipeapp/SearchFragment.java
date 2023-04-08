package com.example.recipeapp;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.RandomRecipeAdapter;
import com.example.recipeapp.Listeners.RandomRecipeResponseListener;
import com.example.recipeapp.Models.RandomRecipeResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchview;
    List<String> tags = new ArrayList<>();
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchview = view.findViewById(R.id.searchview_home);

        dialog = new ProgressDialog(view.getContext());
        manager = new RequestManager(view.getContext());

        dialog.setTitle("Loading...");


        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query.toLowerCase());
                manager.getRandomRecipes(randomRecipeResponseListener,tags);
                dialog.show();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    return view;
    }
//API calls
private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
    @Override
    public void didFetch(RandomRecipeResponse response, String message) {
        dialog.dismiss();
        recyclerView = getView().findViewById(R.id.search_RV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),1));
        randomRecipeAdapter = new RandomRecipeAdapter(getActivity().getApplicationContext(),response.getRecipes());
        recyclerView.setAdapter(randomRecipeAdapter);
    }

    @Override
    public void didError(String message) {
        Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
};
    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString().toLowerCase());
            manager.getRandomRecipes(randomRecipeResponseListener,tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
