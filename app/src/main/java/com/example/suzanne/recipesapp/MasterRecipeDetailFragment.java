package com.example.suzanne.recipesapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.models.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class MasterRecipeDetailFragment extends Fragment {

    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";
    private Recipe mRecipe;
    private OnRecipeStepClickListener mRecipeStepClickListener;
    private static final String STEPS_SCROLL_STATE_KEY = "scrollState";

    public interface OnRecipeStepClickListener{
        void onRecipeStepClick(int stepIndex);
    }

    @BindView(R.id.ingredients_recycler_view)
    RecyclerView mIngredientsRecyclerView;

    @BindView(R.id.step_desc_recycler_view)
    RecyclerView mStepsRecyclerView;

    @BindView(R.id.nested_scroll_view_recipe_detail)
    NestedScrollView mNestedScrollView;

    private int mScrollPosition;

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

            IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(mRecipe.getIngredients());
            RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity(), 1, false);
            mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
            mIngredientsRecyclerView.setAdapter(ingredientsListAdapter);

            RecyclerView.LayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity(), 1, false);
            mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
            StepDescriptionAdapter stepDescriptionAdapter = new StepDescriptionAdapter(mRecipe.getSteps(), mRecipeStepClickListener);
            mStepsRecyclerView.setAdapter(stepDescriptionAdapter);

            mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mScrollPosition = scrollY;
                }
            });

        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mRecipeStepClickListener = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey(STEPS_SCROLL_STATE_KEY)){
            mNestedScrollView.setVerticalScrollbarPosition(savedInstanceState.getInt(STEPS_SCROLL_STATE_KEY));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEPS_SCROLL_STATE_KEY, mScrollPosition);
        super.onSaveInstanceState(outState);
    }
}
