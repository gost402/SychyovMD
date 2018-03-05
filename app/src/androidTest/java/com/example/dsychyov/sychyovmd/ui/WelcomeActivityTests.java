package com.example.dsychyov.sychyovmd.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.dsychyov.sychyovmd.R;
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
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class WelcomeActivityTests {
    private Context context;

    @Rule
    public ActivityTestRule<WelcomeActivity> activityRule = new ActivityTestRule<>(WelcomeActivity.class);

    @Before
    public void setUp() {
        context = getInstrumentation().getTargetContext();
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }

    @Test
    public void haveCorrectElementsOnWelcomePage() throws Exception {
        onView(ViewMatchers.withId(R.id.authorPhoto)).check(matches(isDisplayed()));
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

        //TODO: Idle Resources
        Thread.sleep(1000);
        onView(withId(R.id.dark_theme_radio_button_wrapper)).perform(click());
        //TODO: Idle Resources
        Thread.sleep(5000);

        String key = context.getString(R.string.launcher_theme_dark_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        assertTrue(sharedPreferences.getBoolean(key, false));
    }

    @Test
    public void changeLayout() throws Exception {
        swipeViewPager(3);

        //TODO: Idle Resources
        Thread.sleep(1000);
        onView(withId(R.id.dense_layout_radio_button)).perform(click());
        //TODO: Idle Resources
        Thread.sleep(5000);

        String key = context.getString(R.string.launcher_layout_dense_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        assertTrue(sharedPreferences.getBoolean(key, false));
    }

    @Test
    public void shouldNavigateToLauncherActivity() {
        navigateToLauncherActivity();
        onView(withId(R.id.launcher_fragment_view_pager)).check(matches(isDisplayed()));
    }

    private void navigateToLauncherActivity() {
        int fragmentsCount = 4;
        swipeViewPager(fragmentsCount - 1);
        onView(withId(R.id.next_step)).perform(click());
    }

    private void swipeViewPager(int steps) {
        for (int i = 0; i < steps; ++i) {
            onView(withId(R.id.welcome_view_pager)).perform(swipeLeft());
        }
    }
}
