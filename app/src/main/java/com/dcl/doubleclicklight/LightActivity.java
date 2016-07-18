package com.dcl.doubleclicklight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Leandro on 17/07/2016.
 */
public class LightActivity extends ActionBarActivity {
    private ToggleButton toggleButton;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private android.hardware.Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // flash switch button
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
    /*
    * Turning On flash
    */
    private void turnOnFlash() {
        if (!isFlashOn) {
            try {
                if (camera == null) {
                    camera = Camera.open();
                }
                params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(params);
                camera.startPreview();
                isFlashOn = true;
                // changing button/switch image
                toggleButtonImage();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Some Exception has occured. Please contact us. ",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            try{
                if (camera == null) {
                    camera = Camera.open();
                }
                params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
                isFlashOn = false;
                // changing button/switch image
                toggleButtonImage();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Some Exception has occured. Please contact us. ",
                        Toast.LENGTH_SHORT).show();
            }
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

    private void releaseCamera(){
        if (camera != null){
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();

    }
}

