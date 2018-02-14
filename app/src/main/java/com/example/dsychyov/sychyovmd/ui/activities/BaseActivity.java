package com.example.dsychyov.sychyovmd.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dsychyov.sychyovmd.LauncherApplication;
import com.example.dsychyov.sychyovmd.ui.Utils;
import com.yandex.metrica.YandexMetrica;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setActivityTheme(this);
        YandexMetrica.reportEvent(
                "Base Activity orientation" + String.valueOf(getResources().getConfiguration().orientation)
        );

        super.onCreate(savedInstanceState);
    }

    protected void startActivityForClass(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.onResumeActivity(this);
        YandexMetrica.getReporter(this, LauncherApplication.API_key).onResumeSession();
    }

    @Override
    protected void onPause() {
        YandexMetrica.onPauseActivity(this);
        YandexMetrica.getReporter(this, LauncherApplication.API_key).onPauseSession();
        super.onPause();
    }

}
