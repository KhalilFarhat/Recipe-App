package com.example.recipeapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.RandomRecipeAdapter;
import com.example.recipeapp.Listeners.RandomRecipeResponseListener;
import com.example.recipeapp.Listeners.RecipeClickListener;
import com.example.recipeapp.Models.RandomRecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ProgressDialog dialog;
    private RequestManager manager;
    private RandomRecipeAdapter randomRecipeAdapter;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private final List<String> tags = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dialog = new ProgressDialog(requireContext());
        manager = new RequestManager(requireContext());

        dialog.setTitle("Loading...");

        spinner = view.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                requireActivity().getApplicationContext(),
                R.array.tags,
                R.layout.spinner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        return view;
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeResponse response, String message) {
            dialog.dismiss();
            recyclerView = requireView().findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity().getApplicationContext(), 1));
            randomRecipeAdapter = new RandomRecipeAdapter(requireActivity().getApplicationContext(), response.getRecipes(), recipeClickListener());
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(requireActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString().toLowerCase());
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private RecipeClickListener recipeClickListener() {
        return recipeId -> {
            // handle click on recipe
        };
    }
}
