package com.example.dsychyov.sychyovmd.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

@Database(entities = {DesktopApp.class}, version = 1, exportSchema = false)
public abstract class DesktopAppDb extends RoomDatabase {
    public abstract DesktopAppDao desktopAppsDao();
}
