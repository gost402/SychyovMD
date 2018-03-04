package com.example.dsychyov.sychyovmd;

import android.content.Context;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(YandexMetrica.class)
public class ShadowYandexMetrica {
    @Implementation
    public static void activate(Context context, YandexMetricaConfig config) {
    }
}
