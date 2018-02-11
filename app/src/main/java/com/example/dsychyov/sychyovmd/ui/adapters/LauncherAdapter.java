package com.example.dsychyov.sychyovmd.ui.adapters;

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

import com.example.dsychyov.sychyovmd.ui.Holder;
import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.models.App;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull private final List<App> apps;
    private final int itemLayoutId;

    public LauncherAdapter(@NonNull final List<App> data, final int itemLayoutId) {
        apps = data;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        return new Holder.GridHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        bindGridView((Holder.GridHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public void removeApplicationByPackageName(String packageName) {
        int index = 0;
        for (Iterator<App> iter = apps.listIterator(); iter.hasNext(); ++index) {
            App app = iter.next();
            if (app.getPackageName().equals(packageName)) {
                iter.remove();
                notifyItemRemoved(index);
                return;
            }
        }
    }

    public void addApplication(App app, Comparator<App> comparator) {
        if(comparator == null) {
            apps.add(app);
        } else {
            int appPosition = Collections.binarySearch(apps, app, comparator);

            if(appPosition < 0) {
                appPosition = -appPosition - 1;
            }

            apps.add(appPosition, app);
        }

        notifyDataSetChanged();
    }

    private void bindGridView(@NonNull final Holder.GridHolder gridHolder, final int position) {
        final App app = apps.get(position);

        ImageView iconView = gridHolder.itemView.findViewById(R.id.launcher_app_image);
        final TextView nameView = gridHolder.itemView.findViewById(R.id.launcher_app_name);

        iconView.setImageDrawable(app.getIcon());
        nameView.setText(app.getName());

        addOnClickListener(app, gridHolder.itemView);
        addOnLongClickListener(app, gridHolder.itemView);
    }

    private void addOnClickListener(@NonNull final App app, @NonNull final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = view.getContext().getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                if (intent == null) {
                    return;
                }

                app.incrementFrequency(view.getContext());
                view.getContext().startActivity(intent);
            }
        });
    }

    private void addOnLongClickListener(@NonNull final App app, @NonNull final View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.app_context_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.app_delete: {
                                Intent intent = new Intent(Intent.ACTION_DELETE);
                                intent.setData(Uri.parse("package:" + app.getPackageName()));
                                view.getContext().startActivity(intent);
                                break;
                            }
                            case R.id.app_info: {
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + app.getPackageName()));
                                view.getContext().startActivity(intent);
                                break;
                            }
                            case R.id.app_frequency: {
                                Toast.makeText(view.getContext(), String.valueOf(app.getFrequency()), Toast.LENGTH_LONG).show();
                                break;
                            }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }
}
