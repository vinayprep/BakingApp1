package com.udacity.bakingapp.pojo;

import java.io.Serializable;

/**
 * Created by vinaygharge on 27/12/17.
 */

public class Ingredients implements Serializable {

    private String quantity;

    private String measure;

    private String ingredient;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }


    @Override
    public String toString() {
        return "ClassPojo [quantity = " + quantity +
                ", measure = " + measure +
                ", ingredient = " + ingredient + "]";
    }
}
