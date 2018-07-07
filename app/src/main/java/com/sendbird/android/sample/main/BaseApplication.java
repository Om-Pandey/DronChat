package com.sendbird.android.sample.main;


import android.app.Application;

import com.sendbird.android.SendBird;
import com.sendbird.android.sample.utils.PreferenceUtils;

public class BaseApplication extends Application {

    private static final String APP_ID = "2245A07B-B3A4-4E21-921E-3323646B2C92"; // US-1 Demo
    public static final String VERSION = "3.0.40";

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(getApplicationContext());

        SendBird.init(APP_ID, getApplicationContext());
    }
}
