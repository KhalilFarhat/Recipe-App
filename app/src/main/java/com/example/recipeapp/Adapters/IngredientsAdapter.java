package com.example.recipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Models.ExtendedIngredient;
import com.example.recipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    Context context;
    List<ExtendedIngredient> list;

    public IngredientsAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_meal_ingredients, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.textView_ingredient_name.setText(list.get(position).name);
        holder.textView_ingredient_name.setSelected(true);
        holder.textView_ingredient_quantity.setText(list.get(position).original);
        holder.textView_ingredient_quantity.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_250x250/"+ list.get(position).image).into(holder.imageView_ingredients);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView textView_ingredient_quantity, textView_ingredient_name;
        ImageView imageView_ingredients;

        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_ingredient_quantity = itemView.findViewById(R.id.textView_ingredients_quantity);
            textView_ingredient_name = itemView.findViewById(R.id.textView_ingredients_name);
            imageView_ingredients = itemView.findViewById(R.id.imageView_ingredients);
        }
    }
}
