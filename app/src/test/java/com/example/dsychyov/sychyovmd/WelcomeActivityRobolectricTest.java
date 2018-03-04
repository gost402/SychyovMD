package com.example.dsychyov.sychyovmd;

import com.example.dsychyov.sychyovmd.ui.activities.WelcomeActivity;
import com.yandex.metrica.YandexMetrica;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

//public class WelcomeActivityRobolectricTest extends RobolectricTest {

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(YandexMetrica.class)
public class WelcomeActivityRobolectricTest {

    private final WelcomeActivity mActivity = Robolectric.setupActivity(WelcomeActivity.class);

//    @BeforeClass
//    public static void beforeEverything() {
////        mockStatic(YandexMetrica.class);
//    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
//
//    @Test
//    public void viewPagerElementsCount() {
////        when(YandexMetrica.class.reportEvent)
////        YandexMetricametrica mock(YandexMetrica.class);
//
//        ViewPager viewPager = mActivity.findViewById(R.id.welcome_view_pager);
//        assertThat(viewPager.getAdapter().getCount(), is(4));
//    }
//
//    @Test
//    public void welcomeFragmentPosition() {
//        ViewPager viewPager = mActivity.findViewById(R.id.welcome_view_pager);
//        FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
//
//        assertThat(adapter.getItem(0), is(instanceOf(WelcomeFragment.class)));
//    }
//
//    @Test
//    public void appDescriptionFragmentPosition() {
//        ViewPager viewPager = mActivity.findViewById(R.id.welcome_view_pager);
//        FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
//
//        assertThat(adapter.getItem(1), is(instanceOf(AppDescriptionFragment.class)));
//    }
//
//    @Test
//    public void themeChooserFragmentPosition() {
//        ViewPager viewPager = mActivity.findViewById(R.id.welcome_view_pager);
//        FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
//
//        assertThat(adapter.getItem(2), is(instanceOf(ThemeChooserFragment.class)));
//    }
//
//    @Test
//    public void layoutChooserFragmentPosition() {
//        ViewPager viewPager = mActivity.findViewById(R.id.welcome_view_pager);
//        FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
//
//        assertThat(adapter.getItem(3), is(instanceOf(LayoutChooserFragment.class)));
//    }
}