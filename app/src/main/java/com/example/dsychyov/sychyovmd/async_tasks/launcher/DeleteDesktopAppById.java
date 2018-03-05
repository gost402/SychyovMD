package com.example.dsychyov.sychyovmd.async_tasks.launcher;

import android.os.AsyncTask;
import android.support.annotation.WorkerThread;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

public class DeleteDesktopAppById extends AsyncTask<Void, Void, Void> {
    private final int id;

    public DeleteDesktopAppById(int id) {
        this.id = id;
    }

    @Override
    @WorkerThread
    protected Void doInBackground(Void... voids) {
        DesktopAppDbHolder
                .getInstance()
                .getDb(LauncherApplication.getInstance())
                .desktopAppsDao()
                .deleteDesktopAppByIdAndUpdatePositions(id);

        return null;
    }
}

