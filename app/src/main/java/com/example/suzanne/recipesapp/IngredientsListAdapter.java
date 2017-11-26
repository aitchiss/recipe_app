package com.example.suzanne.recipesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.IngredientSpecification;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsListAdapterViewHolder> {

    private IngredientSpecification[] mIngredients;

    public IngredientsListAdapter(IngredientSpecification[] ingredients){
        mIngredients = ingredients;
    }

    @Override
    public IngredientsListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.ingredients_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new IngredientsListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsListAdapterViewHolder holder, int position) {
        String quantityString = String.valueOf(mIngredients[position].getQuantity());
        String measure = mIngredients[position].getMeasure().toDisplayString().toLowerCase();
        String ingredientName = mIngredients[position].getIngredient();

        holder.ingredientDescription.setText(quantityString + measure + " " + ingredientName);
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null){
            return 0;
        } else {
            return mIngredients.length;
        }
    }

    public void setIngredientsData(IngredientSpecification[] newIngredients){
        mIngredients = newIngredients;
        notifyDataSetChanged();
    }

    class IngredientsListAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient_list_description)
        TextView ingredientDescription;

        public IngredientsListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
