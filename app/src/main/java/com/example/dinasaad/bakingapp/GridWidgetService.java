package com.example.dinasaad.bakingapp;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    ArrayList<String> IngredientName,QuantityName;
    Context mContext;
    JSONArray IngredientsArray;
    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
        IngredientName = new ArrayList<String>();
        QuantityName = new ArrayList<String>();

        try {
            SharedPreferences sharedPref =
                    PreferenceManager.getDefaultSharedPreferences(mContext);
            String  IngredientsJson = sharedPref.getString("Widget_data",null);
            if(IngredientsJson != null) {
                IngredientsArray = new JSONArray(IngredientsJson);

                if (IngredientsArray != null) {
                    for (int j = 0; j < IngredientsArray.length(); j++) {
                        JSONObject object = IngredientsArray.getJSONObject(j);
                        String quantity = object.getString("quantity");
                        String measure = object.getString("measure");
                        QuantityName.add("Quantity: " + quantity + " " + measure);
                        String ingredient = object.getString("ingredient");
                        IngredientName.add(ingredient);

                    }
                }
            }else {
                Toast.makeText(mContext, "Select which recipe to display", Toast.LENGTH_LONG).show();
            }
            } catch (JSONException e) {

            e.printStackTrace();
        }



    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

        IngredientName = new ArrayList<String>();
        QuantityName = new ArrayList<String>();

        try {
            SharedPreferences sharedPref =
                    PreferenceManager.getDefaultSharedPreferences(mContext);
            String  IngredientsJson = sharedPref.getString("Widget_data",null);
            if(IngredientsJson != null){
            JSONArray IngredientsArray = new JSONArray(IngredientsJson);

            if (IngredientsArray != null) {
                for (int j = 0; j < IngredientsArray.length(); j++) {
                    JSONObject object = IngredientsArray.getJSONObject(j);
                    String quantity = object.getString("quantity");
                    String measure = object.getString("measure");
                    QuantityName.add("Quantity: " + quantity + " " + measure);
                    String ingredient = object.getString("ingredient");
                    IngredientName.add(ingredient);

                }
            }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(IngredientsArray != null)
        { return IngredientsArray.length();}
        else
        {  //Toast.makeText(mContext, "Select which recipe to display", Toast.LENGTH_LONG).show();
            return 0;}
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {

        if (IngredientsArray == null || IngredientsArray.length() == 0)
            return null;
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ing_list_item);

        views.setTextViewText(R.id.ingredientView, IngredientName.get(position));
        views.setTextViewText(R.id.quantityView, QuantityName.get(position));


        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

