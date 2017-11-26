package com.example.suzanne.recipesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.models.RecipeStep;

public class StepDetailActivity extends AppCompatActivity {

    private static final String CURRENT_RECIPE_KEY = "currentRecipe";
    private static final String CURRENT_RECIPE_STEP_KEY = "recipeStep";

    private Recipe mRecipe;
    private RecipeStep mRecipeStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mRecipe = bundle.getParcelable(CURRENT_RECIPE_KEY);
            mRecipeStep = mRecipe.getSteps()[bundle.getInt(CURRENT_RECIPE_STEP_KEY)];
            getSupportActionBar().setTitle(mRecipe.getName());
        }
    }
}
