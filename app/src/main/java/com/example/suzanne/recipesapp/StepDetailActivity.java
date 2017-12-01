package com.example.suzanne.recipesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.models.RecipeStep;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnStepArrowClickListener {

    private static final String CURRENT_RECIPE_KEY = "currentRecipe";
    private static final String CURRENT_RECIPE_STEP_KEY = "recipeStep";
    private static final String RECIPE_STEP_DETAIL_KEY = "recipeStepDetail";
    private static final String IS_LAST_STEP_KEY = "isLastStep";
    private static final String IS_FIRST_STEP_KEY = "isFirstStep";
    private static final String FLAG_NEXT = "next";
    private static final String FLAG_PREVIOUS = "previous";

    private Recipe mRecipe;
    private RecipeStep mRecipeStep;
    private int mCurrentStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mRecipe = bundle.getParcelable(CURRENT_RECIPE_KEY);
            mCurrentStepIndex = bundle.getInt(CURRENT_RECIPE_STEP_KEY);
            mRecipeStep = mRecipe.getSteps()[mCurrentStepIndex];
            getSupportActionBar().setTitle(mRecipe.getName());

            Bundle args = new Bundle();
            args.putParcelable(RECIPE_STEP_DETAIL_KEY, mRecipeStep);
//            Notify if we need to hide either arrow
            if(mCurrentStepIndex == 0){
                args.putBoolean(IS_FIRST_STEP_KEY, true);
            } else {
                args.putBoolean(IS_FIRST_STEP_KEY, false);
            }

            if (isLastStep(mCurrentStepIndex)){
                args.putBoolean(IS_LAST_STEP_KEY, true);
            } else {
                args.putBoolean(IS_LAST_STEP_KEY, false);
            }


            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_fragment_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onStepArrowClicked(String flag) {
        Bundle args = new Bundle();

        if(FLAG_NEXT.equals(flag)){
            // Only do something if we're not already on the last step
            if (isLastStep(mCurrentStepIndex)){ return; }
//            increment the step we're on and update the current step
            mCurrentStepIndex++;
            mRecipeStep = mRecipe.getSteps()[mCurrentStepIndex];
//           bundle up the recipe step needed, and whether or not next/back arrows should show
            args.putParcelable(RECIPE_STEP_DETAIL_KEY, mRecipeStep);
            args.putBoolean(IS_FIRST_STEP_KEY, false);
            args.putBoolean(IS_LAST_STEP_KEY, isLastStep(mCurrentStepIndex));

//          Replace the fragment visible with the new one
            replaceFragment(args);

        } else if(FLAG_PREVIOUS.equals(flag)){
//            Only do something if we're not on the first step
            if (mCurrentStepIndex == 0){ return; }
//            decrement the step index and update the current step
            mCurrentStepIndex--;
            mRecipeStep = mRecipe.getSteps()[mCurrentStepIndex];
//            create the bundle
            args.putParcelable(RECIPE_STEP_DETAIL_KEY, mRecipeStep);
            boolean isFirstStep = (mCurrentStepIndex == 0);
            args.putBoolean(IS_FIRST_STEP_KEY, isFirstStep );
            args.putBoolean(IS_LAST_STEP_KEY, isLastStep(mCurrentStepIndex));

            replaceFragment(args);
        }


    }

    private void replaceFragment(Bundle args){
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_fragment_container, stepDetailFragment)
                .commit();
    }

    private boolean isLastStep(int index){
        int lastIndex = mRecipe.getSteps().length - 1;
        return (index == lastIndex);
    }
}
