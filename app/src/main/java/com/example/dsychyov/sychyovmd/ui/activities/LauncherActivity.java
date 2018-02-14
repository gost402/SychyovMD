package com.example.dsychyov.sychyovmd.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.listeners.ChangeGridPropertiesSharedPreferencesListener;
import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherAdapter;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.GridFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.ListFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.DesktopFragment;
import com.yandex.metrica.YandexMetrica;

public class LauncherActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ChangeGridPropertiesSharedPreferencesListener changeGridPropertiesSharedPreferencesListener;

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
                    YandexMetrica.reportEvent("User remove application");
                    removeApplication(packageName);
                    break;
                case Intent.ACTION_PACKAGE_ADDED:
                    YandexMetrica.reportEvent("User add application");
                    addApplication(packageName);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YandexMetrica.reportEvent("LauncherActivity OnCreate");

        setContentView(R.layout.activity_launcher);

        initializeNavigationDrawer();
        initializePreferencesListener();
        initializeLauncherFragment();
        registerApplicationsChangesReceiver();
    }

    private void initializeLauncherFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.launcher_fragment_container, new GridFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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

        if (id == R.id.navigation_drawer_desktop_button) {
            YandexMetrica.reportEvent("User choose desktop fragment");
            replaceLauncherFragment(new DesktopFragment());
        } else if (id == R.id.navigation_drawer_grid_layout_button) {
            YandexMetrica.reportEvent("User choose grid fragment");
            replaceLauncherFragment(new GridFragment());
        } else if (id == R.id.navigation_drawer_linear_layout_button) {
            YandexMetrica.reportEvent("User choose linear layout fragment");
            replaceLauncherFragment(new ListFragment());
        } else if (id == R.id.navigation_drawer_settings_button) {
            YandexMetrica.reportEvent("User choose settings");
            startActivityForClass(SettingsActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProfileActivity(View view) {
        startActivityForClass(ProfileActivity.class);
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

    private void addApplication(String packageName) {
        App app = App.getAppFromPackageName(this, packageName, null);
        RecyclerView recyclerView = findViewById(R.id.launcher_content);

        if(app == null || recyclerView == null) {
            return;
        }

        LauncherAdapter launcherAdapter = (LauncherAdapter) recyclerView.getAdapter();
        launcherAdapter.addApplication(app, App.getAppComparator(this));
    }

    private void removeApplication(String packageName) {
        RecyclerView recyclerView = findViewById(R.id.launcher_content);

        if(recyclerView == null) {
            return;
        }

        LauncherAdapter launcherAdapter = (LauncherAdapter) recyclerView.getAdapter();
        launcherAdapter.removeApplicationByPackageName(packageName);
    }

    private void replaceLauncherFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.launcher_fragment_container, fragment).commit();
    }
}
