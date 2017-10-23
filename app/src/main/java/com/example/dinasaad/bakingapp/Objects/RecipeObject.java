package com.example.dinasaad.bakingapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DinaSaad on 14/08/2017.
 */

public class RecipeObject  implements Parcelable {
    String Id;
    String Name;
    String Servings;
    String Image;
    ArrayList<Ingredients> IngredientsList=new ArrayList<Ingredients>();
    ArrayList<Steps> StepsList=new ArrayList<Steps>();
    String IngredientJsonString;
    public RecipeObject(){

    }
    //constructor
    public RecipeObject(  String Id,
            String Name,
            String Servings,
            String Image,ArrayList<Ingredients> IngredientsList,  ArrayList<Steps> StepsList, String IngredientJsonString)
    {
        this.Id=Id;
        this.Name=Name;
        this.Servings=Servings;
        this.Image=Image;
        this.IngredientsList=IngredientsList;
        this.StepsList = StepsList;
        this.IngredientJsonString = IngredientJsonString;
    }
    public RecipeObject(Parcel in)
    {
        String[] data = new String[5];
        in.readStringArray(data);

        this.Id=data[0];
        this.Name=data[1];
        this.Servings=data[2];
        this.Image=data[3];
        this.IngredientJsonString=data[4];
        in.readTypedList(IngredientsList,Ingredients.CREATOR);
        in.readTypedList(StepsList,Steps.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.Id,
                this.Name,
                this.Servings,
                this.Image,
        this.IngredientJsonString});
        dest.writeTypedList(IngredientsList);
        dest.writeTypedList(StepsList);
    }
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        @Override
        public RecipeObject createFromParcel(Parcel in) {
            return new RecipeObject(in);
        }
        @Override
        public RecipeObject[] newArray(int size) {
            return new RecipeObject [size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getServings() {
        return Servings;
    }

    public void setServings(String servings) {
        Servings = servings;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public ArrayList<Ingredients> getIngredientsList() {
        return IngredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredients> ingredientsList) {
        IngredientsList = ingredientsList;
    }

    public ArrayList<Steps> getStepsList() {
        return StepsList;
    }

    public void setStepsList(ArrayList<Steps> stepsList) {
        StepsList = stepsList;
    }

    public String getIngredientJsonString() {
        return IngredientJsonString;
    }

    public void setIngredientJsonString(String ingredientJsonString) {
        IngredientJsonString = ingredientJsonString;
    }
}
