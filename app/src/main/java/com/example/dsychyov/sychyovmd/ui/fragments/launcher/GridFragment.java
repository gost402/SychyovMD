package com.example.dsychyov.sychyovmd.ui.fragments.launcher;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.ui.OffsetItemDecoration;
import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherAdapter;

import java.util.ArrayList;
import java.util.List;

public class GridFragment extends BaseLauncherFragment {
    public GridFragment() {
    }

    public static GridFragment newInstance(List<App> data, LauncherActivity launcherActivity) {
        GridFragment fragment = new GridFragment();
        fragment.initializeAppsList(data);
        fragment.initializeLauncherActivity(launcherActivity);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        createGridLayout(view);
        return view;
    }

    private void createGridLayout(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.launcher_content);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final boolean isDense = preferences.getBoolean(getString(R.string.launcher_layout_dense_key), false);

        int spanCountResource;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCountResource = isDense ? R.integer.span_count_dense : R.integer.span_count_standard;
        } else {
            spanCountResource = isDense ? R.integer.span_count_dense_land : R.integer.span_count_standard_land;
        }

        final int offset = getResources().getDimensionPixelSize(R.dimen.launcher_icon_margin);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = getResources().getInteger(spanCountResource);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        launcherAdapter = new LauncherAdapter(data, R.layout.launcher_grid_item, launcherActivity);
        recyclerView.setAdapter(launcherAdapter);
    }
}
