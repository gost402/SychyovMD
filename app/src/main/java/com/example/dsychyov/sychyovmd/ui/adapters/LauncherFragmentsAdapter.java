package com.example.dsychyov.sychyovmd.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.DesktopFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.GridFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.ListFragment;

import java.util.ArrayList;
import java.util.List;

public class LauncherFragmentsAdapter extends FragmentStatePagerAdapter {
    private final Fragment[] steps;

    private final GridFragment gridFragment;
    private final ListFragment listFragment;

    public LauncherFragmentsAdapter(FragmentManager fm, LauncherActivity launcherActivity) {
        super(fm);

        List<App> apps = new ArrayList<App>();

        Fragment desktopFragment = new DesktopFragment();
        gridFragment = GridFragment.newInstance(apps, launcherActivity);
        listFragment = ListFragment.newInstance(apps, launcherActivity);

        steps = new Fragment[]{ desktopFragment, gridFragment, listFragment };
    }

    public void updateAppsList(List<App> apps) {
        gridFragment.setAppsAdapterData(apps);
        listFragment.setAppsAdapterData(apps);
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
