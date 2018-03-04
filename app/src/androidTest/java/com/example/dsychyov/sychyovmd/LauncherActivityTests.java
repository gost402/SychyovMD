package com.example.dsychyov.sychyovmd;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
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
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LauncherActivityTests {

    @Rule
    public ActivityTestRule<LauncherActivity> activityRule = new ActivityTestRule<>(LauncherActivity.class);

    @Before
    public void setUp() {
//        intent = new Intent();
//        Context context = getInstrumentation().getTargetContext();
//        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    @Test
    public void gridApplicationPopupContainsCorrectOptions() throws Exception {
        swipeViewPagerLeft(1);
        onView(withId(R.id.launcher_fragment_grid)).check(matches(isDisplayed()));

        onView(withId(R.id.launcher_content)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        0,
                        MyViewAction.longClickChildViewWithId(R.id.grid_item_wrapper)
                )
        );

        onView(allOf(withId(R.id.title), withText(R.string.app_add_desktop),
                childAtPosition(childAtPosition(withClassName(
                        Matchers.is("android.support.v7.view.menu.ListMenuItemView")
                ),0),0))
        ).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.title), withText(R.string.app_delete),
                childAtPosition(childAtPosition(withClassName(
                        Matchers.is("android.support.v7.view.menu.ListMenuItemView")
                ),0),0))
        ).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.title), withText(R.string.app_frequency),
                childAtPosition(childAtPosition(withClassName(
                        Matchers.is("android.support.v7.view.menu.ListMenuItemView")
                ),0),0))
        ).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.title), withText(R.string.app_info),
                childAtPosition(childAtPosition(withClassName(
                        Matchers.is("android.support.v7.view.menu.ListMenuItemView")
                ),0),0))
        ).check(matches(isDisplayed()));

        //        onView(allOf)
//        onView(withId(R.id.app_add_desktop)).check(matches(isDisplayed()));
//        onView(withId(R.id.app_delete)).check(matches(isDisplayed()));
//        onView(withId(R.id.app_frequency)).check(matches(isDisplayed()));
//        onView(withId(R.id.app_info)).check(matches(isDisplayed()));
    }

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void addAppToDesktop() throws Exception {
        RecyclerView desktopRecyclerView = activityRule.getActivity().findViewById(R.id.desktop_recycler_view);
        int desktopItemsCount = desktopRecyclerView.getAdapter().getItemCount();

        swipeViewPagerLeft(1);
        onView(withId(R.id.launcher_fragment_grid)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.launcher_content).atPosition(0)).check(matches(isDisplayed()));

        onView(withId(R.id.launcher_content)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        0,
                        MyViewAction.longClickChildViewWithId(R.id.grid_item_wrapper)
                )
        );

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText(R.string.app_add_desktop),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        swipeViewPagerRight(1);

        onView(withId(R.id.desktop_recycler_view)).check(new RecyclerViewItemCountAssertion(desktopItemsCount + 1));
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
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



    // Dont know how to add contact
    @Test
    public void addContactToDesktop() throws Exception {
//        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

    }

    @Test
    public void addCustomUriToDesktop() throws Exception {
        RecyclerView desktopRecyclerView = activityRule.getActivity().findViewById(R.id.desktop_recycler_view);
        int desktopItemsCount = desktopRecyclerView.getAdapter().getItemCount();

        ViewInteraction floatingActionButton = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.menu),
                                childAtPosition(
                                        withClassName(Matchers.is("android.support.design.widget.CoordinatorLayout")),
                                        1)),
                        2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.desktop_add_url),
                        childAtPosition(
                                allOf(withId(R.id.menu),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.name_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("tttt"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.url_input), withText("https://"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("https://ttttt"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.url_input), withText("https://ttttt"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.add_url_button), withText("Добавить"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        appCompatButton6.perform(click());

        // TODO: Replace with idling recources
        Thread.sleep(5000);

        onView(withId(R.id.desktop_recycler_view)).check(new RecyclerViewItemCountAssertion(desktopItemsCount + 1));
//        add_url_button
    }


    @Test
    public void cancelUrlAdding() throws Exception {
        RecyclerView desktopRecyclerView = activityRule.getActivity().findViewById(R.id.desktop_recycler_view);
        int desktopItemsCount = desktopRecyclerView.getAdapter().getItemCount();

//        onView(withId(R.id.menu)).perform(click());
//        onView(withId(R.id.desktop_add_url)).perform(click());
//        onView(withId(R.id.name_input)).perform(typeText("text"));
//        onView(withId(R.id.url_input)).perform(typeText("https://text"));
//        onView(withId(R.id.add_url_button)).perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.menu),
                                childAtPosition(
                                        withClassName(Matchers.is("android.support.design.widget.CoordinatorLayout")),
                                        1)),
                        2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.desktop_add_url),
                        childAtPosition(
                                allOf(withId(R.id.menu),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.name_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("tttt"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.url_input), withText("https://"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("https://ttttt"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.url_input), withText("https://ttttt"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.cancel_add_url_button), withText("Отмена"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // TODO: Replace with idling recources
        Thread.sleep(5000);

        onView(withId(R.id.desktop_recycler_view)).check(new RecyclerViewItemCountAssertion(desktopItemsCount));
    }


    @Test
    public void showCorrectOptionsInNavigationDrawer() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        //Here's the difference
//        onView(withId(R.id.nav_view)).check(NavigationViewActions.navigateTo(R.id.navigation_drawer_settings_button));
    }

    @Test
    public void openSettings() throws Exception {
//        swipeViewPagerRight(1);
//        onView(withId(R.id.navigation_drawer_settings_button)).perform(click());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        //Here's the difference
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.navigation_drawer_settings_button));

        onView(withId(R.id.settings_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void openProfile() throws Exception {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.navigation_drawer_author_photo), withContentDescription("Placeholder"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_header_container),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

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
