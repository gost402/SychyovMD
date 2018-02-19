package com.example.dsychyov.sychyovmd.listeners;

import android.content.SharedPreferences;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;

public class RecreateLauncherActivityListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final LauncherActivity activity;

    public RecreateLauncherActivityListener(LauncherActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(activity.getString(R.string.launcher_theme_dark_key)) ||
                key.equals(activity.getString(R.string.launcher_sorting_type_key)) ||
                key.equals(activity.getString(R.string.launcher_layout_dense_key)) ) {
            activity.setRecreateActivity();
        }
    }
}
