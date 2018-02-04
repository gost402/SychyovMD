package com.example.dsychyov.sychyovmd.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dsychyov.sychyovmd.ui.OffsetItemDecoration;
import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.models.ProfileItem;
import com.example.dsychyov.sychyovmd.ui.adapters.ProfileItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileItemsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_items_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setHasFixedSize(true);
            final int offset = getResources().getDimensionPixelSize(R.dimen.launcher_icon_margin);
            recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ProfileItemsAdapter(prepareProfileInfo()));
        }

        return view;
    }

    private List<ProfileItem> prepareProfileInfo() {
        List<ProfileItem> list = new ArrayList<>();

        list.add(new ProfileItem(getResources().getDrawable(android.R.drawable.ic_dialog_info), getString(R.string.author_name), ""));
        list.add(new ProfileItem(getResources().getDrawable(android.R.drawable.ic_menu_call), "(33) 652 64 09", ""));
        list.add(new ProfileItem(getResources().getDrawable(android.R.drawable.ic_dialog_email), getString(R.string.author_email), "Email"));
        list.add(new ProfileItem(getResources().getDrawable(android.R.drawable.ic_menu_share), "vk.com/denissychyov", "VK"));
        list.add(new ProfileItem(getResources().getDrawable(android.R.drawable.ic_menu_share), "facebook.com/denis.sychou", "Facebook"));
        list.add(new ProfileItem(getResources().getDrawable(android.R.drawable.ic_menu_share), "steamcommunity.com/id/gost402", "Steam"));
        list.add(new ProfileItem(getResources().getDrawable(android.R.drawable.ic_menu_share), "github.com/gost402", "Github"));

        return list;
    }
}
