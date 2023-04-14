package com.example.recipeapp.Listeners;

import com.example.recipeapp.Models.InstructionsResponse;

import java.util.List;

public interface InstructionsListener {
    public abstract void didFetch(List<InstructionsResponse> response, String message);
    public abstract void didError(String message);
}
