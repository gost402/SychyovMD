package com.example.dsychyov.sychyovmd.ui.fragments.launcher;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;

import com.example.dsychyov.sychyovmd.dao.PackageFrequenciesDAO;
import com.example.dsychyov.sychyovmd.models.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseLauncherFragment extends Fragment {

    @NonNull
    protected List<App> getAppsList() {
        if(getContext() == null) {
            return new ArrayList<>();
        }

        PackageManager packageManager = getContext().getPackageManager();
        List<ApplicationInfo> appsInfo = packageManager.getInstalledApplications(0);
        List<App> apps = new ArrayList<>();
        PackageFrequenciesDAO packageFrequenciesDAO = new PackageFrequenciesDAO(getContext());
        List<Pair<String, Integer>> packageFrequencies = packageFrequenciesDAO.getFrequencies();

        for (ApplicationInfo appInfo : appsInfo) {
            App app = App.getAppFromPackageName(getContext(), appInfo.packageName, packageFrequencies);
            if(app != null) {
                apps.add(app);
            }
        }

        Comparator<App> comparator = App.getAppComparator(getContext());
        if(comparator != null) {
            Collections.sort(apps, comparator);
        }

        return apps;
    }
}
