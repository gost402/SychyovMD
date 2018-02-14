package com.example.dsychyov.sychyovmd.viewmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "desktop_apps")
public class DesktopApp {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public Integer position;

    @ColumnInfo(name = "package_name")
    public String packageName;
}
