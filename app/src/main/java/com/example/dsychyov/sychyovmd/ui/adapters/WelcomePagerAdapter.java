package com.example.dsychyov.sychyovmd.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dsychyov.sychyovmd.ui.fragments.AppDescriptionFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.LayoutChooserFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.ThemeChooserFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.WelcomeFragment;

public class WelcomePagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] steps;

    public WelcomePagerAdapter(FragmentManager fm) {
        super(fm);

        steps = new Fragment[]{
            new WelcomeFragment(), new AppDescriptionFragment(),
            new ThemeChooserFragment(), new LayoutChooserFragment()
        };
    }

    public ThemeChooserFragment getThemeChooserFragment() {
        return (ThemeChooserFragment) getItem(2);
    }

    public LayoutChooserFragment getLayoutChooserFragment() {
        return (LayoutChooserFragment) getItem(3);
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
