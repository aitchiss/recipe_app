package com.example.suzanne.recipesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class MasterRecipeListAdapter extends RecyclerView.Adapter<MasterRecipeListAdapter.MasterRecipeListAdapterViewHolder> {

    private ArrayList<Recipe> mRecipeData;
    private Context mContext;

    public MasterRecipeListAdapter(ArrayList<Recipe> recipes, Context context){
        mRecipeData = recipes;
        mContext = context;
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
        holder.recipeName.setText(mRecipeData.get(position).getName());
        holder.servingNumber.setText(String.valueOf(mRecipeData.get(position).getServings()));

        String imageLocation = mRecipeData.get(position).getImage();
        
        if (imageLocation != null && !imageLocation.isEmpty()){
            Picasso.with(holder.recipeImage.getContext())
                    .load(imageLocation)
                    .placeholder(R.drawable.ic_cake_black)
                    .error(R.drawable.ic_cake_black)
                    .into(holder.recipeImage);
        } else {
            holder.recipeImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_cake_black));
        }

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

        @BindView(R.id.tv_recipe_name) TextView recipeName;
        @BindView(R.id.tv_servings_number) TextView servingNumber;
        @BindView(R.id.iv_recipe_image) ImageView recipeImage;

        public MasterRecipeListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

