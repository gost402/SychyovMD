package com.example.dsychyov.sychyovmd.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;
import com.example.dsychyov.sychyovmd.services.DownloadBackgroundAlarmService;

public class LauncherActivityBroadcastReceiver extends BroadcastReceiver {

    private final LauncherActivity launcherActivity;

    public LauncherActivityBroadcastReceiver(LauncherActivity launcherActivity) {
        this.launcherActivity = launcherActivity;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();

        if (action == null) {
            return;
        }

        switch (action) {
            case Intent.ACTION_PACKAGE_REMOVED:
                launcherActivity.removeApplication(intent);
                break;
            case Intent.ACTION_PACKAGE_ADDED:
                launcherActivity.addApplication(intent);
                break;
            case DownloadBackgroundAlarmService.BROADCAST_ACTION_UPDATE_IMAGE:
                launcherActivity.updateBackgroundImage();
                break;
        }
    }
}
