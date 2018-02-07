package com.example.dsychyov.sychyovmd.listeners;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.dsychyov.sychyovmd.R;

public class ChangeGridPropertiesSharedPreferencesListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final Activity activity;

    public ChangeGridPropertiesSharedPreferencesListener(Activity activity) {
        this.activity = activity;
    }

    // Do not recreate on recycle view changes
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(activity.getString(R.string.launcher_theme_dark_key)) ||
                key.equals(activity.getString(R.string.launcher_sorting_type_key)) ||
                key.equals(activity.getString(R.string.launcher_layout_dense_key)) ) {
            activity.recreate();
        }
    }
}
