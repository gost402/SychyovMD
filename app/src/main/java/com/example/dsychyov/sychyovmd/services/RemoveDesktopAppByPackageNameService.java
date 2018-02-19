package com.example.dsychyov.sychyovmd.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoveDesktopAppByPackageNameService extends Service {

    private class RemoveDesktopAppByPackageNameTask implements Runnable {
        private final String packageName;

        RemoveDesktopAppByPackageNameTask(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public void run() {
            DesktopAppDbHolder
                    .getInstance()
                    .getDb(LauncherApplication.getInstance())
                    .desktopAppsDao()
                    .removeDesktopAppByPackageName(packageName);
        }
    }

    private ExecutorService mExecutor;

    public RemoveDesktopAppByPackageNameService() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String packageName = intent.getStringExtra("packageName");

        if(packageName == null) {
            return START_NOT_STICKY;
        }

        mExecutor.execute(new RemoveDesktopAppByPackageNameTask(packageName));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdownNow();
    }
}
