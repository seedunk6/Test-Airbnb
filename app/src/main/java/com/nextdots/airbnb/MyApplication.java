package com.nextdots.airbnb;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.devs.acr.AutoErrorReporter;
import com.nextdots.airbnb.models.Favorite;
import com.orm.SugarContext;

/**
 * Created by Mari on 7/12/2016.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AutoErrorReporter.get(this)
                .setEmailAddresses("yourdeveloper@gmail.com")
                .setEmailSubject("Auto Crash Report")
                .start();

        SugarContext.init(this);
        Favorite.findById(Favorite.class,(long)1);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
