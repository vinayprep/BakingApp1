package com.udacity.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.pojo.Recipes;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
