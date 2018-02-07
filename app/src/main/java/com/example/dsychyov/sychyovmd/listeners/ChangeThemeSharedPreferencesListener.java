package com.example.dsychyov.sychyovmd.listeners;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.dsychyov.sychyovmd.R;

public class ChangeThemeSharedPreferencesListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final Activity activity;

    public ChangeThemeSharedPreferencesListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(activity.getString(R.string.launcher_theme_dark_key))) {
            activity.recreate();
        }
    }
}