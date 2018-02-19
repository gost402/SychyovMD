package com.example.dsychyov.sychyovmd.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeleteDesktopAppByIdService extends Service {

    private class DeleteDesktopAppByIdTask implements Runnable {
        private final int id;

        DeleteDesktopAppByIdTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            DesktopAppDbHolder
                    .getInstance()
                    .getDb(LauncherApplication.getInstance())
                    .desktopAppsDao()
                    .deleteDesktopAppAndUpdatePositions(id);
        }
    }

    private ExecutorService mExecutor;

    public DeleteDesktopAppByIdService() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = intent.getIntExtra("id", 0);

        mExecutor.execute(new DeleteDesktopAppByIdTask(id));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdownNow();
    }
}
