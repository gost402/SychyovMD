package com.example.dsychyov.sychyovmd.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dsychyov.sychyovmd.ui.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setActivityTheme(this);
        super.onCreate(savedInstanceState);
    }

    protected void startActivityForClass(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
