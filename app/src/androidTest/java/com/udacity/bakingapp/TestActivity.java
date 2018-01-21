package com.udacity.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestActivity {
    private static final String TAG = TestActivity.class.getSimpleName();

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkItemClick() {
        onView(ViewMatchers.withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText("Yellow Cake")).check(matches(isDisplayed()));
    }

    @Test
    public void checkExoPlayer() {
        onView(ViewMatchers.withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.recipe_detail_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.playerView)).check(matches(isDisplayed()));
    }

}