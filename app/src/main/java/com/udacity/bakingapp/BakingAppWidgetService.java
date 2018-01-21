package com.udacity.bakingapp;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import static com.udacity.bakingapp.RecipeDetailFragment.key;

/**
 * Created by vinaygharge on 21/01/18.
 */

public class BakingAppWidgetService extends IntentService {
    public static final String ACTION_UPDATE_RECIPE_LIST = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final String TAG = BakingAppWidgetService.class.getSimpleName();
    private static final String MY_PREFS_NAME = "MY_PREFENCES";

    SharedPreferences sharedpreferences;

    public BakingAppWidgetService(String name) {
        super(name);
    }

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void startActionUpdateRecipeList(Context context) {
        Log.d(TAG, "1........");
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_LIST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "2........");
        if (intent != null) {
            Log.d(TAG, "3........");
            String intentAction = intent.getAction();
            if (ACTION_UPDATE_RECIPE_LIST.equals(intentAction)) {
                Log.d(TAG, "4........");
                sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                String recipeList = sharedpreferences.getString("recipeList", getResources().getString(R.string.appwidget_text));
                List<String> ingredients = Arrays.asList(recipeList.split(key));
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
                BakingAppWidget.updateRecipeIngredients(this, appWidgetManager, ingredients, appWidgetIds);
                Log.d(TAG, "5........");
            }
        }
    }
}
