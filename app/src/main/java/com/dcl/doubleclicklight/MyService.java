package com.dcl.doubleclicklight;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Leandro on 8/12/2015.
 */
public class MyService extends Service {
    private Camera cam;
    private boolean isLightOn;
    private Camera.Parameters p;
    private boolean isFirstTimeFromService;
    private BroadcastReceiver mReceiver;
    private MyGlobalApp appState;

    @Override
    public void onCreate() {
        super.onCreate();
        // Call myGlobalApp class
        appState = ((MyGlobalApp)getApplicationContext());
        // Open camera
        cam = Camera.open();
        // Get camera parameters
        p = cam.getParameters();
        // Initializing isLightOn as false
        isLightOn = false;
        // Calling this method to run service as background
        runAsForeground();

        // Register Receiver that handles SCREEN ON and SCREEN OFF Logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "Starting onStartCommand method...");
        // Get value to check if is the first time that it's being is calling from Activity
        boolean isFirstTimeFromActivity = intent.getBooleanExtra("isFirstTimeFromActivity", false);

        Log.i("isFirstTimeFromService", "Check if is First Time in Service: " + isFirstTimeFromService);
        Log.i("isFirstTimeFromActivity", "Check if is First Time from ACTIVITY: " + isFirstTimeFromActivity);
        // Check if is not the first time from activity neither from service
        if (!isFirstTimeFromActivity){
            if (!isFirstTimeFromService) {
                if (isLightOn) {
                    try {
                        if (cam != null) {
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            cam.setParameters(p);
                            cam.stopPreview();
                            cam.release();
                            cam = null;
                        }
                        isLightOn = false;
                        appState.setIsLightOn(isLightOn);
                        Log.i("IS LIGHT ON", "Turn off the lights.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "Some Exception has occured. Please contact us. ",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Check if camera is null to open again
                    try {
                        if (cam == null) {
                            cam = Camera.open();
                        }
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(p);
                        cam.startPreview();
                        isLightOn = true;

                        appState.setIsLightOn(isLightOn);
                        Log.i("IS LIGHT ON", "Turn ON the lights.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "Some Exception has occured. Please contact us.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                isFirstTimeFromService = false;
            }
        }else{
            isFirstTimeFromService = false;
        }
        return Service.START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    private void runAsForeground() {
        Log.d("SERVICE ", "runAsForeground method IS RUNNING... ");
        // Set first time from service as true
        isFirstTimeFromService = true;

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        // Adding this flag to resume my Activity from Notification
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
        Notification notification;
        b.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_white)
                .setTicker("DoubleClick Light")
                .setContentTitle("DoubleClick Light")
                .setContentText("Running in background")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        // Set onGoing as true to be showed as notification status bar
        b.setOngoing(true);
        notification = b.build();
        startForeground(101, notification);
    }

    @Override
    public void onDestroy() {
        Log.d("On DESTROY", "Enter in ON DESTROY method!! ");
        if (mReceiver != null){
            unregisterReceiver(mReceiver);
            Log.i("On DESTROY", "mReceiver was destroyed.");
        }
        super.onDestroy();
    }
}
