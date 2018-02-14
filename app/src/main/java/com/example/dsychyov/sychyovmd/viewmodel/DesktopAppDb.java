package com.example.dsychyov.sychyovmd.viewmodel;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {DesktopApp.class}, version = 1, exportSchema = false)
public abstract class DesktopAppDb extends RoomDatabase {
    public abstract DesktopAppDao desktopAppsDao();
}
