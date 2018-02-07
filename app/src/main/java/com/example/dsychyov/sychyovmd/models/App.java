package com.example.dsychyov.sychyovmd.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.dsychyov.sychyovmd.dao.PackageFrequenciesDAO;

import java.util.Comparator;

public class App {
    private final Drawable icon;
    private final String name;
    private final String packageName;
    private final long createdAt;
    private int frequency;

    public App(String packageName, String name, Drawable icon, long lastUpdate, int frequency) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.createdAt = lastUpdate;
        this.frequency = frequency;
    }

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

    public static final Comparator<App> nameAscComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return app.name.compareTo(t1.getName());
        }
    };

    public static final Comparator<App> nameDescComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return - app.name.compareTo(t1.getName());
        }
    };

    public static final Comparator<App> createdAtComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return (int) (app.createdAt - t1.getCreatedAt());
        }
    };

    public static final Comparator<App> frequencyComparator = new Comparator<App>() {
        @Override
        public int compare(App app, App t1) {
            return t1.getFrequency() - app.frequency;
        }
    };

    public void incrementFrequency(Context context) {
        PackageFrequenciesDAO packageFrequenciesDAO = new PackageFrequenciesDAO(context);
        packageFrequenciesDAO.insertOrUpdate(packageName, ++frequency);
    }
}
