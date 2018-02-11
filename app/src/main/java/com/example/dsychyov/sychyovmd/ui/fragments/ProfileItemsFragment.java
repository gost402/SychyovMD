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

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ProfileItemsAdapter(prepareProfileInfo()));
        }

        return view;
    }

    private List<ProfileItem> prepareProfileInfo() {
        List<ProfileItem> list = new ArrayList<>();

        list.add(new ProfileItem(getResources().getDrawable(R.drawable.ic_action_info), getString(R.string.author_name), getString(R.string.profile_author)));
        list.add(new ProfileItem(getResources().getDrawable(R.drawable.ic_action_phone), "(33) 652 64 09", getString(R.string.profile_phone)));
        list.add(new ProfileItem(getResources().getDrawable(R.drawable.ic_action_email), getString(R.string.author_email), getString(R.string.profile_email)));
        list.add(new ProfileItem(getResources().getDrawable(R.drawable.ic_action_browser), "vk.com/denissychyov", getString(R.string.profile_vk)));
        list.add(new ProfileItem(getResources().getDrawable(R.drawable.ic_action_browser), "facebook.com/denis.sychou", getString(R.string.profile_facebook)));
        list.add(new ProfileItem(getResources().getDrawable(R.drawable.ic_action_browser), "steamcommunity.com/id/gost402", getString(R.string.profile_steam)));
        list.add(new ProfileItem(getResources().getDrawable(R.drawable.ic_action_browser), "github.com/gost402", getString(R.string.profile_github)));

        return list;
    }
}
