package com.example.suzanne.recipesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.suzanne.recipesapp.models.Recipe;
import com.google.gson.Gson;

/**
 * Created by suzanne on 04/12/2017.
 */

public class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String RECIPES_SHARED_PREF = "recipePrefs";
    private static final String RECIPES_FULL_LIST_KEY = "recipesFullList";
    private static final String CURRENT_WIDGET_RECIPE_KEY = "currentRecipe";

    private Context mContext;
    private Recipe mRecipe;

    public ListViewRemoteViewsFactory(Context context){
        mContext = context;

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
//        Get the last updated list of recipes
        SharedPreferences preferences = mContext.getSharedPreferences(RECIPES_SHARED_PREF, Context.MODE_PRIVATE);
        String recipesJson = preferences.getString(RECIPES_FULL_LIST_KEY, "");
        Gson gson = new Gson();
        Recipe[] recipes = gson.fromJson(recipesJson, Recipe[].class);
        int currentRecipeIndex = preferences.getInt(CURRENT_WIDGET_RECIPE_KEY, 0);
        mRecipe = recipes[currentRecipeIndex];
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mRecipe == null){
            return 0;
        } else {
            return mRecipe.getIngredients().length;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
