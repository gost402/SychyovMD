package com.example.dsychyov.sychyovmd.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SwapDesktopAppPositionsService extends Service {
    private class SwapDesktopAppPositionsTask implements Runnable {
        private final int fromId;
        private final int toId;

        SwapDesktopAppPositionsTask(int fromId, int toId) {
            this.fromId = fromId;
            this.toId = toId;
        }

        @Override
        public void run() {
            DesktopAppDbHolder
                    .getInstance()
                    .getDb(LauncherApplication.getInstance())
                    .desktopAppsDao()
                    .swapPositions(fromId, toId);
        }
    }

    private ExecutorService mExecutor;

    public SwapDesktopAppPositionsService() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int fromId = intent.getIntExtra("fromId", -1);
        int toId = intent.getIntExtra("toId", -1);

        if(fromId == -1 || toId == -1) {
            return START_NOT_STICKY;
        }

        mExecutor.execute(new SwapDesktopAppPositionsTask(fromId, toId));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdownNow();
    }
}
