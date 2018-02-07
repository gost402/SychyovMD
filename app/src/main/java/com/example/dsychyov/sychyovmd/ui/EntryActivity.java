package com.example.dsychyov.sychyovmd.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.dsychyov.sychyovmd.R;

public class EntryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean shouldBeInitialized = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.launcher_not_initialized_key), true);

        finish();
        if(shouldBeInitialized) {
            startActivityForClass(WelcomeActivity.class);
        } else {
            startActivityForClass(LauncherActivity.class);
        }
    }
}
