package com.example.dsychyov.sychyovmd;

import com.yandex.metrica.YandexMetrica;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.internal.bytecode.InstrumentationConfiguration;

public class CustomRunnerWithMoreShadows extends RobolectricTestRunner {
    public CustomRunnerWithMoreShadows(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    public InstrumentationConfiguration createClassLoaderConfig() {
        InstrumentationConfiguration.Builder builder = InstrumentationConfiguration.newBuilder();
        builder.addInstrumentedClass(YandexMetrica.class.getName());
        return builder.build();
    }
}