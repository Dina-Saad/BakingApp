package com.example.dinasaad.bakingapp;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DinaSaad on 27/08/2017.
 */

public class BakingWidgetService extends IntentService {

    public static final String ACTION_WATER_PLANTS = "com.example.android.bakingapp.action.water_plants";
    public static final String ACTION_UPDATE_PLANT_WIDGETS = "com.example.android.bakingapp.action.update_plant_widgets";


    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    /**
     * Starts this service to perform WaterPlants action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionWaterPlants(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_WATER_PLANTS);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WATER_PLANTS.equals(action)) {
                handleActionWaterPlants();
            } else if (ACTION_UPDATE_PLANT_WIDGETS.equals(action)) {
                handleActionUpdatePlantWidgets();
            }
        }
    }
    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_UPDATE_PLANT_WIDGETS);
        context.startService(intent);
    }
    /**
     * Handle action WaterPlant in the provided background thread with the provided
     * parameters.
     */
    private void handleActionWaterPlants() {
        ArrayList<String> IngredientName,QuantityName;
        JSONArray IngredientsArray;
        IngredientName = new ArrayList<String>();
        QuantityName = new ArrayList<String>();

        try {
            SharedPreferences sharedPref =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String IngredientsJson = sharedPref.getString("Widget_data", null);
            if (IngredientsJson != null) {
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
            }
            } catch(JSONException e){

                e.printStackTrace();
            }



    }
    /**
     * Handle action UpdatePlantWidgets in the provided background thread
     */
    private void handleActionUpdatePlantWidgets() {
        ArrayList<String> IngredientName,QuantityName;
        JSONArray IngredientsArray;
        IngredientName = new ArrayList<String>();
        QuantityName = new ArrayList<String>();

        try {
            SharedPreferences sharedPref =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.WidgetIngedientsList);
        //Now update all widgets
        BakingWidgetProvider.updateWidgets(this,appWidgetManager,appWidgetIds);
    }
}
