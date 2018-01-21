package com.udacity.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String TAG = BakingAppWidget.class.getSimpleName();
    public static List<String> recipeIngredients = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, List<String> recipeIng,
                                int appWidgetId) {
        Log.d(TAG, "10........");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_grid_view, pendingIntent);
        Intent intentWidget = new Intent(context, GridViewWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intentWidget);

        Log.d(TAG, "11........");
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeIngredients(Context context, AppWidgetManager appWidgetManager, List<String> recipeIng,
                                               int[] appWidgetIds) {
        Log.d(TAG, "7........" + recipeIng);
        recipeIngredients.addAll(recipeIng);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeIng, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingAppWidgetService.startActionUpdateRecipeList(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

