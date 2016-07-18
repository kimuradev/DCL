package com.dcl.doubleclicklight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private boolean isLightOn;
    private BroadcastReceiver mReceiver;
    private ToggleButton toggleButton;
    private MyGlobalApp appState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ON CREATE", "Starting MainActivity class... ");
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        // Get MyGlobalApp class
        appState = ((MyGlobalApp)getApplicationContext());
        // Set isLightOn with appState value
        isLightOn = appState.isLightOn();

        // Call Intent and pass isFirstTimeFromActivity as true
        Intent i = new Intent(getApplicationContext(), MyService.class);
        i.putExtra("isFirstTimeFromActivity", true);
        getApplicationContext().startService(i);

        // Declare toggle button
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
    }

    @Override
    protected void onStart() {
        Log.d("On START", "Enter in ON START method... ");
        super.onStart();
        // Keep ScreenON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Set toggle button with appState isLightOn value
        toggleButton.setChecked(appState.isLightOn());
        // OnClick event listner from toggle button
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    //Button is ON
                    // set background as ON
                    toggleButton.setBackgroundResource(R.drawable.power_button_on);
                    // call method to turn ON the lights
                    turnLightOnOff(v);
                } else{
                    //Button is OFF
                    // set background as OFF
                    toggleButton.setBackgroundResource(R.drawable.power_button_off);
                    // call method to turn OFF the lights
                    turnLightOnOff(v);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d("On RESUME", "Enter in ON RESUME method... ");
        super.onResume();

        // Check if isLightOn is true or false and replace the icon background.
        Log.d("On RESUME", "IS LIGHT ON:  " + appState.isLightOn());
        if(appState.isLightOn()){
            toggleButton.setBackgroundResource(R.drawable.power_button_on);
        }else{
            toggleButton.setBackgroundResource(R.drawable.power_button_off);
        }
    }

    @Override
    protected void onPause() {
        Log.d("On PAUSE", "Enter in ON PAUSE method... ");
        super.onPause();

    }

    // Method to turn on or turn off the lights
    public void turnLightOnOff(View view) {
        Intent i = new Intent(getApplicationContext(), MyService.class);
        i.putExtra("isFirstTimeFromActivity", false);
        getApplicationContext().startService(i);
    }

    // Method to add settings menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // Method to make some action under settings menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                /*startActivity(new Intent(this, About.class));*/
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about:
                openAboutDialog();
                return true;
            case R.id.action_contact_us:
                openContactUsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    protected void onDestroy() {
        Log.d("On DESTROY", "Enter in ON DESTROY method... ");
        if (mReceiver != null){
            unregisterReceiver(mReceiver);
            Log.i("On DESTROY", "mReceiver was destroyed.");
        }
        super.onDestroy();
    }

    public void openAboutDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("About");
        alertDialogBuilder.setMessage("To turn ON/OFF your light you just need to press a double click on power button key. However, this app works only if you are with SCREEN ON and SCREEN OFF function available. ");

        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void openContactUsDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Contact us");
        alertDialogBuilder.setMessage("If you have any problem or suggestion please send an email to leandro2ki@gmail.com ");

        alertDialogBuilder.setNeutralButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
