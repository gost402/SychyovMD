package com.example.dsychyov.sychyovmd;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class LauncherApplication extends Application {

    private static volatile LauncherApplication appInstance = null;

    public static final String API_key = "cbbd4ac5-095a-4762-9165-5251ea7521a7";
    Thread.UncaughtExceptionHandler myUncaughtExceptionHandler;

    public LauncherApplication() {
        appInstance = this;
    }

    public static LauncherApplication getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeYandexMetrica();
        YandexMetrica.reportEvent("Application OnCreate");
    }

    private void initializeYandexMetrica() {
        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder(API_key);
        initializeFirstActivation(configBuilder);
        YandexMetricaConfig extendedConfig = configBuilder.build();
        YandexMetrica.activate(getApplicationContext(), extendedConfig);
        YandexMetrica.enableActivityAutoTracking(this);
        initializeUncaughtExeptionHandler();
    }

    private void initializeFirstActivation(YandexMetricaConfig.Builder configBuilder) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstApplicationLaunch = sharedPreferences.getBoolean(
                getResources().getString(R.string.launcher_not_initialized_key),
                false
        );

        if (!isFirstApplicationLaunch) {
            configBuilder.handleFirstActivationAsUpdate(true);
        }
    }

    private void initializeUncaughtExeptionHandler() {
        myUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable exception) {
                YandexMetrica.reportUnhandledException(exception);
            }
        };

        Thread.setDefaultUncaughtExceptionHandler(myUncaughtExceptionHandler);
    }
}
