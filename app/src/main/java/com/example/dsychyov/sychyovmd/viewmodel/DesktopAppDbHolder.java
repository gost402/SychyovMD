package com.example.dsychyov.sychyovmd.viewmodel;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

public class DesktopAppDbHolder {

    private static final DesktopAppDbHolder ourInstance = new DesktopAppDbHolder();

    public static DesktopAppDbHolder getInstance() {
        return ourInstance;
    }

    private static final String DATABASE_NAME = "main";

    private volatile DesktopAppDb mCalculationResultDb;

    private DesktopAppDbHolder() {
    }

    public DesktopAppDb getDb(@NonNull Context context) {
        if (mCalculationResultDb == null) {
            synchronized (this) {
                if (mCalculationResultDb == null) {
                    mCalculationResultDb =
                            Room.databaseBuilder(context, DesktopAppDb.class, DATABASE_NAME).build();
                }
            }
        }
        return mCalculationResultDb;
    }
}
