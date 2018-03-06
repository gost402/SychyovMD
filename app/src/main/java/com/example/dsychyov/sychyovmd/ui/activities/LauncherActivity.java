package com.example.dsychyov.sychyovmd.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.async_tasks.launcher.DeleteDesktopAppByPackageName;
import com.example.dsychyov.sychyovmd.broadcast_receivers.LauncherActivityBroadcastReceiver;
import com.example.dsychyov.sychyovmd.dao.PackageFrequenciesDao;
import com.example.dsychyov.sychyovmd.image_loaders.ImageSaver;
import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.services.DownloadBackgroundAlarmService;
import com.example.dsychyov.sychyovmd.ui.CustomViewPager;
import com.example.dsychyov.sychyovmd.ui.adapters.DesktopAppsAdapter;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherAdapter;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherFragmentsAdapter;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.DesktopFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.GridFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.ListFragment;
import com.yandex.metrica.YandexMetrica;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CustomViewPager fragmentsViewPager;
    private LauncherFragmentsAdapter launcherFragmentsAdapter;
    private NavigationView navigationView;
    private UpdateAppsTask updateAppsTask;

    private boolean isHideMenu;

    private BroadcastReceiver broadcastReceiver;

    public void updateBackgroundImage() {
        final Bitmap bitmap = ImageSaver.getInstance().loadImage(
                getApplicationContext(),
                DownloadBackgroundAlarmService.MAIN_BACKGROUND_FILE
        );

        if(bitmap == null) {
            final Intent intent = new Intent(this, DownloadBackgroundAlarmService.class);
            startService(intent);
        }

        ImageView backgroundImage = findViewById(R.id.background_image);
        backgroundImage.setAlpha(0.6f);
        backgroundImage.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        continuousDeploymentIntegrations();

        boolean shouldBeInitialized = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.launcher_not_initialized_key), true);

        if(shouldBeInitialized) {
            startActivityForClass(WelcomeActivity.class);
            finish();
            return;
        }

        setContentView(R.layout.activity_launcher);

        YandexMetrica.reportEvent("LauncherActivity OnCreate");

        broadcastReceiver = new LauncherActivityBroadcastReceiver(this);

        initializeNavigationDrawer();
        initializeDownloadBackgroundAlarmService();
        initializeFragmentsViewPager();

        updateAppsList();

        registerApplicationsChangesReceiver();
        updateBackgroundImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void continuousDeploymentIntegrations() {
        Fabric.with(this, new Crashlytics());
        checkForUpdates();
    }

    private void checkForUpdates() {
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        unregisterManagers();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_drawer_desktop_button) {
            YandexMetrica.reportEvent("User choose desktop fragment");
            isHideMenu = false;
            fragmentsViewPager.setCurrentItem(0);
        } else if (id == R.id.navigation_drawer_grid_layout_button) {
            YandexMetrica.reportEvent("User choose grid fragment");
            isHideMenu = true;
            fragmentsViewPager.setCurrentItem(1);
        } else if (id == R.id.navigation_drawer_linear_layout_button) {
            YandexMetrica.reportEvent("User choose linear layout fragment");
            isHideMenu = true;
            fragmentsViewPager.setCurrentItem(2);
        } else if (id == R.id.navigation_drawer_settings_button) {
            YandexMetrica.reportEvent("User choose settings");
            startActivityForClass(SettingsActivity.class);
        }

        invalidateOptionsMenu();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.launcher_toolbar_menu, menu);

        if(isHideMenu) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.desktop_app_move:
                onDesktopAppMoveMenuItemClick();
                break;
        }
        return true;
    }

    public void showProfileActivity(View view) {
        startActivityForClass(ProfileActivity.class);
    }

    private void initializeDownloadBackgroundAlarmService() {
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if (alarm == null) {
            return;
        }

        final int DOWNLOAD_IMAGE_CODE = 122112;

        Intent intent = new Intent(this, DownloadBackgroundAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                LauncherActivity.this,
                DOWNLOAD_IMAGE_CODE,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String reloadBackgroundInterval = preferences.getString(
                getResources().getString(R.string.launcher_reload_background_key),
                "15"
        );

        int interval = Integer.parseInt(reloadBackgroundInterval) * 60 * 1000;

        alarm.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                interval,
                pendingIntent
        );
    }

    private void initializeNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeFragmentsViewPager() {
        launcherFragmentsAdapter = new LauncherFragmentsAdapter(getSupportFragmentManager(), this);
        fragmentsViewPager = findViewById(R.id.launcher_fragment_view_pager);

        fragmentsViewPager.setAdapter(launcherFragmentsAdapter);
        fragmentsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment fragment = launcherFragmentsAdapter.getItem(position);

                if(fragment instanceof DesktopFragment) {
                    isHideMenu = false;
                    navigationView.setCheckedItem(R.id.navigation_drawer_desktop_button);
                } else if(fragment instanceof GridFragment) {
                    isHideMenu = true;
                    navigationView.setCheckedItem(R.id.navigation_drawer_grid_layout_button);
                } else if(fragment instanceof ListFragment) {
                    isHideMenu = true;
                    navigationView.setCheckedItem(R.id.navigation_drawer_linear_layout_button);
                }

                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    public void updateAppsList() {
        if(updateAppsTask != null && !updateAppsTask.isCancelled()) {
            updateAppsTask.cancel(true);
        }

        updateAppsTask = new UpdateAppsTask();
        updateAppsTask.execute();
    }

    // TODO: Remove;
    @SuppressLint("StaticFieldLeak")
    private class UpdateAppsTask extends AsyncTask<Void, Void, List<App>> {
        @Override
        @WorkerThread
        protected List<App> doInBackground(Void... voids) {
            return getAppsList();
        }

        @Override
        @MainThread
        protected void onPostExecute(List<App> result) {
            super.onPostExecute(result);
            launcherFragmentsAdapter.updateAppsList(result);
        }
    }

    private void registerApplicationsChangesReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(broadcastReceiver, intentFilter);

        registerReceiver(
                broadcastReceiver,
                new IntentFilter(DownloadBackgroundAlarmService.BROADCAST_ACTION_UPDATE_IMAGE)
        );
    }

    private void onDesktopAppMoveMenuItemClick() {
        RecyclerView recyclerView = findViewById(R.id.desktop_recycler_view);
        if(recyclerView != null) {
            DesktopAppsAdapter adapter = (DesktopAppsAdapter) recyclerView.getAdapter();
            boolean newMoveValue = !adapter.isMove();
            fragmentsViewPager.setPagingEnabled(newMoveValue);
            adapter.setMove(newMoveValue);
            adapter.notifyDataSetChanged();
        }
    }

    @NonNull
    protected List<App> getAppsList() {
        PackageManager packageManager = getPackageManager();

        List<ApplicationInfo> appsInfo = packageManager.getInstalledApplications(0);
        List<App> apps = new ArrayList<>();
        PackageFrequenciesDao packageFrequenciesDao = new PackageFrequenciesDao(this);
        List<Pair<String, Integer>> packageFrequencies = packageFrequenciesDao.getFrequencies();

        for (ApplicationInfo appInfo : appsInfo) {
            App app = App.getAppFromPackageName(this, appInfo.packageName, packageFrequencies);
            if(app != null) {
                apps.add(app);
            }
        }

        Comparator<App> comparator = App.getAppComparator(this);
        if(comparator != null) {
            Collections.sort(apps, comparator);
        }

        return apps;
    }

    public void addApplication(Intent intent) {
        if(intent.getDataString() == null) {
            return;
        }

        String packageName = intent.getDataString().substring("package:".length());
        YandexMetrica.reportEvent("User add application");

        App app = App.getAppFromPackageName(this, packageName, null);
        RecyclerView recyclerView = findViewById(R.id.launcher_content);

        if(app == null || recyclerView == null) {
            return;
        }

        LauncherAdapter launcherAdapter = (LauncherAdapter) recyclerView.getAdapter();
        launcherAdapter.addApplication(app, App.getAppComparator(this));
    }

    public void removeApplication(Intent intent) {
        YandexMetrica.reportEvent("User remove application");

        if(intent.getDataString() == null) {
            return;
        }

        String packageName = intent.getDataString().substring("package:".length());
        RecyclerView recyclerView = findViewById(R.id.launcher_content);

        if(recyclerView == null) {
            return;
        }

        LauncherAdapter launcherAdapter = (LauncherAdapter) recyclerView.getAdapter();
        launcherAdapter.removeApplicationByPackageName(packageName);

        new DeleteDesktopAppByPackageName(packageName).execute();
    }
}
