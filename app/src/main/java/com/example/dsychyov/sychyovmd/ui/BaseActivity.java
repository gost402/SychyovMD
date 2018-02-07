package com.example.dsychyov.sychyovmd.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
