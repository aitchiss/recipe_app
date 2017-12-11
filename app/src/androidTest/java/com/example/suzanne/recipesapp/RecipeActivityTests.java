package com.example.suzanne.recipesapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.suzanne.recipesapp.models.IngredientSpecification;
import com.example.suzanne.recipesapp.models.MeasurementType;
import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.models.RecipeStep;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.IsNot.not;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by suzanne on 11/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTests {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<RecipeActivity>(RecipeActivity.class, false, false);


    private Recipe mTestRecipe;


    @Before
    public void intent(){
            Intent intent = new Intent();
            mTestRecipe  = new Recipe(1, "test recipe");

            IngredientSpecification ingredient1 = new IngredientSpecification(12, MeasurementType.G, "carrots");
            IngredientSpecification ingredient2 = new IngredientSpecification(2, MeasurementType.CUP, "sugar");
            IngredientSpecification[] ingredients = new IngredientSpecification[2];
            ingredients[0] = ingredient1;
            ingredients[1] = ingredient2;

            RecipeStep recipeStep = new RecipeStep(1,
                    "short description",
                    "full description",
                    "url",
                    "thumbnail");

            RecipeStep[] steps = new RecipeStep[1];
            steps[0] = recipeStep;

            mTestRecipe.setIngredients(ingredients);
            mTestRecipe.setSteps(steps);
            intent.putExtra("recipe", mTestRecipe);
            mActivityTestRule.launchActivity(intent);
    }
    @Test
    public void testRecipeTitleIsDisplayed(){
        onView(allOf(isDescendantOfA(withResourceName("android:id/action_bar_container")),
                withText(mTestRecipe.getName())));
    }

    @Test
    public void stepDescriptionShownCorrectly() {
        onView(withId(R.id.tv_step_short_description))
                .check(matches(withText(mTestRecipe.getSteps()[0].getShortDescription())));
    }

    @Test
    public void testClickingRecipeStepShowsStepDetail(){
        onView(withId(R.id.step_desc_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.tv_step_description))
                .check(matches(withText(mTestRecipe.getSteps()[0].getDescription())));
    }

}
