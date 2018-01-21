package com.udacity.bakingapp.model;

import com.udacity.bakingapp.pojo.Recipes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;


public interface Api {

    @GET("baking.json")
    Call<ArrayList<Recipes>> getReceipes();

}
