package com.example.dsychyov.sychyovmd.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.ui.adapters.WelcomePagerAdapter;

public class WelcomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    WelcomePagerAdapter welcomePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.welcome_view_pager);
        welcomePagerAdapter = new WelcomePagerAdapter(getSupportFragmentManager());
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
        saveActivityResult();

        final Intent intent = new Intent(this, LauncherActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveActivityResult() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final boolean isDark = welcomePagerAdapter.getThemeChooserFragment().isDark();
        final boolean isDense = welcomePagerAdapter.getLayoutChooserFragment().isDense();

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(getString(R.string.launcher_theme_dark_key), isDark);
        editor.putBoolean(getString(R.string.launcher_layout_dense_key), isDense);

        editor.apply();
    }
}
