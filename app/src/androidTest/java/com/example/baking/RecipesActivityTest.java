package com.example.baking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.baking.ui.activities.RecipesActivity;

import org.junit.After;
import org.junit.Before;
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
public class RecipesActivityTest {

    @Rule
    public final ActivityTestRule<RecipesActivity> mActivityRule = new ActivityTestRule<>(RecipesActivity.class);

    private IdlingResource mIdlingResource;

    // This block is executed after RecipeActivity's onCreate method.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void scrollToRecyclerViewFirstItem_CheckRecipeName() {
        onView(withId(R.id.rv_recipe_select))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(R.string.first_recipe_name)).check(matches(isDisplayed()));
    }

    @Test
    public void clickFirstItem_CheckRecipeDetailSteps() {
        onView(withId(R.id.rv_recipe_select)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_recipe_steps)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(R.string.ingredients)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
