package com.example.dsychyov.sychyovmd.viewmodel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dsychyov.sychyovmd.LauncherApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InsertDesktopAppService extends Service {

    private class InsertDesktopAppTask implements Runnable {
        String packageName;

        InsertDesktopAppTask(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public void run() {
            DesktopApp desktopApp = new DesktopApp();
            desktopApp.packageName = packageName;

            DesktopAppDbHolder.getInstance().getDb(LauncherApplication.getInstance()).desktopAppsDao()
                    .insert(desktopApp);
        }
    }

    private ExecutorService mExecutor;

    public InsertDesktopAppService() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mExecutor.execute(new InsertDesktopAppTask(intent.getStringExtra("packageName")));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdownNow();
    }
}
