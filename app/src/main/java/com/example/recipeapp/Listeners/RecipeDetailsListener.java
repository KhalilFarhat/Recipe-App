package com.example.recipeapp.Listeners;

import com.example.recipeapp.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);

    void didFetch(String recipeDetails, String message);
    void didError(String message);
}
