package com.example.dsychyov.sychyovmd.ui.fragments.launcher;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;
import com.example.dsychyov.sychyovmd.ui.adapters.LauncherAdapter;

import java.util.List;

public class ListFragment extends BaseLauncherFragment {
    public ListFragment() {
    }

    public static ListFragment newInstance(List<App> list, LauncherActivity launcherActivity) {
        ListFragment fragment = new ListFragment();
        fragment.initializeAppsList(list);
        fragment.initializeLauncherActivity(launcherActivity);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        createLinearLayout(view);
        return view;
    }

    private void createLinearLayout(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.launcher_content);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        launcherAdapter = new LauncherAdapter(data, R.layout.launcher_list_item, launcherActivity);
        recyclerView.setAdapter(launcherAdapter);
    }
}
