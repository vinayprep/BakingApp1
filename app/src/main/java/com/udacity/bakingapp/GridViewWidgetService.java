package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import static com.udacity.bakingapp.BakingAppWidget.recipeIngredients;

/**
 * Created by vinaygharge on 21/01/18.
 */

public class GridViewWidgetService extends RemoteViewsService {
    private static final String TAG = GridViewWidgetService.class.getSimpleName();
    List<String> ingredients;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext = null;

        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            ingredients = recipeIngredients;
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(TAG, "8...............");
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_list_widgets);
            views.setTextViewText(R.id.widget_ingredient_name, ingredients.get(position));
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_ingredient_name, fillInIntent);
            Log.d(TAG, "9...............");
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
