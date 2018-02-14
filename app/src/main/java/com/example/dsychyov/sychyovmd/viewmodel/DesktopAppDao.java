package com.example.dsychyov.sychyovmd.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DesktopAppDao {
    @Insert
    public void insert(DesktopApp desktopApp);

    @Query("Select * from desktop_apps")
    public DesktopApp[] loadAll();

    @Query("Select * from desktop_apps")
    public LiveData<List<DesktopApp>> loadDesktopAppsSync();
}
