package com.dcl.doubleclicklight;

import android.app.Application;

/**
 * Created by Leandro on 8/24/2015.
 */
public class MyGlobalApp extends Application {

    // Global variable use to control when the application is with Light ON or OFF
    public boolean isLightOn;

    public boolean isLightOn() {
        return isLightOn;
    }

    public void setIsLightOn(boolean isLightOn) {
        this.isLightOn = isLightOn;
    }
}
