package com.example.suzanne.recipesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suzanne.recipesapp.models.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 26/11/2017.
 */

public class StepDescriptionAdapter extends RecyclerView.Adapter<StepDescriptionAdapter.StepDescriptionAdapterViewHolder> {

    private RecipeStep[] mSteps;
    private final MasterRecipeDetailFragment.OnRecipeStepClickListener mClickHandler;

    public StepDescriptionAdapter(RecipeStep[] steps, MasterRecipeDetailFragment.OnRecipeStepClickListener clickListener){
        mSteps = steps;
        mClickHandler = clickListener;
    }

    @Override
    public StepDescriptionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.step_desc_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new StepDescriptionAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepDescriptionAdapterViewHolder holder, int position) {
        if (position == 0){
            holder.stepDescription.setText(mSteps[position].getShortDescription());
        } else {
            holder.stepDescription.setText(String.valueOf(position) + ": " + mSteps[position].getShortDescription());
        }

    }

    @Override
    public int getItemCount() {
        if (mSteps == null){
            return 0;
        } else {
            return mSteps.length;
        }
    }

    class StepDescriptionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_step_short_description)
        TextView stepDescription;

        public StepDescriptionAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onRecipeStepClick(getAdapterPosition());
        }
    }
}
