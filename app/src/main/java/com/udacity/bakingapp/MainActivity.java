package com.udacity.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.IdlingResource.SimpleIdlingResource;
import com.udacity.bakingapp.pojo.Recipes;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIdlingResource();
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        IdlingPolicies.setMasterPolicyTimeout(3, TimeUnit.MINUTES);
        IdlingPolicies.setIdlingResourceTimeout(3, TimeUnit.MINUTES);
        return mIdlingResource;
    }

    public void onClick(Recipes recipes) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("ingredients", recipes.getIngredients());
        intent.putExtra("steps", recipes.getSteps());
        intent.putExtra("recipes", recipes);
        intent.putExtra("recipeName", recipes.getName());
        startActivity(intent);
    }

}
