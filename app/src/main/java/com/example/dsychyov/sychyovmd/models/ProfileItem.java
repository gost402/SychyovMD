package com.example.dsychyov.sychyovmd.models;

import android.graphics.drawable.Drawable;

public class ProfileItem {

    private Drawable icon;
    private String text;
    private String subtext;

    public ProfileItem(Drawable icon, String text, String subtext) {
        this.icon = icon;
        this.text = text;
        this.subtext = subtext;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public String getSubtext() {
        return subtext;
    }
}
