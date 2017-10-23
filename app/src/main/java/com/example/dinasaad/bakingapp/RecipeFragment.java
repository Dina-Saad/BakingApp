package com.example.dinasaad.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dinasaad.bakingapp.Objects.Ingredients;
import com.example.dinasaad.bakingapp.Objects.RecipeListAdaper;
import com.example.dinasaad.bakingapp.Objects.RecipeObject;
import com.example.dinasaad.bakingapp.Objects.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DinaSaad on 14/08/2017.
 */

public class RecipeFragment extends Fragment {
    public static ArrayList<RecipeObject> RecipeList = new ArrayList<RecipeObject>();
    private Toast mToast;
    public RecipeObject clickedRecipeItem;
    private RecipeListAdaper IngredientAdapter;
    ListView listView;
    boolean savedInstanceStateValue = false;

    public RecipeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("SavedRecipeList")) {
            RecipeList = new ArrayList<RecipeObject>();

        } else {
            RecipeList = savedInstanceState.getParcelableArrayList("SavedRecipeList");
            savedInstanceStateValue = true;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("SavedRecipeList", RecipeList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list, container, false);
        listView = (ListView) rootView.findViewById(R.id.IngedientsList);
        if (savedInstanceState == null || !savedInstanceState.containsKey("SavedRecipeList")) {
        } else {
            IngredientAdapter =
                    new RecipeListAdaper(
                            getContext(), RecipeList);
            IngredientAdapter.notifyDataSetChanged();
            listView.setAdapter(IngredientAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mToast != null) {
                    mToast.cancel();
                }

                String toastMessage = "Item #" + RecipeList.get(position).getName() + " clicked.";
                mToast = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_LONG);
                mToast.show();
                clickedRecipeItem = RecipeList.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("clickedRecipeObject", clickedRecipeItem);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });
        return rootView;


    }

    @Override
    public void onStart() {
        super.onStart();
        FetchRecipesTask Task = new FetchRecipesTask();
        Task.execute();
    }

    public class FetchRecipesTask extends AsyncTask<Void, Void, ArrayList<RecipeObject>>
            implements RecyclerAdapter.ListItemClickListener {

        private final String LOG_TAG = FetchRecipesTask.class.getSimpleName();

        private ArrayList<RecipeObject> getRecipesDataFromJson(String RecipeJsonStr)
                throws JSONException {

            String Id, name, servings, image;
            RecipeList = new ArrayList<RecipeObject>();

            JSONArray RecipesArray = new JSONArray(RecipeJsonStr);
            for (int i = 0; i < RecipesArray.length(); i++) {
                // Get the JSON object
                JSONObject RecipeObject = RecipesArray.getJSONObject(i);
                Id = RecipeObject.getString("id");
                name = RecipeObject.getString("name");
                servings = RecipeObject.getString("servings");
                image = RecipeObject.getString("image");

                //RecipeObject NewRecipeObject = new RecipeObject(Id,name,servings,image);
                String IngredientsJson = RecipeObject.getString("ingredients");
                JSONArray IngredientsArray = new JSONArray(IngredientsJson);
                String stepsJson = RecipeObject.getString("steps");
                JSONArray stepsArray = new JSONArray(stepsJson);
                // ingredient data
                ArrayList<Ingredients> IngredientsArraylistdata = new ArrayList<Ingredients>();

                if (IngredientsArray != null) {
                    for (int j = 0; j < IngredientsArray.length(); j++) {
                        JSONObject object = IngredientsArray.getJSONObject(j);
                        String quantity = object.getString("quantity");
                        String measure = object.getString("measure");
                        String ingredient = object.getString("ingredient");

                        Ingredients ingredientsObject = new Ingredients(quantity, measure, ingredient);
                        IngredientsArraylistdata.add(ingredientsObject);
                    }
                }
                // NewRecipeObject.setIngredientsList(IngredientsArraylistdata);
                //steps data
                ArrayList<Steps> StepsArraylistdata = new ArrayList<Steps>();

                if (stepsArray != null) {
                    for (int j = 0; j < stepsArray.length(); j++) {
                        JSONObject object = stepsArray.getJSONObject(j);
                        String id = object.getString("id");
                        String shortDescription = object.getString("shortDescription");
                        String description = object.getString("description");
                        String videoURL = object.getString("videoURL");
                        String thumbnailURL = object.getString("thumbnailURL");

                        Steps StepsObject = new Steps(id, shortDescription, description, videoURL, thumbnailURL);
                        StepsArraylistdata.add(StepsObject);
                    }
                }
                RecipeObject NewRecipeObject = new RecipeObject(Id, name, servings, image, IngredientsArraylistdata, StepsArraylistdata, IngredientsJson);
                // NewRecipeObject.setStepsList(StepsArraylistdata);
                RecipeList.add(NewRecipeObject);
            }
            return RecipeList;
        }

        @Override
        protected ArrayList<RecipeObject> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String RecipeJsonStr = null;
            try {
                String apiKey = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
                URL url = new URL(apiKey);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                RecipeJsonStr = buffer.toString();
            } catch (IOException e) {

                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getRecipesDataFromJson(RecipeJsonStr);
            } catch (JSONException e) {

                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RecipeObject> result) {
            if (result == null) {
                Toast.makeText(getContext(), "An error occurred, Please try again later.", Toast.LENGTH_LONG).show();
            } else {
                if (savedInstanceStateValue == false) {
                    IngredientAdapter =
                            new RecipeListAdaper(
                                    getContext(), result);
                    listView.setAdapter(IngredientAdapter);
                }
            }

        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            if (mToast != null) {
                mToast.cancel();
            }
            String toastMessage = "Item #" + RecipeList.get(clickedItemIndex).getName() + " clicked.";
            mToast = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_LONG);
            mToast.show();
            clickedRecipeItem = RecipeList.get(clickedItemIndex);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable("clickedRecipeObject", clickedRecipeItem);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}


