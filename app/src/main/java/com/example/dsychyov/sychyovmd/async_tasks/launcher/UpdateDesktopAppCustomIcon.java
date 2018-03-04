package com.example.dsychyov.sychyovmd.async_tasks.launcher;

import android.os.AsyncTask;
import android.support.annotation.WorkerThread;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

public class UpdateDesktopAppCustomIcon extends AsyncTask<Void, Void, Void> {
    private final int id;
    private final byte[] customIcon;

    public UpdateDesktopAppCustomIcon(int id, byte[] customIcon) {
        this.id = id;
        this.customIcon = customIcon;
    }

    @Override
    @WorkerThread
    protected Void doInBackground(Void... voids) {
        DesktopAppDbHolder
                .getInstance()
                .getDb(LauncherApplication.getInstance())
                .desktopAppsDao()
                .updateDesktopAppCustomIcon(id, customIcon);

        return null;
    }
}
