package com.example.suzanne.recipesapp;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.models.RecipeStep;

public class RecipeActivity extends AppCompatActivity implements MasterRecipeDetailFragment.OnRecipeStepClickListener, StepDetailFragment.OnStepArrowClickListener {

    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";
    private static final String CURRENT_RECIPE_KEY = "currentRecipe";
    private static final String CURRENT_RECIPE_STEP_KEY = "recipeStep";
    private static final String RECIPE_STEP_DETAIL_KEY = "recipeStepDetail";
    private static final String IS_LAST_STEP_KEY = "isLastStep";
    private static final String IS_FIRST_STEP_KEY = "isFirstStep";
    private static final String FLAG_NEXT = "next";
    private static final String FLAG_PREVIOUS = "previous";
    private static final String STATE_RECIPE_KEY = "recipeState";
    private Recipe mRecipe;
    private int mCurrentStepIndex;
    private boolean mTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipe = getIntent().getParcelableExtra(RECIPE_ACTIVITY_EXTRA_KEY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_ACTIVITY_EXTRA_KEY, mRecipe);

        if(savedInstanceState == null){

            MasterRecipeDetailFragment recipeDetailFragment = new MasterRecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.master_recipe_detail_container, recipeDetailFragment)
                    .commit();

            if(findViewById(R.id.recipe_horiztonal_layout) != null){
                mTwoPaneMode = true;

                bundle.putParcelable(RECIPE_STEP_DETAIL_KEY, mRecipe.getSteps()[mCurrentStepIndex]);
                if(mRecipe.getSteps().length - 1 == mCurrentStepIndex){
                    bundle.putBoolean(IS_LAST_STEP_KEY, true);
                } else {
                    bundle.putBoolean(IS_LAST_STEP_KEY, false);
                }
                bundle.putBoolean(IS_FIRST_STEP_KEY, (mCurrentStepIndex == 0));
                if(savedInstanceState == null){
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    stepDetailFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_detail_fragment_container, stepDetailFragment)
                            .commit();
                }
            } else {
                mTwoPaneMode = false;
            }

            getSupportActionBar().setTitle(mRecipe.getName());
        }


    }

    @Override
    public void onRecipeStepClick(int stepIndex) {
        mCurrentStepIndex = stepIndex;
        Bundle bundle = new Bundle();
        bundle.putParcelable(CURRENT_RECIPE_KEY, mRecipe);
        bundle.putInt(CURRENT_RECIPE_STEP_KEY, mCurrentStepIndex);
        if (mTwoPaneMode){
            bundle.putParcelable(RECIPE_STEP_DETAIL_KEY, mRecipe.getSteps()[mCurrentStepIndex]);
            if(mRecipe.getSteps().length - 1 == mCurrentStepIndex){
                bundle.putBoolean(IS_LAST_STEP_KEY, true);
            } else {
                bundle.putBoolean(IS_LAST_STEP_KEY, false);
            }
            bundle.putBoolean(IS_FIRST_STEP_KEY, (mCurrentStepIndex == 0));
            updateStepDetailFragment(bundle);
        }else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onStepArrowClicked(String flag) {
        Bundle bundle = new Bundle();
        if(flag.equals(FLAG_PREVIOUS) && mTwoPaneMode){
            mCurrentStepIndex--;
        } else if (flag.equals(FLAG_NEXT)){
            mCurrentStepIndex++;
        }
        bundle.putParcelable(RECIPE_STEP_DETAIL_KEY, mRecipe.getSteps()[mCurrentStepIndex]);
        if(mRecipe.getSteps().length - 1 == mCurrentStepIndex){
            bundle.putBoolean(IS_LAST_STEP_KEY, true);
        } else {
            bundle.putBoolean(IS_LAST_STEP_KEY, false);
        }
        bundle.putBoolean(IS_FIRST_STEP_KEY, (mCurrentStepIndex == 0));
        updateStepDetailFragment(bundle);
    }

    private void updateStepDetailFragment(Bundle bundle){
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_fragment_container, stepDetailFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(STATE_RECIPE_KEY, mRecipe);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_RECIPE_KEY)){
            mRecipe = savedInstanceState.getParcelable(STATE_RECIPE_KEY);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
