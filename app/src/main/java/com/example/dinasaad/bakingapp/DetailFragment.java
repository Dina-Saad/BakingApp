package com.example.dinasaad.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dinasaad.bakingapp.Objects.RecipeObject;

import java.util.ArrayList;

/**
 * Created by DinaSaad on 18/08/2017.
 */

public class DetailFragment extends Fragment
        implements RecyclerAdapter.ListItemClickListener {
    RecipeObject Recipe = new RecipeObject();
    ArrayList<String> Details = new ArrayList<String>();
    private RecyclerAdapter mAdapter;
    private RecyclerView Recyclerview;
    private Toast mToast;
    public DetailFragment() {
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        if (mToast != null) {
            mToast.cancel();
        }
        if (Utilities.Istablet == true) {
            Bundle extras = new Bundle();

            if (clickedItemIndex == 0) {
                Fragment newFragment = new IngredientsFragment();
                extras.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
                newFragment.setArguments(extras);
                // Replace the old head fragment with a new one
                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                t.replace(R.id.head_container, newFragment);
                t.commit();
            } else {
                Fragment newFragment = new StepsFragment();
                extras.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
                extras.putString("index", String.valueOf(clickedItemIndex - 1));
                newFragment.setArguments(extras);
                // Replace the old head fragment with a new one
                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                t.replace(R.id.head_container, newFragment);
                t.commit();

            }


            // This LinearLayout will only initially exist in the two-pane tablet case


        } else {
            if (clickedItemIndex == 0) {
                Intent intent = new Intent(getActivity(), IngredientsActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
                intent.putExtras(extras);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), StepsDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
                extras.putString("index", String.valueOf(clickedItemIndex - 1));

                intent.putExtras(extras);
                startActivity(intent);
            }


        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Recyclerview = (RecyclerView) rootView.findViewById(R.id.Recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        Recyclerview.setLayoutManager(layoutManager);
        Recipe = new RecipeObject();
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();


        if (extras != null) {
            Recipe = extras.getParcelable("clickedRecipeObject");
        }

        if (Recipe.getIngredientsList().size() != 0) {
            Details.add("Ingredients");
        }
        if (Recipe.getStepsList().size() != 0) {
            for (int i = 0; i < Recipe.getStepsList().size(); i++) {
                Details.add(Recipe.getStepsList().get(i).getShortDescription());
            }
        }
       /* if (Utilities.Istablet == true) {
            Fragment newFragment = new IngredientsFragment();
            extras.putParcelable("clickedRecipeObject", (Parcelable) Recipe);
            newFragment.setArguments(extras);
            // Replace the old head fragment with a new one
            FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
            t.replace(R.id.head_container, newFragment);
            t.commit();
        }*/
        mAdapter = new RecyclerAdapter(Details, this, 1);
        Recyclerview.setAdapter(mAdapter);


        return rootView;

    }

}
