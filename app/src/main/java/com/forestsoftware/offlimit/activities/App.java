package com.forestsoftware.offlimit.activities;

import android.app.Application;

import com.forestsoftware.offlimit.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/exan_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
