package me.palazzetti.adkrover;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import me.palazzetti.adkrover.adktoolkit.AdkManager;
import me.palazzetti.adkrover.arduino.Arduino;
import me.palazzetti.adkrover.twitter.TwitterParser;
import me.palazzetti.adkrover.twitter.TwitterReceiver;

public class RoverActivity extends Activity {
    private AdkManager mAdkManager;
    private AsyncTwitterReceiver mAsyncReceiver = null;
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
        String serialCommand = Arduino.commandBuilder(0, selectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void goBackward(View v) {
        String serialCommand = Arduino.commandBuilder(1, selectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void turnLeft(View v) {
        String serialCommand = Arduino.commandBuilder(2, selectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void turnRight(View v) {
        String serialCommand = Arduino.commandBuilder(3, selectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void turnBack(View v) {
        String serialCommand = Arduino.commandBuilder(4, selectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void startTwitterCommandsFetch(View v) {
        // Check if connection is available
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Start Twitter "game" once
            if (mAsyncReceiver == null) {
                mAsyncReceiver = new AsyncTwitterReceiver();
                mAsyncReceiver.execute();
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_online), Toast.LENGTH_LONG).show();
        }
    }

    public class AsyncTwitterReceiver extends AsyncTask<Void, String, Void> {
        private final static String TAG_LOG = "RoverAsyncReceiver";
        private String lastFetchedId = "1";

        @Override
        protected Void doInBackground(Void... params) {
            JSONArray twitterCommands;

            try {
                while (true) {
                    publishProgress("Start fetching");
                    twitterCommands = new TwitterReceiver().searchMentions("DroidRover", lastFetchedId);

                    if (twitterCommands.length() > 0) {
                        // Store the last one
                        lastFetchedId = twitterCommands.getJSONObject(0).getString("id_str");

                        List<String> serialCommandsList = TwitterParser.tweetsToCommands(twitterCommands);

                        publishProgress("Start executing");
                        for (String serialCommand : serialCommandsList) {
                            mAdkManager.sendText(serialCommand);
                        }

                        publishProgress("Finished executing");
                    }
                    Thread.sleep(5000);
                    publishProgress("Waking up!");
                }
            } catch (JSONException e) {
                Log.e(TAG_LOG, e.getMessage());
            } catch (InterruptedException e) {
                Log.e(TAG_LOG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
        }
    }
}
