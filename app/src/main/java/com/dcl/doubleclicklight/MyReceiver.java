package com.dcl.doubleclicklight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Leandro on 8/12/2015.
 */
public class MyReceiver extends BroadcastReceiver {

    private boolean screenOff;
    private boolean firstConnection = true;
    // Set currentTime as the last time in milliseconds
    long currentTime = System.currentTimeMillis();
    long interval = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BROADCAST RECEIVER", "Starting onReceive method ...");

        // check if it is the firstConnection to avoid multiple calls
        if (firstConnection) {
            Log.d("RECEIVER Time", "Start Time: " + currentTime);
            interval = System.currentTimeMillis() - currentTime;
            Log.d("RECEIVER Interval: ", "Interval time: " + interval);
            // If interval less than 1 second the do the action
            if (interval < 1000) {
                Intent i = new Intent(context, MyService.class);
                context.startService(i);
                firstConnection = false;
            }
        } else {
            firstConnection = true;
        }
        // Set currentTime as the last time in milliseconds
        currentTime = System.currentTimeMillis();
    }
}
