package com.example.dsychyov.sychyovmd.viewmodel;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.ui.fragments.ItemTouchHelperAdapter;
import com.yandex.metrica.YandexMetrica;

import java.util.Collections;
import java.util.List;

public class DesktopAppsAdapter extends RecyclerView.Adapter<DesktopAppsAdapter.ViewHolder>
    implements ItemTouchHelperAdapter {

    @Override
    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        YandexMetrica.reportEvent("Swap desktop apps");
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

    private List<DesktopApp> mData;

    public DesktopAppsAdapter(final List<DesktopApp> data) {
        mData = data;
    }

    public void setData(final List<DesktopApp> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.launcher_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DesktopApp desktopApp = mData.get(position);
        App app = App.getAppFromPackageName(holder.itemView.getContext(), desktopApp.packageName, null);

        ImageView imageView = holder.itemView.findViewById(R.id.launcher_app_image);
        TextView textView = holder.itemView.findViewById(R.id.launcher_app_name);

        if (app != null) {
            imageView.setImageDrawable(app.getIcon());
            textView.setText(app.getName());
        }

//        addOnClickListener(desktopApp, holder.itemView);
//        addOnLongClickListener(desktopApp, holder.itemView);
    }

//    private void addOnClickListener(@NonNull final DesktopApp app, @NonNull final View view) {
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = view.getContext().getPackageManager().getLaunchIntentForPackage(app.packageName);
//                if (intent == null) {
//                    return;
//                }
//                YandexMetrica.reportEvent("Start another app from desktop");
//
//                // Increment frequency in app
//                view.getContext().startActivity(intent);
//            }
//        });
//    }
//
//    private void addOnLongClickListener(@NonNull final DesktopApp app, @NonNull final View view) {
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(final View view) {
//                PopupMenu popup = new PopupMenu(view.getContext(), view);
//                popup.inflate(R.menu.desktop_app_context_menu);
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.desktop_app_delete: {
//                                YandexMetrica.reportEvent("Deleting app from desktop");
//                                Intent intent = new Intent(Intent.ACTION_DELETE);
//                                intent.setData(Uri.parse("package:" + app.packageName));
//                                view.getContext().startActivity(intent);
//                                break;
//                            }
//                            default:
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popup.show();
//                return false;
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
