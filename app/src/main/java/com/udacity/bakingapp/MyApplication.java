package com.udacity.bakingapp;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
