package com.example.dsychyov.sychyovmd.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.dsychyov.sychyovmd.R;

public class Utils
{
    public static void setActivityTheme(Activity activity)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isDark = preferences.getBoolean(activity.getResources().getString(R.string.launcher_theme_dark_key), false);

        activity.setTheme(isDark ? R.style.AppTheme_Dark : R.style.AppTheme);
    }
}
