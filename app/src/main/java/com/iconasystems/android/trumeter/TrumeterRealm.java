package com.iconasystems.android.trumeter;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by christoandrew on 11/20/16.
 */

public class TrumeterRealm extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // This instantiates DBFlow
        FlowManager.init(this);
        // add for verbose logging
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

    }
}
