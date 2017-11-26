package com.example.suzanne.recipesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suzanne on 26/11/2017.
 */

public class Recipe implements Parcelable {


    private int id;
    private String name;
    private RecipeStep[] steps;
    private IngredientSpecification[] ingredients;
    private int servings;
    private String image;

    public Recipe(int id, String name){
        this.id = id;
        this.name = name;
    }

    private Recipe(Parcel parcel){
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.steps = parcel.createTypedArray(RecipeStep.CREATOR);
        this.ingredients = parcel.createTypedArray(IngredientSpecification.CREATOR);
        this.servings = parcel.readInt();
        this.image = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecipeStep[] getSteps() {
        return steps;
    }

    public void setSteps(RecipeStep[] steps) {
        this.steps = steps;
    }

    public IngredientSpecification[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(IngredientSpecification[] ingredients) {
        this.ingredients = ingredients;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeTypedArray(this.steps, 0);
        parcel.writeTypedArray(this.ingredients, 0);
        parcel.writeInt(this.servings);
        parcel.writeString(this.image);
    }

    public final static Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        public Recipe[] newArray(int size){
            return new Recipe[0];
        }
    };
}
