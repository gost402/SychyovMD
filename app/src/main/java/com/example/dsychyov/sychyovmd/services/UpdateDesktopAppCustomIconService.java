package com.example.dsychyov.sychyovmd.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateDesktopAppCustomIconService extends Service {

    private class UpdateDesktopAppCustomIconTask implements Runnable {
        private final int id;
        private final byte[] customIcon;

        UpdateDesktopAppCustomIconTask(int id, byte[] customIcon) {
            this.id = id;
            this.customIcon = customIcon;
        }

        @Override
        public void run() {
            DesktopAppDbHolder
                    .getInstance()
                    .getDb(LauncherApplication.getInstance())
                    .desktopAppsDao()
                    .updateDesktopAppCustomIcon(id, customIcon);
        }
    }

    private ExecutorService mExecutor;

    public UpdateDesktopAppCustomIconService() {
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
        byte[] customIcon = intent.getByteArrayExtra("customIcon");

        mExecutor.execute(new UpdateDesktopAppCustomIconTask(id, customIcon));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdownNow();
    }
}
