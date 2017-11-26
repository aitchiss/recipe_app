package com.example.suzanne.recipesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suzanne on 26/11/2017.
 */

public class IngredientSpecification implements Parcelable {


    private int quantity;
    private MeasurementType measure;
    private String ingredient;

    public IngredientSpecification(int quantity, MeasurementType measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public IngredientSpecification(Parcel parcel){
        this.quantity = parcel.readInt();
        this.measure = MeasurementType.valueOf(parcel.readString());
        this.ingredient = parcel.readString();
    }

    public int getQuantity() {
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
        parcel.writeInt(this.quantity);
        parcel.writeString(this.measure.toString());
        parcel.writeString(this.ingredient);
    }

    public static final Parcelable.Creator<IngredientSpecification> CREATOR = new Parcelable.Creator<IngredientSpecification>(){
        @Override
        public IngredientSpecification createFromParcel(Parcel parcel) {
            return new IngredientSpecification(parcel);
        }

        public IngredientSpecification[] newArray(int size) { return new IngredientSpecification[0]; }
    };
}
