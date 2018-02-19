package com.example.dsychyov.sychyovmd.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(tableName = "app_frequencies")
public class AppFrequency {

    @ColumnInfo(name = "package_name")
    public String packageName;

    public int frequency;
}
