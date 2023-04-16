package com.example.recipeapp;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.text.HtmlCompat;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;
public class RecipeDetailsActivity extends AppCompatActivity {

    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary, textView_similar_title, textView_similar_serving;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_similar, recycler_meal_instructions;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;
    ImageView bookmarkIcon;
    Boolean bookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_details);
        findViews();
        dbHelper db = new dbHelper(getApplicationContext());
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sp.getString("email", "");
        id = Integer.valueOf(getIntent().getStringExtra("id"));
        bookmarkIcon = findViewById(R.id.bookmark);
        bookmarked = db.isBookmarked(id,email);
        if(!bookmarked){
            bookmarkIcon.setImageResource(R.drawable.bookmark_empty_new);
        }
        else {
            bookmarkIcon.setImageResource(R.drawable.bookmark_filled_new);
        }
        RequestManager manager = new RequestManager(getApplicationContext());
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getInstructions(instructionsListener,id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();
    }



    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
//        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
//        recycler_meal_similar = findViewById(R.id.recycler_meal_similar);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);

        ImageView shareButton = findViewById(R.id.share);
        TextView recipeSourceTextView = findViewById(R.id.textView_meal_source);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the recipe source
                String recipeSource = recipeSourceTextView.getText().toString();

                // Copy the recipe source to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Recipe Source", recipeSource);
                clipboard.setPrimaryClip(clip);

                // Show toast message
                Toast.makeText(getApplicationContext(), "Source Link Copied", Toast.LENGTH_SHORT).show();
            }
        });
//        String mealSummaryHtml = textView_meal_summary.getText().toString();

//        String mealSummaryPlainText = String.valueOf(HtmlCompat.fromHtml(mealSummaryHtml,HtmlCompat.FROM_HTML_MODE_LEGACY));

//        textView_meal_summary.setText(mealSummaryPlainText);

    }




    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {


            dialog.dismiss();

            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceUrl);
//
            Picasso.get().load(response.image).transform(new RoundedCornersTransformation(100,0)).into(imageView_meal_image);
            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);
        }
        @Override
        public void didError(String message) {

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
        dbHelper db = new dbHelper(getApplicationContext());
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sp.getString("email", "");
        bookmarked = db.isBookmarked(id,email);
        if(!bookmarked){
            if(!db.areBookmarksFull(email)) {
                bookmarkIcon.setImageResource(R.drawable.bookmark_filled_new);
                db.addBookmark(id, email);
            }
            else {
                Toast.makeText(this, "Cannot add more than 5 bookmarks!",Toast.LENGTH_LONG).show();
            }
        }
        else {
            bookmarkIcon.setImageResource(R.drawable.bookmark_empty_new);
            db.removeBookmark(id,email);
        }
    }



}

