package com.example.dsychyov.sychyovmd.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        if(getContext() == null) {
            return list;
        }

        Drawable browserIcon = getDrawableFromAttribute(getContext(), R.attr.icon_browser);
        Drawable infoIcon = getDrawableFromAttribute(getContext(), R.attr.icon_info);
        Drawable phoneIcon = getDrawableFromAttribute(getContext(), R.attr.icon_phone);
        Drawable emailIcon = getDrawableFromAttribute(getContext(), R.attr.icon_email);

        list.add(new ProfileItem(infoIcon, getString(R.string.author_name), getString(R.string.profile_author), false));
        list.add(new ProfileItem(phoneIcon, "(33) 652 64 09", getString(R.string.profile_phone), false));
        list.add(new ProfileItem(emailIcon, getString(R.string.author_email), getString(R.string.profile_email), false));
        list.add(new ProfileItem(browserIcon, "https://vk.com/denissychyov", getString(R.string.profile_vk), true));
        list.add(new ProfileItem(browserIcon, "https://facebook.com/denis.sychou", getString(R.string.profile_facebook), true));
        list.add(new ProfileItem(browserIcon, "https://steamcommunity.com/id/gost402", getString(R.string.profile_steam), true));
        list.add(new ProfileItem(browserIcon, "https://github.com/gost402", getString(R.string.profile_github), true));

        Activity activity = getActivity();

        if(activity != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            boolean showSecretItem = preferences.getBoolean(
                    getString(R.string.show_secret_profile_item_key),
                    false
            );

            if(showSecretItem) {
                list.add(secretItem(browserIcon));
            }
        }

        return list;
    }

    private ProfileItem secretItem(Drawable icon) {
        return new ProfileItem(
                icon,
                "https://open.spotify.com/user/gost402/playlist/0XqGqJsRYoUG3SOgEwnKql",
                getString(R.string.secret_playlist),
                true
        );
    }

    private Drawable getDrawableFromAttribute(Context context, int id) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[] { id });
        int attributeResourceId = a.getResourceId(0, 0);
        return getResources().getDrawable(attributeResourceId);
    }
}
