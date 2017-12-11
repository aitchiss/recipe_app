package com.example.suzanne.recipesapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
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
    private static final String ON_NEXT_CLICK_TAG = "nextClick";
    private static final String ON_BACK_CLICK_TAG = "backClick";
    private static final String RECIPE_ACTIVITY_EXTRA_KEY = "recipe";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        Intent listViewIntent = new Intent(context, ListViewWidgetService.class);
        views.setRemoteAdapter(R.id.lv_widget_ingredients, listViewIntent);
        views.setOnClickPendingIntent(R.id.widget_next_arrow,
                getPendingSelfIntent(context, ON_NEXT_CLICK_TAG));
        views.setOnClickPendingIntent(R.id.widget_back_button,
                getPendingSelfIntent(context, ON_BACK_CLICK_TAG));


//        Get the latest recipes list from shared prefs, and set the title accordingly
        Recipe[] recipes = getSharedPreferencesRecipes(context);
        int currentRecipeIndex = getCurrentRecipeIndex(context);

//        Hide arrows if no back/forward option
        if (recipes.length - 1 == currentRecipeIndex){
            views.setViewVisibility(R.id.widget_next_arrow, View.INVISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_next_arrow, View.VISIBLE);
        }

        if (currentRecipeIndex == 0){
            views.setViewVisibility(R.id.widget_back_button, View.INVISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_back_button, View.VISIBLE);
        }

        views.setTextViewText(R.id.tv_widget_recipe_title, recipes[currentRecipeIndex].getName());
//        Update the listview
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_ingredients);


        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(RECIPE_ACTIVITY_EXTRA_KEY, recipes[currentRecipeIndex]);
        Log.d("widget provider", recipes[currentRecipeIndex].getName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    protected static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, RecipeWidgetProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        SharedPreferences preferences = context.getSharedPreferences(RECIPES_SHARED_PREF, Context.MODE_PRIVATE);
        int currentIndex = preferences.getInt(CURRENT_WIDGET_RECIPE_KEY, 0);

        SharedPreferences.Editor editor = preferences.edit();

        if(ON_NEXT_CLICK_TAG.equals(intent.getAction())){
            editor.putInt(CURRENT_WIDGET_RECIPE_KEY, (currentIndex + 1));
            editor.commit();

        } else if (ON_BACK_CLICK_TAG.equals(intent.getAction())){
            editor.putInt(CURRENT_WIDGET_RECIPE_KEY, (currentIndex - 1));
            editor.commit();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), RecipeWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static Recipe[] getSharedPreferencesRecipes(Context context){
        SharedPreferences preferences = context.getSharedPreferences(RECIPES_SHARED_PREF, Context.MODE_PRIVATE);
        String recipesJson = preferences.getString(RECIPES_FULL_LIST_KEY, "");
        Gson gson = new Gson();
        return gson.fromJson(recipesJson, Recipe[].class);
    }

    private static int getCurrentRecipeIndex(Context context){
        SharedPreferences preferences = context.getSharedPreferences(RECIPES_SHARED_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(CURRENT_WIDGET_RECIPE_KEY, 0);
    }

}

