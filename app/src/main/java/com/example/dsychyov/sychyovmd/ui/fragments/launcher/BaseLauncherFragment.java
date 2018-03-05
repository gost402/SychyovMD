package com.example.dsychyov.sychyovmd.ui.fragments.launcher;

import android.support.v4.app.Fragment;

import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherAdapter;

import java.util.List;

public abstract class BaseLauncherFragment extends Fragment {

    protected List<App> data;

    protected LauncherAdapter launcherAdapter;

    protected LauncherActivity launcherActivity;

    protected void initializeAppsList(List<App> list) {
        this.data = list;
    }

    protected void initializeLauncherActivity(LauncherActivity launcherActivity) {
        this.launcherActivity = launcherActivity;
    }

    public void setAppsAdapterData(List<App> data) {
        this.data = data;

        if(launcherAdapter != null) {
            launcherAdapter.setApps(data);
        }
    }
}
