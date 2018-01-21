
package com.udacity.bakingapp.IdlingResource;


import android.support.test.espresso.IdlingRegistry;

import com.jakewharton.espresso.OkHttp3IdlingResource;

import okhttp3.OkHttpClient;

public abstract class CustomIdlingRegistry {
    public static void registerOkHttp(OkHttpClient okHttpClient) {
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create(
                "okhttp", okHttpClient));
    }

}