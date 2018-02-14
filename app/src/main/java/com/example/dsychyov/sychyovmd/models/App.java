package com.example.dsychyov.sychyovmd.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.dao.PackageFrequenciesDAO;
import com.example.dsychyov.sychyovmd.ui.Utils;
import com.yandex.metrica.YandexMetrica;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class App {
    private Drawable icon;
    private String name;
    private String packageName;
    private String description;
    private long createdAt;
    private int frequency;

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    private long getCreatedAt() {
        return createdAt;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getDescription() {
        return description;
    }

    private static final Comparator<App> nameAscComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return app.name.compareTo(t1.getName());
        }
    };

    private static final Comparator<App> nameDescComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return - app.name.compareTo(t1.getName());
        }
    };

    private static final Comparator<App> createdAtComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return (int) (app.createdAt - t1.getCreatedAt());
        }
    };

    private static final Comparator<App> frequencyComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return t1.getFrequency() - app.frequency;
        }
    };

    public void incrementFrequency(Context context) {
        PackageFrequenciesDAO packageFrequenciesDAO = new PackageFrequenciesDAO(context);
        packageFrequenciesDAO.insertOrUpdate(packageName, ++frequency);
        description = getAppDescription(context, packageName, createdAt);
    }

    @Nullable
    public static App getAppFromPackageName(Context context, String packageName, List<Pair<String, Integer>> packageFrequencies) {
        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);

            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                App app = new App();

                app.packageName = packageName;
                app.icon = packageManager.getApplicationIcon(appInfo);
                app.createdAt = packageManager.getPackageInfo(appInfo.packageName, 0).firstInstallTime;
                app.name = (String) packageManager.getApplicationLabel(appInfo);
                app.description = getAppDescription(context, packageName, app.createdAt);

                if(packageFrequencies != null) {
                    for (Pair<String, Integer> packageFrequency : packageFrequencies) {
                        if(appInfo.packageName.equals(packageFrequency.first) && packageFrequency.second != null) {
                            app.frequency = packageFrequency.second;
                            break;
                        }
                    }
                }

                return app;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return null;
    }

    private static String getAppDescription(Context context, String packageName, long createdAt) {
        String formattedDate = Utils.formatDate(context, createdAt);
        Locale locale = Utils.getCurrentLocale(context);

        return String.format(locale, "%s: %s\n", context.getString(R.string.description_created_at), formattedDate) +
                String.format(locale, "%s: %s", context.getString(R.string.description_package_name), packageName);
    }

    public static Comparator<App> getAppComparator(Context context) {
        Comparator<App> comparator = null;

        String sortType = getSortType(context);
        YandexMetrica.reportEvent("Sort type: " + sortType);

        switch (sortType) {
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

    private static String getSortType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.launcher_sorting_type_key), "Default");
    }
}
