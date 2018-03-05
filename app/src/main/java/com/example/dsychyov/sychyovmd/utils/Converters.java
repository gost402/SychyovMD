package com.example.dsychyov.sychyovmd.utils;

import android.arch.persistence.room.TypeConverter;

import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public static DesktopApp.Type toType(int typeCode) {
        List<DesktopApp.Type> types = Arrays.asList(DesktopApp.Type.APPLICATION, DesktopApp.Type.URI, DesktopApp.Type.CONTACT);

        for(DesktopApp.Type type : types) {
            if(typeCode == type.getCode()) {
                return type;
            }
        }

        return null;
    }

    @TypeConverter
    public static int toInteger(DesktopApp.Type status) {
        return status.getCode();
    }
}