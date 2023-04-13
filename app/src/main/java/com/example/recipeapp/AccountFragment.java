package com.example.recipeapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.RandomRecipeAdapter;
import com.example.recipeapp.Listeners.RecipeClickListener;
import com.example.recipeapp.Listeners.RecipeDetailsListener;
import com.example.recipeapp.Models.RandomRecipe;
import com.example.recipeapp.Models.RecipeDetailsResponse;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    FirebaseFirestore db;
    private ProgressDialog dialog;
    private RequestManager manager;
    private RandomRecipeAdapter randomRecipeAdapter;
    private RecyclerView recyclerView;
    private final List<String> tags = new ArrayList<>();
    private final List<Integer> IDS = new ArrayList<>();
    List<Integer> bookmarks;
    List<RandomRecipe> recipeList;
    TextView txt_welcome;
    List<Integer> timelist;
    TextView txtview;
    Button SignOutBtn;
    RandomRecipe randomrecipe_obj;

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
        dbHelper myDB = new dbHelper(getContext());
        db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_account, container, false);
//         Inflate the layout for this fragment
        String username = myDB.getUsername(sp.getString("email", ""));
        txt_welcome = view.findViewById(R.id.txt_Welcome);
        txt_welcome.setText("Welcome, "+username);

        SignOutBtn = view.findViewById(R.id.SignOutBtn);
        SignOutBtn.setOnClickListener(view1
                -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("IsSignedIn", false).commit();
            editor.putString("email", "").commit();
            startActivity(new Intent(getActivity(),WelcomeActivity.class));
        });

        RequestManager manager = new RequestManager(getActivity().getApplicationContext());



        recipeList = new ArrayList<>();
//        db.collection("Accounts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("TAG", document.getId() + " => " + document.getData().get("Bookmarks"));
//                                bookmarks = (List<Integer>) document.get("Bookmarks");
//                            }
//
//                            Log.d("Finally",bookmarks.get(0)+"");
//                            IDS = bookmarks;
//                            Log.d("TAG", IDS.size()+" lolol");
//                            for(int i = 0; i < IDS.size(); i++) {
//                            Log.d("TAG", "in fetch woow");
//                                RandomRecipe randomRecipeObj = new RandomRecipe(); // create new instance for each iteration
//                                manager.getRecipeDetails(new RecipeDetailsListener() {
//                                    @Override
//                                    public void didFetch(RecipeDetailsResponse response, String message) {
//                                        randomRecipeObj.id = response.id;
//                                        randomRecipeObj.image = response.image;
//                                        randomRecipeObj.imageType = response.imageType;
//                                        randomRecipeObj.title = response.title;
//                                        randomRecipeObj.servings = response.servings;
//                                        randomRecipeObj.readyInMinutes = response.readyInMinutes;
//                                        recipeList.add(randomRecipeObj);
//
//                                        if(recipeList.size() == IDS.size()) {
//                                            // all recipe details fetched, update recycler view
//                                            recyclerView = requireView().findViewById(R.id.bookmark_rv);
//                                            recyclerView.setHasFixedSize(true);
//                                            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity().getApplicationContext(), 1));
//                                            randomRecipeAdapter = new RandomRecipeAdapter(requireActivity().getApplicationContext(), recipeList, recipeClickListener());
//                                            recyclerView.setAdapter(randomRecipeAdapter);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void didError(String message) {
//                                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                    }
//                                }, IDS.get(i));
//                            }
//                        } else {
//                            Log.w("TAG", "Error getting documents.", task.getException());
//                        }
//                    }
//                });





        return view;
    }

    private RecipeClickListener recipeClickListener() {
        return recipeId -> {
            // handle click on recipe
        };
    }
//    public void get(){
//        db.collection("Accounts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("TAG", document.getId() + " => " + document.getData().get("Bookmarks"));
//                                bookmarks = (List<Integer>) document.get("Bookmarks");
//                            }
//                            Log.d("Finally",bookmarks.get(0)+"");
//                            IDS = bookmarks;
//                            Log.d("TAG", IDS.size()+" lolol");
//                        } else {
//                            Log.w("TAG", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
//    }


}