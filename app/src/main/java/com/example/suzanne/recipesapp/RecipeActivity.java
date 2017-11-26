package com.example.suzanne.recipesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.suzanne.recipesapp.models.Recipe;

public class RecipeActivity extends AppCompatActivity {

    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Recipe recipe = getIntent().getParcelableExtra(RECIPE_ACTIVITY_EXTRA_KEY);
        Log.d("recipe", recipe.getName());
    }
}
