package me.palazzetti.adkrover.twitter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

import android.util.Log;

import me.palazzetti.adkrover.Constants;
import me.palazzetti.adkrover.http.UrlConnector;

public class TwitterReceiver {
    private final static String STREAM_API = "http://droidcon.evonove.it/api/tweets/?id=%d";

    public JSONArray getTwitterStream(long lastFetchedId) {
        JSONArray twitterTimeline = new JSONArray();

        try {
            // Requires Twitter timeline
            UrlConnector twitterConnector = new UrlConnector(buildQuery(lastFetchedId));
            twitterConnector.addHeader("Content-Type", "application/json");

            // Do GET and grab tweets into a JSONArray
            int statusCode = twitterConnector.get();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                twitterTimeline = new JSONArray(twitterConnector.getResponse());
            } else {
                Log.e(Constants.LOG_TAG, String.valueOf(statusCode));
            }

            // Close connection
            twitterConnector.disconnect();

        } catch (JSONException | IOException e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
        }

        return twitterTimeline;
    }

    private String buildQuery(long lastFetchedId) {
        return String.format(STREAM_API, lastFetchedId);
    }
}
