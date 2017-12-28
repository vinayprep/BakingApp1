package com.udacity.bakingapp.pojo;

import java.io.Serializable;

/**
 * Created by vinaygharge on 28/12/17.
 */

public class Recipes implements Serializable {
    private String id;

    private String name;

    private Ingredients[] ingredients;

    private Steps[] steps;

    private String servings;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ingredients[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients[] ingredients) {
        this.ingredients = ingredients;
    }

    public Steps[] getSteps() {
        return steps;
    }

    public void setSteps(Steps[] steps) {
        this.steps = steps;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }


    @Override
    public String toString() {
        return "ClassPojo [id = " + id +
                ", name = " + name +
                ", ingredients = " + ingredients +
                ", steps = " + steps +
                ", servings = " + servings +
                ", image = " + image + "]";
    }
}
