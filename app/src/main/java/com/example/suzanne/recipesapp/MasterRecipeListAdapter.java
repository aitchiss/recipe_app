package com.example.suzanne.recipesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.Recipe;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class MasterRecipeListAdapter extends RecyclerView.Adapter<MasterRecipeListAdapter.MasterRecipeListAdapterViewHolder> {

    private ArrayList<Recipe> mRecipeData;

    public MasterRecipeListAdapter(ArrayList<Recipe> recipes){
        mRecipeData = recipes;
    }

    @Override
    public MasterRecipeListAdapter.MasterRecipeListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.master_recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new MasterRecipeListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MasterRecipeListAdapter.MasterRecipeListAdapterViewHolder holder, int position) {
        holder.textView.setText(mRecipeData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipeData == null){
            return 0;
        } else {
            return mRecipeData.size();
        }
    }

    public void setRecipeData(ArrayList<Recipe> recipes){
        mRecipeData = recipes;
        notifyDataSetChanged();
    }

    class MasterRecipeListAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MasterRecipeListAdapterViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.tv_recipe_name) ;
            ButterKnife.bind(this, itemView);
        }
    }
}

