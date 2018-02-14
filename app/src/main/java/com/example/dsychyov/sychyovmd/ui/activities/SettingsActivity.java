package com.example.dsychyov.sychyovmd.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.dsychyov.sychyovmd.listeners.ChangeThemeSharedPreferencesListener;
import com.example.dsychyov.sychyovmd.R;
import com.yandex.metrica.YandexMetrica;

public class SettingsActivity extends AppCompatPreferenceActivity {

    ChangeThemeSharedPreferencesListener changeThemeSharedPreferencesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YandexMetrica.reportEvent("SettingsActivity OnCreate");

        setContentView(R.layout.activity_settings);

        initializeToolbar();
        initializePreferencesListener();
    }

    private void initializePreferencesListener() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        changeThemeSharedPreferencesListener = new ChangeThemeSharedPreferencesListener(this);
        preferences.registerOnSharedPreferenceChangeListener(changeThemeSharedPreferencesListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar == null) {
            return;
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
