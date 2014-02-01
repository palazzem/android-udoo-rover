package me.palazzetti.adkrover;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import me.palazzetti.adkrover.adktoolkit.AdkManager;

public class RoverActivity extends Activity {
    private AdkManager mAdkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover);

        // Start ADK Manager
        mAdkManager = new AdkManager((UsbManager) getSystemService(Context.USB_SERVICE));
        registerReceiver(mAdkManager.getUsbReceiver(), mAdkManager.getDetachedFilter());
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
}
