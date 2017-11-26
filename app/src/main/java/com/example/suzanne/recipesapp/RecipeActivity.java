package com.example.suzanne.recipesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.models.RecipeStep;

public class RecipeActivity extends AppCompatActivity implements MasterRecipeDetailFragment.OnRecipeStepClickListener {

    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";
    private static final String CURRENT_RECIPE_KEY = "currentRecipe";
    private static final String CURRENT_RECIPE_STEP_KEY = "recipeStep";
    private Recipe mRecipe;
    private int mCurrentStepIndex;

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

    @Override
    public void onRecipeStepClick(int stepIndex) {
        mCurrentStepIndex = stepIndex;
        Bundle bundle = new Bundle();
        bundle.putParcelable(CURRENT_RECIPE_KEY, mRecipe);
        bundle.putInt(CURRENT_RECIPE_STEP_KEY, mCurrentStepIndex);

//        TODO LAUNCH THE STEP DETAIL ACTIVITY
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
