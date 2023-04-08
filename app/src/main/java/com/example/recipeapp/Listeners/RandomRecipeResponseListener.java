package com.example.recipeapp.Listeners;

import com.example.recipeapp.Models.RandomRecipeResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeResponse response, String message);
    void didError(String message);
}
