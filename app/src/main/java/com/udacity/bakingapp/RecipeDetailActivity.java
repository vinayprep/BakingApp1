package com.udacity.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.udacity.bakingapp.adapters.RecipeDetailsAdapter;
import com.udacity.bakingapp.pojo.Ingredients;
import com.udacity.bakingapp.pojo.Recipes;
import com.udacity.bakingapp.pojo.Steps;

import static com.udacity.bakingapp.RecipeDetailFragment.RECIPE_INGREDIENTS;
import static com.udacity.bakingapp.RecipeDetailFragment.RECIPE_STEPS;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.ListItemClickListener, RecipeDetailsAdapter.ListItemClickListener {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    Ingredients[] ingredients;
    Steps[] steps;
    Recipes recipes;
    String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ingredients = (Ingredients[]) getIntent().getSerializableExtra("ingredients");
        steps = (Steps[]) getIntent().getSerializableExtra("steps");
        recipes = (Recipes) getIntent().getSerializableExtra("recipes");
        recipeName = getIntent().getStringExtra("recipeName");
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);
        if (savedInstanceState == null) {

            Bundle bundle = getIntent().getExtras();

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_ingredients, recipeDetailFragment).addToBackStack(RECIPE_INGREDIENTS)
                    .commit();

            if (findViewById(R.id.recipe_detail).getTag() != null && findViewById(R.id.recipe_detail).getTag().equals("tablet-land")) {

                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeStepsFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_steps, recipeStepsFragment).addToBackStack(RECIPE_STEPS)
                        .commit();
            }
        } else {
            recipeName = savedInstanceState.getString("recipeName");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                if (findViewById(R.id.recipe_steps) == null) {
                    if (fm.getBackStackEntryCount() > 1) {
                        fm.popBackStack(RECIPE_INGREDIENTS, 0);
                    } else if (fm.getBackStackEntryCount() > 0) {
                        finish();
                    }
                } else {
                    finish();
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putString("recipeName", recipeName);
    }

    @Override
    public void onListItemClick(Steps[] stepsOut, int selectedItemIndex, String recipeName) {

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putSerializable("steps", stepsOut);
        stepBundle.putSerializable("recipes", recipes);
        stepBundle.putInt("index", selectedItemIndex);
        stepBundle.putString("recipeName", recipeName);
        recipeStepsFragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_detail).getTag() != null && findViewById(R.id.recipe_detail).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_steps, recipeStepsFragment).addToBackStack(RECIPE_STEPS)
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_ingredients, recipeStepsFragment).addToBackStack(RECIPE_STEPS)
                    .commit();
        }

    }

}
