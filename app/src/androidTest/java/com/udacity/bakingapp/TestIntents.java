package com.udacity.bakingapp;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestIntents {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<MainActivity>(MainActivity.class);

    @Before
    public void checkAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void checkIntent() {
        onView(ViewMatchers.withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }
}