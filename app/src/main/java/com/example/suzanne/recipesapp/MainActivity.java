package com.example.suzanne.recipesapp;

import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.utilities.RecipeJsonUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private static final int RECIPES_LOADER_ID = 10;
    private static final String RECIPES_PARCEL_KEY = "recipes";
    private static final String RECIPES_SHARED_PREF_KEY = "recipeList";
    private ArrayList<Recipe> mRecipes;

    @BindView(R.id.layout_main_activity_no_network)
    LinearLayout mNoNetworkView;

    @BindView(R.id.master_recipe_list_fragment)
    FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_SHARED_PREF_KEY)){
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES_SHARED_PREF_KEY);
            mFragmentContainer.setVisibility(View.VISIBLE);
            mNoNetworkView.setVisibility(View.INVISIBLE);
            showRecipeMasterFragment();
        } else {
            loadRecipes();
        }
    }

    public void loadRecipes(){
        if (NetworkUtils.isOnlineOrConnecting(this)){
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
            mRecipes = data;
            showRecipeMasterFragment();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }
}
