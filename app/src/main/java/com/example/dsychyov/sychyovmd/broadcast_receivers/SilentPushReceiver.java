package com.example.dsychyov.sychyovmd.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.dsychyov.sychyovmd.R;
import com.yandex.metrica.push.YandexMetricaPush;

public class SilentPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
        final String showSecretItem = context.getResources().getString(R.string.show_secret_profile_item_key);

        if(payload.equals(showSecretItem)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().putBoolean(showSecretItem, true).apply();
        }
    }
}