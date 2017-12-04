package com.example.suzanne.recipesapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.IngredientSpecification;
import com.example.suzanne.recipesapp.models.MeasurementType;
import com.example.suzanne.recipesapp.models.Recipe;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String RECIPES_SHARED_PREF = "recipePrefs";
    private static final String RECIPES_FULL_LIST_KEY = "recipesFullList";
    private static final String CURRENT_WIDGET_RECIPE_KEY = "currentRecipe";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

//        Get the latest recipes list from shared prefs, and set the content to the first recipe
        SharedPreferences preferences = context.getSharedPreferences(RECIPES_SHARED_PREF, Context.MODE_PRIVATE);
        String recipesJson = preferences.getString(RECIPES_FULL_LIST_KEY, "");
        Gson gson = new Gson();
        Recipe[] recipes = gson.fromJson(recipesJson, Recipe[].class);


        views.setTextViewText(R.id.tv_widget_recipe_title, recipes[0].getName());
//        Update the listview
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_ingredients);


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.tv_widget_recipe_title, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

