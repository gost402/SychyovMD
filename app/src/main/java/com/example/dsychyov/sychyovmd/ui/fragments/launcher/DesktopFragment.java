package com.example.dsychyov.sychyovmd.ui.fragments.launcher;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.ui.OffsetItemDecoration;
import com.example.dsychyov.sychyovmd.ui.fragments.SimpleItemTouchHelperCallback;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopAppsAdapter;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopAppsViewModel;
import com.thesurix.gesturerecycler.GestureManager;

import java.util.ArrayList;
import java.util.List;

public class DesktopFragment extends BaseLauncherFragment implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;

    public DesktopFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.desktop_recycler_view);

        addLayoutManager(recyclerView);
        addAdapter(recyclerView );

        return view;
    }

    private void addAdapter(RecyclerView recyclerView) {
        final DesktopAppsAdapter mAdapter = new DesktopAppsAdapter(new ArrayList<DesktopApp>());
        recyclerView.setAdapter(mAdapter);

        DesktopAppsViewModel desktopAppsViewModel = ViewModelProviders.of(this).get(DesktopAppsViewModel.class);

        desktopAppsViewModel.getDesktopAppsLiveData().observe(this, new Observer<List<DesktopApp>>() {
            @Override
            public void onChanged(@Nullable final List<DesktopApp> apps) {
                mAdapter.setData(apps);
                mAdapter.notifyDataSetChanged();
            }
        });
//
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);

        GestureManager gestureManager = new GestureManager.Builder(recyclerView)
                .setSwipeEnabled(true)
                .setLongPressDragEnabled(true)
                .setSwipeFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                .setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
                .build();
    }

    private void addLayoutManager(RecyclerView recyclerView) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        final boolean isDense = preferences.getBoolean(getString(R.string.launcher_layout_dense_key), false);
//
//        int spanCountResource;
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            spanCountResource = isDense ? R.integer.span_count_dense : R.integer.span_count_standard;
//        } else {
//            spanCountResource = isDense ? R.integer.span_count_dense_land : R.integer.span_count_standard_land;
//        }
//
//        final int offset = getResources().getDimensionPixelSize(R.dimen.launcher_icon_margin);
//        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));
//
//        final int spanCount = getResources().getInteger(spanCountResource);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
