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
    @Insert
    public abstract void insert(DesktopApp desktopApp);

    @Query("SELECT * FROM desktop_apps")
    public abstract  DesktopApp[] loadAll();

    @Query("SELECT * FROM desktop_apps ORDER BY position")
    public abstract LiveData<List<DesktopApp>> loadDesktopAppsSync();

    @Query("SELECT position FROM desktop_apps ORDER BY position DESC LIMIT 1")
    public abstract Integer lastPosition();

    @Query("DELETE FROM desktop_apps WHERE id = :id")
    public abstract void deleteDesktopAppById(int id);

    @Query("DELETE FROM desktop_apps WHERE value = :packageName AND type = 0")
    public abstract void removeDesktopAppByPackageName(String packageName);

    @Query("UPDATE desktop_apps SET custom_icon = :customIcon WHERE id = :id")
    public abstract void updateDesktopAppCustomIcon(int id, byte[] customIcon);

    @Query("UPDATE desktop_apps SET position = :newPosition WHERE id = :id")
    public abstract void updateDesktopAppPosition(int id, int newPosition);

    @Query("SELECT position FROM desktop_apps WHERE id = :id LIMIT 1")
    public abstract Integer getDesktopAppPosition(int id);

    @Query("SELECT * FROM desktop_apps WHERE id = :id LIMIT 1")
    public abstract DesktopApp getDesktopApp(int id);

    @Query("UPDATE desktop_apps SET position = position - 1 WHERE position > :position")
    public abstract void decrementPositions(int position);

    @Transaction
    public void deleteDesktopAppAndUpdatePositions(int id) {
        Integer position = getDesktopAppPosition(id);
        deleteDesktopAppById(id);
        decrementPositions(position);
    }

    @Transaction
    public void getLastPositionAndInsert(DesktopApp newApp) {
        Integer lastPosition = lastPosition();
        newApp.position = lastPosition == null ? 0 : lastPosition + 1;
        insert(newApp);
    }

    @Transaction
    public void swapPositions(int fromId, int toId) {
        int fromPosition = getDesktopAppPosition(fromId);
        int toPosition = getDesktopAppPosition(toId);

        updateDesktopAppPosition(toId, fromPosition);
        updateDesktopAppPosition(fromId, toPosition);
    }
}
