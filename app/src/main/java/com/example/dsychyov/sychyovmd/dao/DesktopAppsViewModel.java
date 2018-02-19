package com.example.dsychyov.sychyovmd.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.dsychyov.sychyovmd.dao.DesktopAppDbHolder;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

import java.util.List;

public class DesktopAppsViewModel extends AndroidViewModel {

    @NonNull
    private LiveData<List<DesktopApp>> desktopApps;

    public DesktopAppsViewModel(@NonNull Application application) {
        super(application);
        desktopApps = DesktopAppDbHolder.getInstance().getDb(this.getApplication())
                .desktopAppsDao().loadDesktopAppsSync();
    }

    @NonNull
    public LiveData<List<DesktopApp>> getDesktopAppsLiveData() {
        return desktopApps;
    }
}
