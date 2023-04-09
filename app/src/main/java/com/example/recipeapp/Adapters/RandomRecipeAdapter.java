package com.example.recipeapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Listeners.RecipeClickListener;
import com.example.recipeapp.Models.RandomRecipe;
import com.example.recipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeAdapter.RandomRecipeViewHolder> {
    Context context;
    List<RandomRecipe> list;
    RecipeClickListener listener;

    public RandomRecipeAdapter(Context context, List<RandomRecipe> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false);
        return new RandomRecipeViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);
        holder.textView_servings.setText(list.get(position).getServings() + " persons");
        holder.textView_duration.setText(list.get(position).getReadyInMinutes() + " mins");
        Picasso.get().load(list.get(position).getImage()).into(holder.imageView_food);

        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class RandomRecipeViewHolder extends RecyclerView.ViewHolder {
        CardView random_list_container;
        TextView textView_title, textView_servings, textView_duration;
        ImageView imageView_food;

        public RandomRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            random_list_container = itemView.findViewById(R.id.RandomRecipeContainer);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_servings = itemView.findViewById(R.id.textView_servings);
            textView_duration = itemView.findViewById(R.id.textView_duration);
            imageView_food = itemView.findViewById(R.id.imageView_food);
        }
    }
}
