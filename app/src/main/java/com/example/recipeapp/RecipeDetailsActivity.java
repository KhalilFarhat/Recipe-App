package com.example.recipeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.IngredientsAdapter;
import com.example.recipeapp.Adapters.InstructionsAdapter;
import com.example.recipeapp.Adapters.SimilarRecipeAdapter;
import com.example.recipeapp.Listeners.InstructionsListener;
import com.example.recipeapp.Listeners.RecipeClickListener;
import com.example.recipeapp.Listeners.RecipeDetailsListener;
import com.example.recipeapp.Listeners.SimilarRecipesListener;
import com.example.recipeapp.Models.InstructionsResponse;
import com.example.recipeapp.Models.RecipeDetailsResponse;
import com.example.recipeapp.Models.SimilarRecipeResponse;
import com.google.firebase.FirebaseApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    //FirebaseFirestore db;
    List<Integer> trial;
    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary, textView_similar_title, textView_similar_serving;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_similar, recycler_meal_instructions;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_recipe_details);
        trial = new ArrayList<>();
        //db = FirebaseFirestore.getInstance();
        findViews();

//        id = Integer.parseInt(getIntent().getStringExtra("id"));
//        id = Integer.parseInt(getIntent().getStringExtra("id"));
//        id = getIntent().getIntExtra("id", 0);
        //Use this the above cause errors
        id = Integer.valueOf(getIntent().getStringExtra("id"));
        RequestManager manager = new RequestManager(getApplicationContext());
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getSimilarRecipes(similarRecipesListener, id);
        manager.getInstructions(instructionsListener,id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();

    }
    private String getPlainTextSummary(String htmlSummary) {
        // Replace <b> tags with <strong> tags
        String summaryWithStrongTags = htmlSummary.replace("<b>", "<strong>").replace("</b>", "</strong>");

        // Convert HTML string to Spannable object
        Spanned spannedSummary = HtmlCompat.fromHtml(summaryWithStrongTags, HtmlCompat.FROM_HTML_MODE_LEGACY);

        // Remove <b> tags using SpannableStringBuilder
        SpannableStringBuilder builder = new SpannableStringBuilder(spannedSummary);
        StyleSpan[] styleSpans = builder.getSpans(0, builder.length(), StyleSpan.class);
        for (StyleSpan styleSpan : styleSpans) {
            if (styleSpan.getStyle() == Typeface.BOLD) {
                int start = builder.getSpanStart(styleSpan);
                int end = builder.getSpanEnd(styleSpan);
                builder.removeSpan(styleSpan);
                builder.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        // Return plain text String
        return builder.toString();
    }


    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_similar = findViewById(R.id.recycler_meal_similar);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);

    }





    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();

            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceUrl);
            textView_meal_summary.setText(response.summary);
            Picasso.get().load(response.image).into(imageView_meal_image);
            Toast.makeText(RecipeDetailsActivity.this, response.id+"", Toast.LENGTH_LONG).show();

            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);
        }
        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {
            Toast.makeText(RecipeDetailsActivity.this, "LOL", Toast.LENGTH_LONG).show();
            Log.d("RESPONSE",response.get(0).toString());
            recycler_meal_similar.setHasFixedSize(true);
            recycler_meal_similar.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailsActivity.this, response, recipeClickListener);
            recycler_meal_similar.setAdapter(similarRecipeAdapter);
        }
        @Override
        public void didError(String errorMessage) {
            Toast.makeText(RecipeDetailsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            Log.d("Error",errorMessage);
        }
    };


    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            Intent intent = new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {
            // Handle error
        }
    };
    public void Bookmark(View v) {
        Toast.makeText(this, "CLICKED BOOKMARK" + id, Toast.LENGTH_LONG).show();
        String username = "TrialID";
        // Retrieve the current user's document from Firebase
        //DocumentReference userRef = db.collection("Accounts").document(username); //Document Path to be changed to username
        dbHelper db = new dbHelper(getApplicationContext());
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sp.getString("email", "");
        ArrayList<Integer> Favorites = db.getBookmarks(email);
        Log.d("omar",Favorites.size() + "");
        }

}

