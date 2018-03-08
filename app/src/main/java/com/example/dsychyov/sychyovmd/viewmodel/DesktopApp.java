package com.example.dsychyov.sychyovmd.viewmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.dsychyov.sychyovmd.utils.Converters;

@Entity(tableName = "desktop_apps", indices = { @Index(value = "position", unique = true) })
public class DesktopApp {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo
    public Integer position;

    public String value;

    public String name;

    @ColumnInfo(name = "custom_icon")
    public byte[] customIcon;

    @TypeConverters(Converters.class)
    public Type type;

    public enum Type {
        APPLICATION(0),
        URI(1),
        CONTACT(2);

        private int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
