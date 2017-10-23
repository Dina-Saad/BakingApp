package com.example.dinasaad.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dinasaad.bakingapp.Objects.Ingredients;
import com.example.dinasaad.bakingapp.Objects.RecipeObject;

import java.util.ArrayList;

/**
 * Created by DinaSaad on 18/08/2017.
 */

public class IngredientsFragment extends Fragment {

    private ListAdapter IngredientAdapter;
    public ArrayList<Ingredients> IngredientsList = new ArrayList<Ingredients>();

    public IngredientsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("SavedIngredientsList")) {
             IngredientsList = new ArrayList<Ingredients>();
        }
        else {
            IngredientsList = savedInstanceState.getParcelableArrayList("SavedIngredientsList");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("SavedIngredientsList", IngredientsList);
        super.onSaveInstanceState(outState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecipeObject Recipe = new RecipeObject();
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        if (extras == null) {
            return null;
        }
        else if (extras != null) {
            Recipe =  extras.getParcelable("clickedRecipeObject");
            if(savedInstanceState == null || !savedInstanceState.containsKey("SavedIngredientsList")) {
                IngredientsList = Recipe.getIngredientsList();
            }
        }
        View rootView = inflater.inflate(R.layout.list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.IngedientsList);
        IngredientAdapter =
                new ListAdapter(
                        getContext(),IngredientsList);
        listView.setAdapter(IngredientAdapter);
        //widget data
        String value=Recipe.getIngredientJsonString();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Widget_data",value);
        editor.apply();
        return rootView;
    }

}
