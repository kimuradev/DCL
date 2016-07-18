package com.dcl.doubleclicklight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.security.Policy;

/**
 * Created by Leandro on 17/07/2016.
 */
class LightActivity extends Activity {
    //private ImageButton btnSwitch;
    private ToggleButton toggleButton;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private android.hardware.Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // flash switch button
        //btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);

        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(LightActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });
    }

    // getting camera parameters
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    /*
* Turning On flash
*/
    private void turnOnFlash() {
        if (!isFlashOn) {
            /*if (camera == null || params == null) {
                return;
            }*/
            if (camera == null) {
                camera = Camera.open();
            }
            // play sound
            //playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
        }
    }

    private void toggleButtonImage() {
                if (toggleButton.isChecked()) {
                    //Button is ON
                    // set background as ON
                    toggleButton.setBackgroundResource(R.drawable.power_button_on);
                    // call method to turn ON the lights
                } else{
                    //Button is OFF
                    // set background as OFF
                    toggleButton.setBackgroundResource(R.drawable.power_button_off);
                    // call method to turn OFF the lights
                }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            /*if (camera == null || params == null) {
                return;
            }*/
            if (camera == null) {
                camera = Camera.open();
            }
            // play sound
            //playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }
}

