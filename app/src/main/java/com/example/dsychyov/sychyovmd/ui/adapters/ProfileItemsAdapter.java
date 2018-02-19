package com.example.dsychyov.sychyovmd.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.models.ProfileItem;
import com.yandex.metrica.YandexMetrica;

import java.util.List;

public class ProfileItemsAdapter extends RecyclerView.Adapter<ProfileItemsAdapter.ViewHolder> {

    private final List<ProfileItem> profileInfoList;

    public ProfileItemsAdapter(List<ProfileItem> data) {
        profileInfoList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProfileItem item = profileInfoList.get(position);

        holder.item = item;
        holder.iconView.setImageDrawable(item.getIcon());
        holder.mainTextView.setText(item.getText());
        holder.subTextView.setText(item .getSubtext());

        final Context context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isOpenInBrowser()) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(item.getText()));
                    YandexMetrica.reportEvent("Open: " + item.getText());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconView;
        final TextView mainTextView;
        final TextView subTextView;

        public ProfileItem item;

        ViewHolder(View view) {
            super(view);
            iconView = view.findViewById(R.id.personal_info_icon);
            mainTextView = view.findViewById(R.id.personal_info_main_text);
            subTextView = view.findViewById(R.id.personal_info_sub_text);
        }
    }
}
