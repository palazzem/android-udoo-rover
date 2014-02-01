package me.palazzetti.adkrover;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import me.palazzetti.adkrover.adktoolkit.AdkManager;
import me.palazzetti.adkrover.arduino.Arduino;

public class RoverActivity extends Activity {
    private AdkManager mAdkManager;
    private SeekBar mSpeedBar;
    private TextView mSpeedText;

    private int selectedSpeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover);

        // Start ADK Manager
        mAdkManager = new AdkManager((UsbManager) getSystemService(Context.USB_SERVICE));
        registerReceiver(mAdkManager.getUsbReceiver(), mAdkManager.getDetachedFilter());

        // Widget assignment
        mSpeedText = (TextView) findViewById(R.id.speed_text);
        mSpeedText.setText(getResources().getString(R.string.rover_speed) + "0");

        mSpeedBar = (SeekBar) findViewById(R.id.speed_bar);
        mSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpeedText.setText(getResources().getString(R.string.rover_speed) + String.valueOf(progress));
                selectedSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // noop
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // noop
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdkManager.closeAdk();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdkManager.resumeAdk();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAdkManager.getUsbReceiver());
    }

    /**
     * View interaction
     */

    public void goForward(View v) {
        Arduino.executeCommand(mAdkManager, 0, selectedSpeed);
    }

    public void goBackward(View v) {
        Arduino.executeCommand(mAdkManager, 1, selectedSpeed);
    }

    public void turnLeft(View v) {
        Arduino.executeCommand(mAdkManager, 2, selectedSpeed);
    }

    public void turnRight(View v) {
        Arduino.executeCommand(mAdkManager, 3, selectedSpeed);
    }

    public void turnBack(View v) {
        Arduino.executeCommand(mAdkManager, 4, selectedSpeed);
    }
}
