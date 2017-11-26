package com.example.suzanne.recipesapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class StepDetailFragment extends Fragment {

    private static final String RECIPE_STEP_DETAIL_KEY = "recipeStepDetail";

    private RecipeStep mRecipeStep;

    @BindView(R.id.tv_step_description)
    TextView mStepDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            mRecipeStep = bundle.getParcelable(RECIPE_STEP_DETAIL_KEY);
            mStepDescription.setText(mRecipeStep.getDescription());
        }

        return rootView;
    }
}
