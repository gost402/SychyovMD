package com.example.dsychyov.sychyovmd.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.ui.adapters.WelcomePagerAdapter;
import com.yandex.metrica.YandexMetrica;

public class WelcomeActivity extends BaseActivity {
    ViewPager viewPager;
    WelcomePagerAdapter welcomePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        YandexMetrica.reportEvent("WelcomeActivity OnCreate");
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initializeWelcomePagerAdapter();
        initializeTabIndicator();
    }

    private void initializeTabIndicator() {
        TabLayout tabLayout = findViewById(R.id.tab_dots);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void initializeWelcomePagerAdapter() {
        welcomePagerAdapter = new WelcomePagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.welcome_view_pager);
        viewPager.setAdapter(welcomePagerAdapter);

        findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(welcomePagerAdapter.getCount() == viewPager.getCurrentItem() + 1) {
                    showLauncherActivity(view);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
            }
        });
    }

    public void showLauncherActivity(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean(getString(R.string.launcher_not_initialized_key), false).apply();

        finish();
        startActivityForClass(LauncherActivity.class);
    }
}
