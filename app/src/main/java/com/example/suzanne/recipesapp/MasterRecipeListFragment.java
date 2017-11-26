package com.example.suzanne.recipesapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suzanne.recipesapp.models.Recipe;

import java.util.ArrayList;

/**
 * Created by suzanne on 26/11/2017.
 */

public class MasterRecipeListFragment extends Fragment {

    MasterRecipeListAdapter masterRecipeListAdapter;
    ArrayList<Recipe> mRecipes;

    public MasterRecipeListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_recipe_list, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recipes_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), 1, false);
        recyclerView.setLayoutManager(layoutManager);
        Bundle bundle = this.getArguments();
        ArrayList<Recipe> recipes;

        if (bundle != null){


            recipes = bundle.getParcelableArrayList("recipes");


        } else {
            recipes = new ArrayList<Recipe>();
        }

        masterRecipeListAdapter = new MasterRecipeListAdapter(recipes);

        recyclerView.setAdapter(masterRecipeListAdapter);

        return rootView;

    }
}
