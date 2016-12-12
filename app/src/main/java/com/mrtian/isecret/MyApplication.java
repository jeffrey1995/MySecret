package com.mrtian.isecret;

import android.app.Application;
import android.content.Context;

/**
 * Created by tianxiying on 16/12/6.
 */
public class MyApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        context = this.getApplicationContext();
        super.onCreate();
    }
}
