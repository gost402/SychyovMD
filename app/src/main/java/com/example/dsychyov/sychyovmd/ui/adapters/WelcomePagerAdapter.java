package com.example.dsychyov.sychyovmd.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dsychyov.sychyovmd.ui.fragments.AppDescriptionFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.LayoutChooserFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.ThemeChooserFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.WelcomeFragment;

public class WelcomePagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] steps;

    public WelcomePagerAdapter(FragmentManager fm) {
        super(fm);

        Fragment welcome = new WelcomeFragment();
        Fragment appDescription = new AppDescriptionFragment();
        Fragment themeChooser = new ThemeChooserFragment();
        Fragment layoutChooser = new LayoutChooserFragment();

        steps = new Fragment[]{welcome, appDescription, themeChooser, layoutChooser};
    }

    @Override
    public Fragment getItem(int position) {
        return steps[position];
    }

    @Override
    public int getCount() {
        return steps.length;
    }
}
