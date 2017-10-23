package com.example.dinasaad.bakingapp;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by DinaSaad on 25/08/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityBasicTest {

          /**
         * The ActivityTestRule is a rule provided by Android used for functional testing of a single
         * activity. The activity that will be tested will be launched before each test that's annotated
         * with @Test and before methods annotated with @Before. The activity will be terminated after
         * the test and methods annotated with @After are complete. This rule allows you to directly
         * access the activity during the test.
         */
        @Rule
        public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

        /**
         * Clicks on a GridView item and checks it opens up the OrderActivity with the correct details.
         */
        @Test
        public void RecipeNameCountTest () {
            onView (withId(R.id.IngedientsList)).check(ViewAssertions.matches(Matchers.withListSize(4)));
            onData(anything()).inAdapterView(withId(R.id.IngedientsList)).atPosition(0).onChildView(withId(R.id.Recipe_name)).check(matches(withText("Nutella Pie")));
            onData(anything()).inAdapterView(withId(R.id.IngedientsList)).atPosition(1).onChildView(withId(R.id.Recipe_name)).check(matches(withText("Brownies")));
            onData(anything()).inAdapterView(withId(R.id.IngedientsList)).atPosition(2).onChildView(withId(R.id.Recipe_name)).check(matches(withText("Yellow Cake")));
            onData(anything()).inAdapterView(withId(R.id.IngedientsList)).atPosition(3).onChildView(withId(R.id.Recipe_name)).check(matches(withText("Cheesecake")));
        }

    @Test
    public void RecipeStepsCountTest () {

         onData(anything()).inAdapterView(withId(R.id.IngedientsList)).atPosition(1).perform(click());
         onView(withId(R.id.Recipes)).check(new RecyclerViewItemCountAssertion(11));
    }
}
class Matchers {
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }
}
