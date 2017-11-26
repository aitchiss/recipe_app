package com.example.suzanne.recipesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.suzanne.recipesapp.models.Recipe;

public class RecipeActivity extends AppCompatActivity {

    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipe = getIntent().getParcelableExtra(RECIPE_ACTIVITY_EXTRA_KEY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_ACTIVITY_EXTRA_KEY, mRecipe);

        MasterRecipeDetailFragment recipeDetailFragment = new MasterRecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.master_recipe_detail_container, recipeDetailFragment)
                .commit();

        getSupportActionBar().setTitle(mRecipe.getName());
    }
}
