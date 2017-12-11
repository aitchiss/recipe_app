package com.example.suzanne.recipesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.suzanne.recipesapp.models.IngredientSpecification;
import com.example.suzanne.recipesapp.models.MeasurementType;
import com.example.suzanne.recipesapp.models.Recipe;
import com.google.gson.Gson;

/**
 * Created by suzanne on 04/12/2017.
 */

public class ListViewWidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String RECIPES_SHARED_PREF = "recipePrefs";
    private static final String RECIPES_FULL_LIST_KEY = "recipesFullList";
    private static final String CURRENT_WIDGET_RECIPE_KEY = "currentRecipe";

    private Context mContext;
    private IngredientSpecification[] mIngredients;

    public ListViewRemoteViewsFactory(Context context){
        mContext = context;


    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
//        Get the last updated list of recipes
        Log.d("widget service", "on data set changed");
        SharedPreferences preferences = mContext.getSharedPreferences(RECIPES_SHARED_PREF, Context.MODE_PRIVATE);
        String recipesJson = preferences.getString(RECIPES_FULL_LIST_KEY, "");
        Gson gson = new Gson();
        Recipe[] recipes = gson.fromJson(recipesJson, Recipe[].class);
        int currentRecipeIndex = preferences.getInt(CURRENT_WIDGET_RECIPE_KEY, 0);
        mIngredients = recipes[currentRecipeIndex].getIngredients();
        Log.d("widget service", String.valueOf(mIngredients.length));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mIngredients == null){
            return 0;
        } else {
            return mIngredients.length;
        }
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getViewAt(int position){
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_list_item);

//        Update the ingredient list
        String ingredientString = "";

//        Get the right quantity string text
        String quantityString;
        boolean quantityIsWholeNumber = doubleIsWholeNumber(mIngredients[position].getQuantity());
        if (quantityIsWholeNumber){
            quantityString = String.valueOf(Integer.valueOf((int) Math.round(mIngredients[position].getQuantity())));
        } else {
            quantityString = String.valueOf(mIngredients[position].getQuantity());
        }

//        Add plural if required
        Boolean addCupPlural = (mIngredients[position].getMeasure() == MeasurementType.CUP && mIngredients[position].getQuantity() > 1);
        String measure = mIngredients[position].getMeasure().toDisplayString().toLowerCase();
        if (addCupPlural){
            measure += "s";
        }
        String ingredientName = mIngredients[position].getIngredient();

        ingredientString += quantityString;
        ingredientString += measure;
        ingredientString += " ";
        ingredientString += ingredientName;
        views.setTextViewText(R.id.tv_widget_ingredient_list_item, ingredientString);
        Log.d("widget service", ingredientString);
        return views;
    }

    private boolean doubleIsWholeNumber(double number){
        if (number % 1 == 0){
            return true;
        } else {
            return false;
        }
    }
}
