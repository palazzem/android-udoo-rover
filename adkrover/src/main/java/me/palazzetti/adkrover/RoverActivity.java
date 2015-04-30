package me.palazzetti.adkrover;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import me.palazzetti.adktoolkit.AdkManager;
import me.palazzetti.adkrover.arduino.Arduino;
import me.palazzetti.adkrover.twitter.TwitterParser;
import me.palazzetti.adkrover.twitter.TwitterReceiver;

public class RoverActivity extends Activity {
    private static final String PREFS_NAME = "DroidRover";

    private AsyncTwitterReceiver mAsyncReceiver = null;
    private AdkManager mAdkManager;
    private TextView mSpeedText;
    private SeekBar mSpeedBar;

    private int mSelectedSpeed = 0;
    private long mLastFetchedId;
    private boolean mKeepAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover);

        // Start ADK Manager
        mAdkManager = new AdkManager((UsbManager) getSystemService(Context.USB_SERVICE));
        registerReceiver(mAdkManager.getUsbReceiver(), mAdkManager.getDetachedFilter());

        // Widget assignment
        mSpeedText = (TextView) findViewById(R.id.speed_text);
        mSpeedText.setText(getResources().getString(R.string.rover_speed) + mSelectedSpeed);

        mSpeedBar = (SeekBar) findViewById(R.id.speed_bar);
        mSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpeedText.setText(getResources().getString(R.string.rover_speed) + String.valueOf(progress));
                mSelectedSpeed = progress;
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

        // Get last stored values
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mLastFetchedId = settings.getLong("lastId", 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdkManager.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdkManager.open();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store last tweet
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("lastId", mLastFetchedId);
        editor.commit();

        // Turn off AsyncReceiver
        mKeepAlive = false;
        mAsyncReceiver = null;
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
        String serialCommand = Arduino.commandBuilder(0, mSelectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void goBackward(View v) {
        String serialCommand = Arduino.commandBuilder(1, mSelectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void turnLeft(View v) {
        String serialCommand = Arduino.commandBuilder(2, mSelectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void turnRight(View v) {
        String serialCommand = Arduino.commandBuilder(3, mSelectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void turnBack(View v) {
        String serialCommand = Arduino.commandBuilder(4, mSelectedSpeed);
        Arduino.executeCommand(mAdkManager, serialCommand);
    }

    public void startTwitterCommandsFetch(View v) {
        // Check if connection is available
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (mAsyncReceiver == null) {
                // Start Twitter "game"
                mKeepAlive = true;
                mAsyncReceiver = new AsyncTwitterReceiver();
                mAsyncReceiver.execute();
            } else {
                // Stop Twitter "game"
                mKeepAlive = false;
                mAsyncReceiver = null;
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_online), Toast.LENGTH_LONG).show();
        }
    }

    public class AsyncTwitterReceiver extends AsyncTask<Void, String, Void> {
        private final static String TAG_LOG = "RoverAsyncReceiver";

        @Override
        protected Void doInBackground(Void... params) {
            JSONArray twitterCommands;

            try {
                TwitterReceiver twitterReceiver = new TwitterReceiver();
                publishProgress(getResources().getString(R.string.start_fetching));

                while (mKeepAlive) {
                    twitterCommands = twitterReceiver.getTwitterStream(mLastFetchedId);

                    if (twitterCommands.length() > 0) {
                        // Last tweet is the newest
                        mLastFetchedId = twitterCommands.getJSONObject(0).getLong("tweet_id");
                        List<String> serialCommandsList = TwitterParser.tweetsToCommands(twitterCommands);

                        publishProgress(getResources().getString(R.string.start_execution));
                        for (String serialCommand : serialCommandsList) {
                            Log.d(TAG_LOG, "Executing: " + serialCommand);
                            mAdkManager.write(serialCommand);
                        }
                    }

                    // Wait before next polling
                    Thread.sleep(1000);
                }
            } catch (JSONException e) {
                Log.e(TAG_LOG, e.getMessage());
            } catch (InterruptedException e) {
                Log.e(TAG_LOG, e.getMessage());
            } finally {
                publishProgress(getResources().getString(R.string.stop_fetching));
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
