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
import com.example.dsychyov.sychyovmd.ui.activities.LauncherActivity;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;
import com.example.dsychyov.sychyovmd.services.InsertDesktopAppService;
import com.yandex.metrica.YandexMetrica;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull private List<App> apps;
    private final int itemLayoutId;
    private final LauncherActivity launcherActivity;

    public void setApps(@NonNull List<App> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    public LauncherAdapter(@NonNull final List<App> data, final int itemLayoutId, LauncherActivity launcherActivity) {
        apps = data;
        this.itemLayoutId = itemLayoutId;
        this.launcherActivity = launcherActivity;
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
            notifyItemInserted(apps.size() - 1);
        } else {
            int appPosition = Collections.binarySearch(apps, app, comparator);

            if(appPosition < 0) {
                appPosition = -appPosition - 1;
            }

            apps.add(appPosition, app);
            notifyItemInserted(appPosition);
        }
    }

    private void bindGridView(@NonNull final Holder.GridHolder gridHolder, final int position) {
        final App app = apps.get(position);

        final ImageView iconView = gridHolder.itemView.findViewById(R.id.launcher_app_image);
        final TextView nameView = gridHolder.itemView.findViewById(R.id.launcher_app_name);
        final TextView descriptionView = gridHolder.itemView.findViewById(R.id.launcher_app_description);

        iconView.setImageDrawable(app.getIcon());
        nameView.setText(app.getName());

        if(descriptionView != null) {
            descriptionView.setText(app.getDescription());
        }

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
                YandexMetrica.reportEvent("Start another app");
                app.incrementFrequency(view.getContext());
                launcherActivity.updateAppsList();
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
                                YandexMetrica.reportEvent("Deleting app");
                                Intent intent = new Intent(Intent.ACTION_DELETE);
                                intent.setData(Uri.parse("package:" + app.getPackageName()));
                                view.getContext().startActivity(intent);
                                break;
                            }
                            case R.id.app_info: {
                                YandexMetrica.reportEvent("Show app info");
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + app.getPackageName()));
                                view.getContext().startActivity(intent);
                                break;
                            }
                            case R.id.app_frequency: {
                                YandexMetrica.reportEvent("Show app frequency");
                                Toast.makeText(view.getContext(), String.valueOf(app.getFrequency()), Toast.LENGTH_LONG).show();
                                break;
                            }
                            case R.id.app_add_desktop: {
                                YandexMetrica.reportEvent("Add app on desktop");
                                Intent intent = new Intent(view.getContext().getApplicationContext(), InsertDesktopAppService.class);
                                intent.putExtra("value", app.getPackageName());
                                intent.putExtra("type", DesktopApp.Type.APPLICATION);
                                view.getContext().startService(intent);
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
