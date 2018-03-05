package com.example.dsychyov.sychyovmd.async_tasks.launcher;

import android.os.AsyncTask;
import android.support.annotation.WorkerThread;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

public class DeleteDesktopAppByPackageName extends AsyncTask<Void, Void, Void> {
    private final String packageName;

    public DeleteDesktopAppByPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    @WorkerThread
    protected Void doInBackground(Void... voids) {
        DesktopAppDbHolder
                .getInstance()
                .getDb(LauncherApplication.getInstance())
                .desktopAppsDao()
                .deleteDesktopAppByPackageNameAndUpdatePositions(packageName);

        return null;
    }
}

