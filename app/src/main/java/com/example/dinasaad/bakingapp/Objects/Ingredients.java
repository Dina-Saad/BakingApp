package com.example.dinasaad.bakingapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DinaSaad on 14/08/2017.
 */

public class Ingredients implements Parcelable {
    String Quantity;
    String Measure;
    String Ingredient;


    Ingredients(){

    }

    //constructor
    public Ingredients( String Quantity,
                          String Measure,
                          String Ingredient)
    {
        this.Quantity=Quantity;
        this.Ingredient=Ingredient;
        this.Measure=Measure;
    }
    public Ingredients(Parcel in)
    {
        String[] data = new String[3];
        in.readStringArray(data);

        this.Quantity=data[0];
        this.Measure=data[1];
        this.Ingredient=data[2];
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.Quantity,
                this.Measure,
                this.Ingredient});
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }
        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients [size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }
}
