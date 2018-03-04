package com.example.dsychyov.sychyovmd.ui;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.support.RecycleViewItemLongClick;
import com.example.dsychyov.sychyovmd.support.RecyclerViewItemCountAssertion;
import com.example.dsychyov.sychyovmd.support.RecyclerViewMatcher;
import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LauncherActivityTests {

    @Rule
    public ActivityTestRule<LauncherActivity> activityRule = new ActivityTestRule<>(LauncherActivity.class);

    @Test
    public void gridApplicationPopupContainsCorrectOptions() throws Exception {
        swipeViewPagerLeft(1);
        onView(ViewMatchers.withId(R.id.launcher_fragment_grid)).check(matches(isDisplayed()));

        onView(withId(R.id.launcher_content)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        0,
                        RecycleViewItemLongClick.longClickChildViewWithId(R.id.grid_item_wrapper)
                )
        );

        Thread.sleep(1000);

        onView(allOf(withId(R.id.title), withText(R.string.app_add_desktop))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), withText(R.string.app_delete))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), withText(R.string.app_frequency))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), withText(R.string.app_info))).check(matches(isDisplayed()));
    }

    @Test
    public void addAppToDesktop() throws Exception {
        RecyclerView desktopRecyclerView = activityRule.getActivity().findViewById(R.id.desktop_recycler_view);
        int desktopItemsCount = desktopRecyclerView.getAdapter().getItemCount();

        swipeViewPagerLeft(1);
        onView(withId(R.id.launcher_fragment_grid)).check(matches(isDisplayed()));
        onView(new RecyclerViewMatcher(R.id.launcher_content).atPosition(0)).check(matches(isDisplayed()));

        onView(withId(R.id.launcher_content)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        0,
                        RecycleViewItemLongClick.longClickChildViewWithId(R.id.grid_item_wrapper)
                )
        );

        // TODO: Idle Resources
        Thread.sleep(2000);

        onView(allOf(withId(R.id.title), withText(R.string.app_add_desktop))).perform(click());
        swipeViewPagerRight(1);
        onView(withId(R.id.desktop_recycler_view)).check(new RecyclerViewItemCountAssertion(desktopItemsCount + 1));
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
    public void addCustomUriToDesktop() throws Exception {
        RecyclerView desktopRecyclerView = activityRule.getActivity().findViewById(R.id.desktop_recycler_view);
        int desktopItemsCount = desktopRecyclerView.getAdapter().getItemCount();

        onView(childAtPosition(withId(R.id.menu), 2)).perform(click());
        onView(withId(R.id.desktop_add_url)).perform(click());
        onView(withId(R.id.name_input)).perform(replaceText("tttt"), closeSoftKeyboard());
        onView(withId(R.id.url_input)).perform(replaceText("https://ttttt"), closeSoftKeyboard());
        onView(withId(R.id.add_url_button)).perform(closeSoftKeyboard(), click());

        // TODO: Replace with idling resources
        Thread.sleep(3000);

        onView(withId(R.id.desktop_recycler_view)).check(new RecyclerViewItemCountAssertion(desktopItemsCount + 1));
    }

    @Test
    public void cancelUrlAdding() throws Exception {
        RecyclerView desktopRecyclerView = activityRule.getActivity().findViewById(R.id.desktop_recycler_view);
        int desktopItemsCount = desktopRecyclerView.getAdapter().getItemCount();

        onView(childAtPosition(withId(R.id.menu), 2)).perform(click());
        onView(withId(R.id.desktop_add_url)).perform(click());
        onView(withId(R.id.name_input)).perform(replaceText("tttt"), closeSoftKeyboard());
        onView(withId(R.id.url_input)).perform(replaceText("https://ttttt"), closeSoftKeyboard());
        onView(withId(R.id.cancel_add_url_button)).perform(closeSoftKeyboard(), click());

        // TODO: Replace with idling resources
        Thread.sleep(5000);

        onView(withId(R.id.desktop_recycler_view)).check(new RecyclerViewItemCountAssertion(desktopItemsCount));
    }

    @Test
    public void openSettings() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.navigation_drawer_settings_button));
        onView(withId(R.id.settings_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void openProfile() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.navigation_drawer_author_photo)).perform(click());
        onView(withId(R.id.profile_items_container)).check(matches(isDisplayed()));
    }

    private void swipeViewPagerLeft(int steps) {
        for (int i = 0; i < steps; ++i) {
            onView(withId(R.id.launcher_fragment_view_pager)).perform(swipeLeft());
        }
    }

    private void swipeViewPagerRight(int steps) {
        for (int i = 0; i < steps; ++i) {
            onView(withId(R.id.launcher_fragment_view_pager)).perform(swipeRight());
        }
    }
}
