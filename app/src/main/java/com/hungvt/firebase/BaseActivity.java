package com.hungvt.firebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Windows7 on 08/10/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int contentViewId = getContentViewId();
        if (contentViewId!=-1){
            setContentView(contentViewId);
            initComponents();
            registerListener();
        }
    }
    public abstract int getContentViewId();
    public abstract void initComponents();
    public abstract void registerListener();
}
