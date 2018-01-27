package com.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.RecipeDetailActivity;

import java.util.ArrayList;

import static com.udacity.bakingapp.widget.BakingAppWidgetService.ACTION_UPDATE_RECIPE_LIST;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String TAG = BakingAppWidget.class.getSimpleName();
    static ArrayList<String> recipeIngredients = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "10........");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.addCategory(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, pendingIntent);

        Intent intentWidget = new Intent(context, GridViewWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intentWidget);

        Log.d(TAG, "11........");
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeIngredients(Context context, AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds) {
        Log.d(TAG, "7........");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "3........");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));
        String intentAction = intent.getAction();
        if (ACTION_UPDATE_RECIPE_LIST.equals(intentAction)) {
            Log.d(TAG, "4........");
            recipeIngredients = intent.getExtras().getStringArrayList("ingredients");
            if (recipeIngredients == null || recipeIngredients.size() == 0) {
                recipeIngredients = new ArrayList<>();
                recipeIngredients.add("Please run the App to get the recipes.");
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
            BakingAppWidget.updateRecipeIngredients(context, appWidgetManager, appWidgetIds);
            Log.d(TAG, "5........");
        }
        super.onReceive(context, intent);
    }
}

