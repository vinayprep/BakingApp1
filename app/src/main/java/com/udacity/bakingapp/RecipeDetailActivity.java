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
        if (savedInstanceState == null) {

            Bundle selectedRecipeBundle = getIntent().getExtras();

            final RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(selectedRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack("STACK_RECIPE_DETAIL")
                    .commit();

            if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {

                final RecipeStepsFragment fragment2 = new RecipeStepsFragment();
                fragment2.setArguments(selectedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, fragment2).addToBackStack("STACK_RECIPE_STEP_DETAIL")
                        .commit();

            }


        } else {
            recipeName = savedInstanceState.getString("recipeName");
        }


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                if (findViewById(R.id.fragment_container2) == null) {
                    if (fm.getBackStackEntryCount() > 1) {
                        //go back to "Recipe Detail" screen
                        fm.popBackStack("STACK_RECIPE_DETAIL", 0);
                    } else if (fm.getBackStackEntryCount() > 0) {
                        //go back to "Recipe" screen
                        finish();

                    }


                } else {
                    //go back to "Recipe" screen
                    finish();
                }
            }
        });
    }

    @Override
    public void onListItemClick(Steps[] stepsOut, int selectedItemIndex, String recipeName) {


        final RecipeStepsFragment fragment = new RecipeStepsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putSerializable("steps", stepsOut);
        stepBundle.putInt("index", selectedItemIndex);
        stepBundle.putString("recipeName", recipeName);
        fragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment).addToBackStack("STACK_RECIPE_STEP_DETAIL")
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack("STACK_RECIPE_STEP_DETAIL")
                    .commit();
        }

    }

}
