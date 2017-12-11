package com.example.suzanne.recipesapp;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.suzanne.recipesapp.IdlingResource.RecipeIdlingResource;
import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.utilities.RecipeJsonUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private static final int RECIPES_LOADER_ID = 10;
    private static final String RECIPES_PARCEL_KEY = "recipes";
    private static final String RECIPES_SHARED_PREF_KEY = "recipeList";
    private static final String RECIPES_SHARED_PREF = "recipePrefs";
    private static final String RECIPES_FULL_LIST_KEY = "recipesFullList";
    private ArrayList<Recipe> mRecipes;
    private RecipeIdlingResource mIdlingResource;



    @BindView(R.id.layout_main_activity_no_network)
    LinearLayout mNoNetworkView;

    @BindView(R.id.master_recipe_list_fragment)
    FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mIdlingResource = new RecipeIdlingResource();
        mIdlingResource.setIdleState(false);


        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_SHARED_PREF_KEY)){
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES_SHARED_PREF_KEY);
            mFragmentContainer.setVisibility(View.VISIBLE);
            mNoNetworkView.setVisibility(View.INVISIBLE);
            showRecipeMasterFragment();
            mIdlingResource.setIdleState(true);
            Log.d("set idle state", "true");
        } else {
            loadRecipes();
        }
    }

    @VisibleForTesting
    @NonNull
    public RecipeIdlingResource getRecipeIdlingResource(){
        if (mIdlingResource == null){
            mIdlingResource = new RecipeIdlingResource();
        }
        return mIdlingResource;
    }

    public void loadRecipes(){
        if (NetworkUtils.isOnlineOrConnecting(this)){
            Log.d("mainactivit", "loading recipes");
            mFragmentContainer.setVisibility(View.VISIBLE);
            mNoNetworkView.setVisibility(View.INVISIBLE);
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader loader = loaderManager.getLoader(RECIPES_LOADER_ID);
            if (loader == null){
                loaderManager.initLoader(RECIPES_LOADER_ID, null, this);
            } else {
                loaderManager.restartLoader(RECIPES_LOADER_ID, null, this);
            }
        } else {
            mFragmentContainer.setVisibility(View.GONE);
            mNoNetworkView.setVisibility(View.VISIBLE);
        }
    }

    public void showRecipeMasterFragment(){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RECIPES_PARCEL_KEY, mRecipes);

        MasterRecipeListFragment masterRecipeListFragment = new MasterRecipeListFragment();
        masterRecipeListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.master_recipe_list_fragment, masterRecipeListFragment)
                .commitAllowingStateLoss();
    }

    private void storeRecipesInSharedPrefs(){
//        Store recipes on each network call, to be used in app widget
        Gson gson = new Gson();
        String recipes = gson.toJson(mRecipes);
        SharedPreferences sharedPreferences = getSharedPreferences(RECIPES_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RECIPES_FULL_LIST_KEY, recipes);
        editor.commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipes != null){
            outState.putParcelableArrayList(RECIPES_SHARED_PREF_KEY, mRecipes);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {

            @Override
            protected void onStartLoading() {
                Log.d("main activity", "started loading");
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                URL url = NetworkUtils.buildUrl();
                ArrayList<Recipe> recipeData;
                try {
                    String results = NetworkUtils.getResponseFromHttpUrl(url);
                    recipeData = RecipeJsonUtils.convertJsonToRecipes(results);
                    Log.d("main activty", "recipe data recvd");
                } catch (Exception e){
                    e.printStackTrace();
                    recipeData = null;
                }
                return recipeData;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        if (data != null){
            Log.d("main activty", "on load finished");
            mRecipes = data;
            showRecipeMasterFragment();
            storeRecipesInSharedPrefs();
            mIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }
}
