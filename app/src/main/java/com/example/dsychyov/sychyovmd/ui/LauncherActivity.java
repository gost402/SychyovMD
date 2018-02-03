package com.example.dsychyov.sychyovmd.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dsychyov.sychyovmd.OffsetItemDecoration;
import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        createGridLayout();
    }


    private void createGridLayout() {
        final RecyclerView recyclerView = findViewById(R.id.launcher_content);
        recyclerView.setHasFixedSize(true);
        final int offset = getResources().getDimensionPixelSize(R.dimen.launcher_icon_margin);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean isDense = preferences.getBoolean(getString(R.string.launcher_layout_dense_key), false);

        int spanCountResource;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCountResource = isDense ? R.integer.span_count_dense : R.integer.span_count_standard;
        } else {
            spanCountResource = isDense ? R.integer.span_count_dense_land : R.integer.span_count_standard_land;
        }

        final int spanCount = getResources().getInteger(spanCountResource);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        final List<Integer> data = generateData();
        final LauncherAdapter launcherAdapter = new LauncherAdapter(data);
        recyclerView.setAdapter(launcherAdapter);
    }

    @NonNull
    private List<Integer> generateData() {
        final List<Integer> colors = new ArrayList<>();
        final Random rnd = new Random();
        for (int i = 0; i < 1000; i++) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colors.add(color);
        }

        return colors;
    }

}
