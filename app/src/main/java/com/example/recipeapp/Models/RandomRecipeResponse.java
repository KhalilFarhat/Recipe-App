package com.example.recipeapp.Models;

import java.util.List;

public class RandomRecipeResponse {
    List<RandomRecipe> recipes;

    public List<RandomRecipe> getRecipes() {
        return recipes;
    }
    public int id;
    public String title;
    public String image;
    public String imageType;
    public int servings;
    public int readyInMinutes;
    public String sourceUrl;
    public String spoonacularSourceUrl;
    public int aggregateLikes;
    public float spoonacularScore;
    public float healthScore;
    public String creditsText;
    public String license;
    public String sourceName;
    public int likes;
    public int missedIngredientCount;
    public int usedIngredientCount;
    public WinePairing winePairing;
}
