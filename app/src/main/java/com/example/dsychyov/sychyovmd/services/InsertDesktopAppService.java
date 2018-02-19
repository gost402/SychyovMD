package com.example.dsychyov.sychyovmd.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;
import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InsertDesktopAppService extends Service {

    private class InsertDesktopAppTask implements Runnable {
        private final String value;
        private final String name;
        private final DesktopApp.Type type;

        InsertDesktopAppTask(String value, String name, DesktopApp.Type type) {
            this.value = value;
            this.name = name;
            this.type = type;
        }

        @Override
        public void run() {
            DesktopApp desktopApp = new DesktopApp();

            desktopApp.name = name;
            desktopApp.value = value;
            desktopApp.type = type;

            DesktopAppDbHolder
                    .getInstance()
                    .getDb(LauncherApplication.getInstance())
                    .desktopAppsDao()
                    .getLastPositionAndInsert(desktopApp);
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
        String value = intent.getStringExtra("value");
        String name = intent.getStringExtra("name");
        DesktopApp.Type type = (DesktopApp.Type) intent.getSerializableExtra("type");

        mExecutor.execute(new InsertDesktopAppTask(value, name, type));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdownNow();
    }
}
