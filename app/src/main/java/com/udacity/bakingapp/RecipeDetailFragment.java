package com.udacity.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.udacity.bakingapp.adapters.RecipeDetailsAdapter;
import com.udacity.bakingapp.pojo.Ingredients;
import com.udacity.bakingapp.pojo.Recipes;
import com.udacity.bakingapp.pojo.Steps;
import com.udacity.bakingapp.widget.UpdateBakingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecipeDetailFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String RECIPE = "RECIPE";
    private static final String RECIPE_INGREDIENTS = "RECIPE_INGREDIENTS";
    private static final String RECIPE_STEPS = "RECIPE_STEPS";
    private static final String RECIPE_NAME = "RECIPE_NAME";
    private static final String SCROLL_POSITION = "scroll";
    String recipeName;
    Ingredients[] ingredients;
    Steps[] steps;
    Recipes recipe;
    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    int scrollPos = 0;
    SharedPreferences sharedpreferences;
    ScrollView mScrollView;
    private List<Steps> mModels;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        mScrollView = rootView.findViewById(R.id.ingredients_scroll_view);

        sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

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
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        RecipeDetailsAdapter mRecipeDetailsAdapter = new RecipeDetailsAdapter((RecipeDetailActivity) getActivity());
        recyclerView.setAdapter(mRecipeDetailsAdapter);
        mModels = new ArrayList<>();
        mModels.clear();
        mModels.addAll(Arrays.asList(steps));
        mRecipeDetailsAdapter.replaceAll(mModels, recipeName);
        recyclerView.scrollToPosition(0);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SCROLL_POSITION)) {
                scrollPos = Integer.parseInt(savedInstanceState.getString(SCROLL_POSITION));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getLayoutManager().scrollToPosition(scrollPos);
                    }
                }, 200);
            }
            final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if (position != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);
                    }
                }, 200);
            }
        } else {
            scrollPos = Integer.parseInt(sharedpreferences.getString("scrollPostion", "0"));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.getLayoutManager().scrollToPosition(scrollPos);
                }
            }, 200);
        }
//        //update widget
        UpdateBakingService.startBakingService(getContext(), recipeIngredients);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putSerializable(RECIPE, recipe);
        currentState.putSerializable(RECIPE_INGREDIENTS, ingredients);
        currentState.putSerializable(RECIPE_STEPS, steps);
        currentState.putString(RECIPE_NAME, recipeName);
        int into = mLayoutManager.findFirstVisibleItemPosition();
        currentState.putString(SCROLL_POSITION, String.valueOf(into));
        currentState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        int into = mLayoutManager.findFirstVisibleItemPosition();
        editor.putString("scrollPostion", String.valueOf(into));
        editor.apply();
    }
}
