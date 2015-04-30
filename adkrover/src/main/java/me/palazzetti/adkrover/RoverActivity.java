package me.palazzetti.adkrover;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import me.palazzetti.adktoolkit.AdkManager;
import me.palazzetti.adkrover.robots.Rover;
import me.palazzetti.adkrover.twitter.TwitterReceiver;

public class RoverActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "DroidRover";

    private Rover mRover;
    private AsyncTwitterReceiver mAsyncReceiver = null;
    private TextView mSpeedText;

    private int mSelectedSpeed = 0;
    private long mLastFetchedId;
    private boolean mKeepAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover);

        // Gets the Rover instance
        mRover = new Rover(new AdkManager(this));
        mRover.turnOn();

        // Widget assignment
        mSpeedText = (TextView) findViewById(R.id.speed_text);
        mSpeedText.setText(getResources().getString(R.string.rover_speed) + mSelectedSpeed);

        SeekBar speedBar = (SeekBar) findViewById(R.id.speed_bar);
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_pairing) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store last tweet
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("lastId", mLastFetchedId);
        editor.apply();

        // Turn off AsyncReceiver
        mKeepAlive = false;
        mAsyncReceiver = null;
    }

    public void goForward(View v) {
        mRover.forward(mSelectedSpeed);
    }

    public void goBackward(View v) {
        mRover.back(mSelectedSpeed);
    }

    public void turnLeft(View v) {
        mRover.left(mSelectedSpeed);
    }

    public void turnRight(View v) {
        mRover.right(mSelectedSpeed);
    }

    public void startTwitterCommandsFetch(View v) {
        // Check if connection is available
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (mAsyncReceiver == null) {
                // Start Twitter "game"
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.start_fetching), Toast.LENGTH_LONG).show();
                mKeepAlive = true;
                mAsyncReceiver = new AsyncTwitterReceiver();
                mAsyncReceiver.execute();
            } else {
                // Stop Twitter "game"
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.stop_fetching), Toast.LENGTH_LONG).show();
                mKeepAlive = false;
                mAsyncReceiver = null;
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_online), Toast.LENGTH_LONG).show();
        }
    }

    public class AsyncTwitterReceiver extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray tweets;

            try {
                TwitterReceiver twitterReceiver = new TwitterReceiver();

                while (mKeepAlive) {
                    tweets = twitterReceiver.getTwitterStream(mLastFetchedId);

                    if (tweets.length() > 0) {
                        // Last tweet is the newest
                        mLastFetchedId = tweets.getJSONObject(0).getLong("tweet_id");
                        mRover.execute(tweets);
                    }

                    // Wait before next polling
                    Thread.sleep(1000);
                }
            } catch (JSONException | InterruptedException e) {
                Log.e(Constants.LOG_TAG, e.getMessage());
            }

            return null;
        }
    }
}
