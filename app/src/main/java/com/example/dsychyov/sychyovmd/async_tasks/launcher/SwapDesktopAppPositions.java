package com.example.dsychyov.sychyovmd.async_tasks.launcher;

import android.os.AsyncTask;
import android.support.annotation.WorkerThread;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

public class SwapDesktopAppPositions extends AsyncTask<Void, Void, Void> {
    private final int fromId;
    private final int toId;

    public SwapDesktopAppPositions(int fromId, int toId) {
        this.fromId = fromId;
        this.toId= toId;
    }

    @Override
    @WorkerThread
    protected Void doInBackground(Void... voids) {
        DesktopAppDbHolder
                .getInstance()
                .getDb(LauncherApplication.getInstance())
                .desktopAppsDao()
                .swapPositions(fromId, toId);

        return null;
    }
}

