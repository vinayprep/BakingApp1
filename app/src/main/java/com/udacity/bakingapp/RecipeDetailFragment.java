package com.udacity.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.adapters.RecipeDetailsAdapter;
import com.udacity.bakingapp.pojo.Ingredients;
import com.udacity.bakingapp.pojo.Recipes;
import com.udacity.bakingapp.pojo.Steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecipeDetailFragment extends Fragment {

    private static final String RECIPE = "RECIPE";
    private static final String RECIPE_INGREDIENTS = "RECIPE_INGREDIENTS";
    private static final String RECIPE_STEPS = "RECIPE_STEPS";
    private static final String RECIPE_NAME = "RECIPE_NAME";
    String recipeName;
    Ingredients[] ingredients;
    Steps[] steps;
    Recipes recipe;
    private List<Steps> mModels;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;
        TextView textView;


        if (savedInstanceState != null) {
            recipe = (Recipes) savedInstanceState.getSerializable(RECIPE);
            ingredients = (Ingredients[]) savedInstanceState.getSerializable(RECIPE_INGREDIENTS);
            steps = (Steps[]) savedInstanceState.getSerializable(RECIPE_STEPS);
            recipeName = savedInstanceState.getString(RECIPE_NAME);
        } else {
            recipe = (Recipes) getArguments().getSerializable("recipes");
            ingredients = (Ingredients[]) getArguments().getSerializable("ingredients");
            steps = (Steps[]) getArguments().getSerializable("steps");
            recipeName = getArguments().getString("recipeName");
        }


        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        textView = rootView.findViewById(R.id.recipe_detail_text);

        ArrayList<String> recipeIngredients = new ArrayList<>();

        for (int i = 0; i < ingredients.length; i++) {
            textView.append("\u2022 " + ingredients[i].getIngredient() + "\n");
            textView.append("\t\t\t Quantity: " + ingredients[i].getQuantity() + "\n");
            textView.append("\t\t\t Measure: " + ingredients[i].getMeasure() + "\n\n");

            recipeIngredients.add(ingredients[i].getIngredient() + "\n" +
                    "Quantity: " + ingredients[i].getQuantity() + "\n" +
                    "Measure: " + ingredients[i].getMeasure() + "\n");
        }

        recyclerView = rootView.findViewById(R.id.recipe_detail_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        RecipeDetailsAdapter mRecipeDetailsAdapter = new RecipeDetailsAdapter((RecipeDetailActivity) getActivity());
        recyclerView.setAdapter(mRecipeDetailsAdapter);
        mModels = new ArrayList<>();
        mModels.clear();
        mModels.addAll(Arrays.asList(steps));
        mRecipeDetailsAdapter.replaceAll(mModels, recipeName);
        recyclerView.scrollToPosition(0);
//        mRecipeDetailsAdapter.setMasterRecipeData(recipe,getContext());
//
//        //update widget
//        UpdateBakingService.startBakingService(getContext(),recipeIngredientsForWidgets);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putSerializable(RECIPE, recipe);
        currentState.putSerializable(RECIPE_INGREDIENTS, ingredients);
        currentState.putSerializable(RECIPE_STEPS, steps);
        currentState.putString(RECIPE_NAME, recipeName);
    }

}
