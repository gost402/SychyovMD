package com.example.dsychyov.sychyovmd.ui.fragments.launcher;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.Utils;
import com.example.dsychyov.sychyovmd.async_tasks.launcher.InsertDesktopApp;
import com.example.dsychyov.sychyovmd.async_tasks.launcher.UpdateDesktopAppCustomIcon;
import com.example.dsychyov.sychyovmd.dao.DesktopAppsViewModel;
import com.example.dsychyov.sychyovmd.ui.OffsetItemDecoration;
import com.example.dsychyov.sychyovmd.ui.adapters.DesktopAppsAdapter;
import com.example.dsychyov.sychyovmd.ui.fragments.AddUrlFragment;
import com.example.dsychyov.sychyovmd.ui.fragments.SimpleItemTouchHelperCallback;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;
import com.github.clans.fab.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DesktopFragment extends BaseLauncherFragment implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;
    private Integer toBeIconPickedDesktopAppId;

    private final int PICK_CONTACT = 2015;

    public void setToBeIconPickedDesktopAppId(Integer toBeIconPickedDesktopAppId) {
        this.toBeIconPickedDesktopAppId = toBeIconPickedDesktopAppId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.desktop_recycler_view);

        addLayoutManager(recyclerView);
        addAdapter(recyclerView);
        recyclerView.setHasFixedSize(true);

        initializeFloatActionButtons(view);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_CONTACT:
                pickContact(data);
                break;
            case DesktopAppsAdapter.PICK_IMAGE:
                pickIconForDesktopApp(data);
                break;
        }
    }

    private void initializeFloatActionButtons(View view) {
        FloatingActionButton addContactButton = view.findViewById(R.id.desktop_add_contact);
        FloatingActionButton addUrlButton = view.findViewById(R.id.desktop_add_url);

        if(getActivity() == null) {
            return;
        }

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });

        addUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View clickView) {
                DialogFragment fragment = new AddUrlFragment();
                fragment.show(getActivity().getSupportFragmentManager(), "addUrl");
            }
        });
    }

    private void pickContact(Intent data) {
        if(getContext() == null) {
            return;
        }

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri contactUri = data.getData();

        if(contentResolver == null || contactUri == null) {
            return;
        }

        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);

        if (cursor == null) {
            return;
        }

        cursor.moveToFirst();
        int nameId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        String name = cursor.getString(nameId);
        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        cursor.close();

        new InsertDesktopApp(name, contactId, DesktopApp.Type.CONTACT).execute();
    }

    private void pickIconForDesktopApp(Intent data) {
        if(getContext() == null || toBeIconPickedDesktopAppId == null) {
            return;
        }

        Uri selectedImage = data.getData();

        // TODO: Do something with it, disgusting
        try {
            UpdateDesktopAppCustomIcon task = new UpdateDesktopAppCustomIcon(
                    toBeIconPickedDesktopAppId,
                    Utils.getByteArrayFromImage(getContext(), selectedImage)
            );
            toBeIconPickedDesktopAppId = null;
            task.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAdapter(RecyclerView recyclerView) {
        final DesktopAppsAdapter mAdapter = new DesktopAppsAdapter(new ArrayList<DesktopApp>(), this);
        recyclerView.setAdapter(mAdapter);

        DesktopAppsViewModel desktopAppsViewModel = ViewModelProviders.of(this).get(DesktopAppsViewModel.class);

        desktopAppsViewModel.getDesktopAppsLiveData().observe(this, new Observer<List<DesktopApp>>() {
            @Override
            public void onChanged(@Nullable final List<DesktopApp> apps) {
                if(mAdapter.isMove()) {
                    return;
                }

                mAdapter.setData(apps);
                mAdapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void addLayoutManager(RecyclerView recyclerView) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final boolean isDense = preferences.getBoolean(getString(R.string.launcher_layout_dense_key), false);

        int spanCountResource;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCountResource = isDense ? R.integer.span_count_dense : R.integer.span_count_standard;
        } else {
            spanCountResource = isDense ? R.integer.span_count_dense_land : R.integer.span_count_standard_land;
        }

        final int offset = getResources().getDimensionPixelSize(R.dimen.launcher_icon_margin);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = getResources().getInteger(spanCountResource);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
