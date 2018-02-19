package com.example.dsychyov.sychyovmd.ui.adapters;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.models.App;
import com.example.dsychyov.sychyovmd.services.DeleteDesktopAppByIdService;
import com.example.dsychyov.sychyovmd.services.SwapDesktopAppPositionsService;
import com.example.dsychyov.sychyovmd.ui.fragments.ItemTouchHelperAdapter;
import com.example.dsychyov.sychyovmd.ui.fragments.launcher.DesktopFragment;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;
import com.yandex.metrica.YandexMetrica;

import java.util.Collections;
import java.util.List;

public class DesktopAppsAdapter extends RecyclerView.Adapter<DesktopAppsAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    public static final int PICK_IMAGE = 2014;

    private boolean isMove;

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    @Override
    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        DesktopApp fromApp = mData.get(fromPosition);
        DesktopApp toApp = mData.get(toPosition);

        YandexMetrica.reportEvent("Swap desktop apps");
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        if(fragmentActivity != null && fragmentActivity.getContext() != null) {
            Intent intent = new Intent(fragmentActivity.getContext(), SwapDesktopAppPositionsService.class);
            intent.putExtra("fromId", fromApp.id);
            intent.putExtra("toId", toApp.id);
            fragmentActivity.getContext().startService(intent);
        }

        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(final View itemView) {
            super(itemView);
        }
    }

    private List<DesktopApp> mData;
    private DesktopFragment fragmentActivity;

    public DesktopAppsAdapter(final List<DesktopApp> data, DesktopFragment fragmentActivity) {
        mData = data;
        this.fragmentActivity = fragmentActivity;
    }

    public void setData(final List<DesktopApp> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.launcher_grid_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DesktopApp desktopApp = mData.get(position);
        ImageView imageView = holder.itemView.findViewById(R.id.launcher_app_image);
        TextView textView = holder.itemView.findViewById(R.id.launcher_app_name);

        switch (desktopApp.type) {
            case APPLICATION:
                createApplicationDesktopApp(holder, desktopApp, imageView, textView);
                break;
            case URI:
                createUriDesktopApp(holder, desktopApp, imageView, textView);
                break;
            case CONTACT:
                createContactDesktopApp(holder, desktopApp, imageView, textView);
                break;
        }

        if(isMove) {
            addDesktopAppAnimation(holder);
        } else {
            addClickListeners(holder, desktopApp);
        }
    }

    private void addClickListeners(ViewHolder holder, DesktopApp desktopApp) {
        addOnClickListener(desktopApp, holder.itemView);
        addOnLongClickListener(desktopApp, holder.itemView);
    }

    private void addDesktopAppAnimation(ViewHolder holder) {
        new AnimationUtils();
        Animation controller = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.shake);
        controller.setRepeatCount(Animation.INFINITE);
        controller.setRepeatMode(Animation.RELATIVE_TO_PARENT);
        holder.itemView.startAnimation(controller);
        holder.itemView.setOnClickListener(null);
        holder.itemView.setOnLongClickListener(null);
    }

    private void createApplicationDesktopApp(ViewHolder holder, DesktopApp desktopApp, ImageView imageView, TextView textView) {
        App app = App.getAppFromPackageName(holder.itemView.getContext(), desktopApp.value, null);
        if (app != null) {
            imageView.setImageDrawable(app.getIcon());
            textView.setText(app.getName());
        }
    }

    private void createContactDesktopApp(ViewHolder holder, DesktopApp desktopApp, ImageView imageView, TextView textView) {
        imageView.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.author_circle));
        textView.setText(desktopApp.name);
    }

    private void createUriDesktopApp(ViewHolder holder, DesktopApp desktopApp, ImageView imageView, TextView textView) {
        if(desktopApp.customIcon != null) {
            Drawable customIcon = new BitmapDrawable(
                    holder.itemView.getResources(),
                    BitmapFactory.decodeByteArray(desktopApp.customIcon, 0, desktopApp.customIcon.length)
            );
            imageView.setImageDrawable(customIcon);
        } else {
            imageView.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.author_circle));
        }

        textView.setText(desktopApp.name);
    }

    private void addOnClickListener(@NonNull final DesktopApp app, @NonNull final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (app.type) {
                    case APPLICATION:
                        openApplication(view, app.value);
                        break;
                    case URI:
                        openUri(view, app.value);
                        break;
                    case CONTACT:
                        openContact(view, app.value);
                        break;
                }
            }
        });
    }

    private void openContact(View view, String contactUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactUri));
        intent.setData(uri);
        view.getContext().startActivity(intent);
    }

    private void openUri(View view, String uri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        view.getContext().startActivity(intent);
    }

    private void openApplication(View view, String packageName) {
        Intent intent = view.getContext().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return;
        }

        YandexMetrica.reportEvent("Start another app from desktop");
        view.getContext().startActivity(intent);
    }

    private void addOnLongClickListener(@NonNull final DesktopApp app, @NonNull final View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.desktop_app_context_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.desktop_app_remove: {
                                removeDesktopApp(view, app);
                                break;
                            }
                            case R.id.desktop_app_add_icon: {
                                addDesktopAppCustomIcon(app);
                                break;
                            }
                        }
                        return false;
                    }
                });

                popup.show();
                return false;
            }
        });
    }

    private void addDesktopAppCustomIcon(@NonNull DesktopApp app) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        fragmentActivity.setToBeIconPickedDesktopAppId(app.id);
        fragmentActivity.startActivityForResult(pickPhoto , PICK_IMAGE);
    }

    private void removeDesktopApp(View view, @NonNull DesktopApp app) {
        Intent intent = new Intent(
                view.getContext().getApplicationContext(),
                DeleteDesktopAppByIdService.class
        );
        intent.putExtra("id", app.id);
        view.getContext().startService(intent);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
