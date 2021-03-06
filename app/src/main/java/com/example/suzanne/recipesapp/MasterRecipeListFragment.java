package com.example.suzanne.recipesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suzanne.recipesapp.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class MasterRecipeListFragment extends Fragment implements MasterRecipeListAdapter.RecipeClickHandler{

    MasterRecipeListAdapter masterRecipeListAdapter;
    private static final String RECIPES_PARCEL_KEY = "recipes";
    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";
    private static final String LIST_STATE_KEY = "listState";
    ArrayList<Recipe> mRecipes;

    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecyclerView;

    public MasterRecipeListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_recipe_list, container, false);
        ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), 1, false);
        mRecyclerView.setLayoutManager(layoutManager);
        Bundle bundle = this.getArguments();

        if (bundle != null){
            mRecipes = bundle.getParcelableArrayList(RECIPES_PARCEL_KEY);
        } else {
            mRecipes = new ArrayList<Recipe>();
        }
        masterRecipeListAdapter = new MasterRecipeListAdapter(mRecipes, getActivity(), this);
        mRecyclerView.setAdapter(masterRecipeListAdapter);
        return rootView;
    }


    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(getActivity(), RecipeActivity.class);
        intent.putExtra(RECIPE_ACTIVITY_EXTRA_KEY, recipe);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int scrollPosition = mRecyclerView.getVerticalScrollbarPosition();
        outState.putInt(LIST_STATE_KEY, scrollPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(LIST_STATE_KEY)){
            int listPosition = savedInstanceState.getInt(LIST_STATE_KEY);
            mRecyclerView.setVerticalScrollbarPosition(listPosition);
        }
    }
}
