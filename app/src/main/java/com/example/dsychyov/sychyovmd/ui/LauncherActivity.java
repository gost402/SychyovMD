package com.example.dsychyov.sychyovmd.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.dsychyov.sychyovmd.listeners.ChangeGridPropertiesSharedPreferencesListener;
import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.dao.PackageFrequenciesDAO;
import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LauncherActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ChangeGridPropertiesSharedPreferencesListener changeGridPropertiesSharedPreferencesListener;
    RecyclerView recyclerView;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (action == null || intent.getDataString() == null) {
                return;
            }

            String packageName = intent.getDataString().substring("package:".length());

            switch (action) {
                case Intent.ACTION_PACKAGE_REMOVED:
                    removeApplication(packageName);
                    break;
                case Intent.ACTION_PACKAGE_ADDED:
                    addApplication(packageName);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);

        initializeNavigationDrawer();
        initializePreferencesListener();
        initializeRecyclerView();
        registerApplicationsChangesReceiver();

        createGridLayout();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_drawer_grid_layout_button) {
            // if instance of ListAdapter
            createGridLayout();
        } else if (id == R.id.navigation_drawer_linear_layout_button) {
            // if instance of GridLayoutAdapter
            createLinearLayout();
        } else if (id == R.id.navigation_drawer_settings_button) {
            startActivityForClass(SettingsActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProfileActivity(View view) {
        startActivityForClass(ProfileActivity.class);
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.launcher_content);
        recyclerView.setHasFixedSize(true);
        final int offset = getResources().getDimensionPixelSize(R.dimen.launcher_icon_margin);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));
    }

    private void initializePreferencesListener() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        changeGridPropertiesSharedPreferencesListener = new ChangeGridPropertiesSharedPreferencesListener(this);
        preferences.registerOnSharedPreferenceChangeListener(changeGridPropertiesSharedPreferencesListener);
    }

    private void initializeNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void registerApplicationsChangesReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void createGridLayout() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean isDense = preferences.getBoolean(getString(R.string.launcher_layout_dense_key), false);

        int spanCountResource;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCountResource = isDense ? R.integer.span_count_dense : R.integer.span_count_standard;
        } else {
            spanCountResource = isDense ? R.integer.span_count_dense_land : R.integer.span_count_standard_land;
        }

        final int spanCount = getResources().getInteger(spanCountResource);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        final List<App> data = getAppsList();
        final LauncherAdapter launcherAdapter = new LauncherAdapter(data, R.layout.launcher_grid_item);
        recyclerView.setAdapter(launcherAdapter);
    }

    private void createLinearLayout() {
        RecyclerView recyclerView= findViewById(R.id.launcher_content);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<App> data = getAppsList();
        final LauncherAdapter launcherAdapter = new LauncherAdapter(data, R.layout.launcher_list_item);
        recyclerView.setAdapter(launcherAdapter);
    }

    private void addApplication(String packageName) {
        App app = getAppFromPackageName(packageName, null);

        if(app == null) {
            return;
        }

        LauncherAdapter launcherAdapter = (LauncherAdapter) recyclerView.getAdapter();
        launcherAdapter.addApplication(app, getAppComparator());
    }

    private void removeApplication(String packageName) {
        PackageFrequenciesDAO packageFrequenciesDAO = new PackageFrequenciesDAO(this);
        packageFrequenciesDAO.delete(packageName);

        LauncherAdapter launcherAdapter = (LauncherAdapter) recyclerView.getAdapter();
        launcherAdapter.removeApplicationByPackageName(packageName);
    }

    @NonNull
    private List<App> getAppsList() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> appsInfo = packageManager.getInstalledApplications(0);
        List<App> apps = new ArrayList<>();
        PackageFrequenciesDAO packageFrequenciesDAO = new PackageFrequenciesDAO(this);
        List<Pair<String, Integer>> packageFrequencies = packageFrequenciesDAO.getFrequencies();

        for (ApplicationInfo appInfo : appsInfo) {
            App app = getAppFromPackageName(appInfo.packageName, packageFrequencies);
            if(app != null) {
                apps.add(app);
            }
        }

        Comparator<App> comparator = getAppComparator();
        if(comparator != null) {
            Collections.sort(apps, comparator);
        }

        return apps;
    }

    private App getAppFromPackageName(String packageName, List<Pair<String, Integer>> packageFrequencies) {
        PackageManager packageManager = getPackageManager();

        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);

            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                Drawable icon = packageManager.getApplicationIcon(appInfo);
                String name = (String) packageManager.getApplicationLabel(appInfo);
                long createdAt = packageManager.getPackageInfo(appInfo.packageName, 0).firstInstallTime;
                int frequency = 0;

                Log.i("NAME", name);
                Log.i("PACKAGE", appInfo.packageName);

                if(packageFrequencies != null) {
                    for (Pair<String, Integer> packageFrequency : packageFrequencies) {
                        if(appInfo.packageName.equals(packageFrequency.first) && packageFrequency.second != null) {
                            frequency = packageFrequency.second;
                            break;
                        }
                    }
                }

                return new App(appInfo.packageName, name, icon, createdAt, frequency);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return null;
    }

    private Comparator<App> getAppComparator() {
        Comparator<App> comparator = null;

        switch (getSortType()) {
            case "Date": {
                comparator = App.createdAtComparator;
                break;
            }
            case "NameAsc": {
                comparator = App.nameAscComparator;
                break;
            }
            case "NameDesc": {
                comparator = App.nameDescComparator;
                break;
            }
            case "Popularity": {
                comparator = App.frequencyComparator;
                break;
            }
        }

        return comparator;
    }

    private String getSortType() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(getString(R.string.launcher_sorting_type_key), "Default");
    }
}
