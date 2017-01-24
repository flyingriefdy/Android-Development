package com.example.hazie.flickrapp;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by hazie on 11/4/2016.
 */
public class BaseActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    protected Toolbar activateToolBar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    protected Toolbar activateToolBarWithHomeEnabled() {
        activateToolBar();
        if (mToolbar == null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return mToolbar;
    }
}
