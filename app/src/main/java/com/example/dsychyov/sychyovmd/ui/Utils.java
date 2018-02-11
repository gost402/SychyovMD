package com.example.dsychyov.sychyovmd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.dsychyov.sychyovmd.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils
{
    public static void setActivityTheme(Activity activity)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isDark = preferences.getBoolean(activity.getResources().getString(R.string.launcher_theme_dark_key), false);

        activity.setTheme(isDark ? R.style.AppTheme_Dark : R.style.AppTheme);
    }

    public static String formatDate(Context context, long lDate) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(lDate);
    }

    public static Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            return context.getResources().getConfiguration().locale;
        }
    }
}
