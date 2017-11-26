package com.example.suzanne.recipesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suzanne on 26/11/2017.
 */

public class IngredientSpecification implements Parcelable {


    private double quantity;
    private MeasurementType measure;
    private String ingredient;

    public IngredientSpecification(double quantity, MeasurementType measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public IngredientSpecification(Parcel parcel){
        this.quantity = parcel.readDouble();
        this.measure = MeasurementType.getType(parcel.readString());
        this.ingredient = parcel.readString();
    }

    public double getQuantity() {
        return quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public MeasurementType getMeasure() {
        return measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.quantity);
        parcel.writeString(this.measure.toString());
        parcel.writeString(this.ingredient);
    }

    public static final Parcelable.Creator<IngredientSpecification> CREATOR = new Parcelable.Creator<IngredientSpecification>(){
        @Override
        public IngredientSpecification createFromParcel(Parcel parcel) {
            return new IngredientSpecification(parcel);
        }

        public IngredientSpecification[] newArray(int size) { return new IngredientSpecification[size]; }
    };
}
