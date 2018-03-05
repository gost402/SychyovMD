package com.example.dsychyov.sychyovmd.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

import java.util.List;

@Dao
public abstract class DesktopAppDao {
    @Query("SELECT * FROM desktop_apps ORDER BY id")
    public abstract List<DesktopApp> loadAll();

    @Query("SELECT * FROM desktop_apps ORDER BY position")
    public abstract LiveData<List<DesktopApp>> loadDesktopAppsSync();

    @Query("UPDATE desktop_apps SET custom_icon = :customIcon WHERE id = :id")
    public abstract void updateDesktopAppCustomIcon(int id, byte[] customIcon);

    @Transaction
    public void deleteDesktopAppByIdAndUpdatePositions(int id) {
        Integer position = getDesktopAppPositionById(id);
        deleteDesktopAppById(id);
        decrementPositions(position);
    }

    @Transaction
    public void deleteDesktopAppByPackageNameAndUpdatePositions(String packageName) {
        Integer position = getDesktopAppPositionByPackageName(packageName);

        if(position == null) {
            return;
        }

        deleteDesktopAppByPackageName(packageName);
        decrementPositions(position);
    }

    @Transaction
    public void insertOnLastPosition(DesktopApp newApp) {
        Integer lastPosition = lastPosition();
        newApp.position = lastPosition == null ? 0 : lastPosition + 1;
        insert(newApp);
    }

    @Transaction
    public void swapPositions(int fromId, int toId) {
        int fromPosition = getDesktopAppPositionById(fromId);
        int toPosition = getDesktopAppPositionById(toId);

        Integer lastPosition = lastPosition();
        int tempPosition = lastPosition + 1;

        updateDesktopAppPosition(toId, tempPosition);
        updateDesktopAppPosition(fromId, toPosition);
        updateDesktopAppPosition(toId, fromPosition);
    }

    @Insert
    protected abstract void insert(DesktopApp desktopApp);

    @Query("SELECT position FROM desktop_apps ORDER BY position DESC LIMIT 1")
    protected abstract Integer lastPosition();

    @Query("SELECT position FROM desktop_apps WHERE id = :id LIMIT 1")
    protected abstract Integer getDesktopAppPositionById(int id);

    @Query("SELECT position FROM desktop_apps WHERE value = :packageName AND type = 0 LIMIT 1")
    protected abstract Integer getDesktopAppPositionByPackageName(String packageName);

    @Query("UPDATE desktop_apps SET position = position - 1 WHERE position > :position")
    protected abstract void decrementPositions(int position);

    @Query("UPDATE desktop_apps SET position = :newPosition WHERE id = :id")
    protected abstract void updateDesktopAppPosition(int id, int newPosition);

    @Query("DELETE FROM desktop_apps WHERE id = :id")
    protected abstract void deleteDesktopAppById(int id);

    @Query("DELETE FROM desktop_apps WHERE value = :packageName AND type = 0")
    protected abstract void deleteDesktopAppByPackageName(String packageName);
}
