package com.example.dinasaad.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IngredientTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ingredientTest() {
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.IngedientsList),
                                withParent(allOf(withId(R.id.container),
                                        withParent(withId(android.R.id.content))))),
                        1),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.Recipes),
                        withParent(allOf(withId(R.id.container),
                                withParent(allOf(withId(android.R.id.content),
                                        withParent(withId(R.id.decor_content_parent)))))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        withId(R.id.IngedientsList),
                        3),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.ingredientView), withText("light brown sugar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.IngedientsList),
                                        3),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("light brown sugar")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.quantityView), withText("Quantity: 100 G"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.IngedientsList),
                                        3),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Quantity: 100 G")));



    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
