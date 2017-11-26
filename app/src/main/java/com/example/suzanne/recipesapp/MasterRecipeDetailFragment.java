package com.example.suzanne.recipesapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class MasterRecipeDetailFragment extends Fragment {

    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";
    private Recipe mRecipe;

    @BindView(R.id.tv_recipe_name)
    TextView mRecipeNameTextView;

    @BindView(R.id.ingredients_recycler_view)
    RecyclerView mIngredientsRecyclerView;

    @BindView(R.id.step_desc_recycler_view)
    RecyclerView mStepsRecyclerView;

    public MasterRecipeDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            mRecipe = bundle.getParcelable(RECIPE_ACTIVITY_EXTRA_KEY);
            mRecipeNameTextView.setText(mRecipe.getName());

            IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(mRecipe.getIngredients());
            RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity(), 1, false);
            mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
            mIngredientsRecyclerView.setAdapter(ingredientsListAdapter);

            RecyclerView.LayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity(), 1, false);
            mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
            StepDescriptionAdapter stepDescriptionAdapter = new StepDescriptionAdapter(mRecipe.getSteps());
            mStepsRecyclerView.setAdapter(stepDescriptionAdapter);

        }

        return rootView;
    }
}
