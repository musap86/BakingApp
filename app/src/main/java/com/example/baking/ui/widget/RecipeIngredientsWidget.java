package com.example.baking.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.baking.R;
import com.example.baking.ui.activities.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {
    private static String sIngredients;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        String ingredients, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);


        sIngredients = ingredients;
        views.setTextViewText(R.id.appwidget_text, ingredients);

        Intent intent = new Intent(context, RecipesActivity.class);
        intent.putExtra("widget", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeIngredientsWidget(Context context, AppWidgetManager appWidgetManager,
                                                     String ingredients, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, ingredients, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // When the widget is added for the first time, sIngredients is null.
        if (sIngredients == null) {
            String ingredients = context.getResources().getString(R.string.widget_default);
            updateRecipeIngredientsWidget(context, appWidgetManager, ingredients, appWidgetIds);
        }
    }
}

