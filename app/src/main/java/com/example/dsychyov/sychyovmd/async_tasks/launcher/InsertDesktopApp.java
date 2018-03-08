package com.example.dsychyov.sychyovmd.async_tasks.launcher;

import android.os.AsyncTask;
import android.support.annotation.WorkerThread;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

public class InsertDesktopApp extends AsyncTask<Void, Void, Void> {
    private final String name;
    private final String value;
    private final DesktopApp.Type type;

    public InsertDesktopApp(String name, String value, DesktopApp.Type type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    @Override
    @WorkerThread
    protected Void doInBackground(Void... voids) {
        DesktopApp desktopApp = new DesktopApp();

        desktopApp.name = name;
        desktopApp.value = value;
        desktopApp.type = type;

        DesktopAppDbHolder
                .getInstance()
                .getDb(LauncherApplication.getInstance())
                .desktopAppsDao()
                .insertOnLastPosition(desktopApp);

        return null;
    }
}
