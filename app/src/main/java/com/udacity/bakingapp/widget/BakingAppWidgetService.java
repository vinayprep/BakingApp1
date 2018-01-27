package com.udacity.bakingapp.widget;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
/**
 * Created by vinaygharge on 21/01/18.
 */

public class BakingAppWidgetService extends IntentService {
    public static final String ACTION_UPDATE_RECIPE_LIST = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final String TAG = BakingAppWidgetService.class.getSimpleName();

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void startActionUpdateRecipeList(Context context, ArrayList<String> ingredients) {
        Log.d(TAG, "1........");
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.putExtra("ingredients", ingredients);
        intent.setAction(ACTION_UPDATE_RECIPE_LIST);
        context.startService(intent);
    }

    private void callServiceIntent(ArrayList<String> ingredients) {
        Intent intent = new Intent(ACTION_UPDATE_RECIPE_LIST);
        intent.putExtra("ingredients", ingredients);
        intent.setAction(ACTION_UPDATE_RECIPE_LIST);
        sendBroadcast(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "2........");
        if (intent != null) {
            callServiceIntent(intent.getExtras().getStringArrayList("ingredients"));
        }
    }
}
