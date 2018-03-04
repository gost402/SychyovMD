package com.example.dsychyov.sychyovmd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.dsychyov.sychyovmd.ui.activities.WelcomeActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class WelcomeActivityTests {

    private Intent intent;
    private Context context;
    private SharedPreferences.Editor preferencesEditor;

    private static int WELCOME_ACTIVITY_FRAGMENT_COUNT = 4;

    @Rule
    public ActivityTestRule<WelcomeActivity> activityRule = new ActivityTestRule<>(WelcomeActivity.class);

    @Before
    public void setUp() {
        intent = new Intent();
        context = getInstrumentation().getTargetContext();
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    @Test
    public void haveCorrectElementsOnWelcomePage() throws Exception {
        onView(withId(R.id.authorPhoto)).check(matches(isDisplayed()));
        swipeViewPager(1);
        onView(withId(R.id.app_icon)).check(matches(isDisplayed()));
        swipeViewPager(1);
        onView(withId(R.id.dark_theme_radio_button_wrapper)).check(matches(isDisplayed()));
        onView(withId(R.id.light_theme_radio_button_wrapper)).check(matches(isDisplayed()));
        swipeViewPager(1);
        onView(withId(R.id.dense_layout_radio_button_wrapper)).check(matches(isDisplayed()));
        onView(withId(R.id.standard_layout_radio_button_wrapper)).check(matches(isDisplayed()));
    }

    @Test
    public void changeThemeToDark() throws Exception {
        swipeViewPager(2);
//        onView(withId(R.id.dark_theme_radio_button_wrapper)).perform(click());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.dark_theme_radio_button),
                        childAtPosition(
                                allOf(withId(R.id.dark_theme_radio_button_wrapper),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        String key = context.getString(R.string.launcher_theme_dark_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        assertEquals("true", sharedPreferences.getString(key, null));
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

    @Test
    public void changeLayout() throws Exception {
        swipeViewPager(3);
        onView(withId(R.id.dense_layout_radio_button_wrapper)).perform(click());

        String key = context.getString(R.string.launcher_layout_dense_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        assertEquals("true", sharedPreferences.getString(key, null));
    }

    @Test
    public void shouldNavigateToLauncherActivity() {
        navigateToLauncherActivity();
        onView(withId(R.id.launcher_fragment_view_pager)).check(matches(isDisplayed()));
    }

    private void navigateToLauncherActivity() {
        swipeViewPager(WELCOME_ACTIVITY_FRAGMENT_COUNT - 1);
        onView(withId(R.id.next_step)).perform(click());
    }

    private void swipeViewPager(int steps) {
        for (int i = 0; i < steps; ++i) {
            onView(withId(R.id.welcome_view_pager)).perform(swipeLeft());
        }
    }
}
