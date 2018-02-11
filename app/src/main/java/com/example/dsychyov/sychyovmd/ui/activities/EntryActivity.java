package com.example.dsychyov.sychyovmd.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import com.example.dsychyov.sychyovmd.R;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import io.fabric.sdk.android.Fabric;

public class EntryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean shouldBeInitialized = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.launcher_not_initialized_key), true);

//        continuousDeploymentIntegrations();

        finish();
        if(shouldBeInitialized) {
            startActivityForClass(WelcomeActivity.class);
        } else {
            startActivityForClass(LauncherActivity.class);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        checkForCrashes();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        unregisterManagers();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregisterManagers();
//    }
//
//    private void continuousDeploymentIntegrations() {
//        Fabric.with(this, new Crashlytics());
//        checkForUpdates();
//    }
//
//    private void checkForUpdates() {
//        UpdateManager.register(this);
//    }
//
//    private void checkForCrashes() {
//        CrashManager.register(this);
//    }
//
//    private void unregisterManagers() {
//        UpdateManager.unregister();
//    }
}
