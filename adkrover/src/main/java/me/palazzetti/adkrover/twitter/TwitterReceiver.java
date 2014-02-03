package me.palazzetti.adkrover.twitter;

import android.util.Log;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import me.palazzetti.adkrover.utils.UrlConnector;

/**
 * Mocks Twitter receiver: it uses only id and text fields.
 */

public class TwitterReceiver {
    private final static String TAG_LOG = "RoverNetwork";
    private final static String STREAM_API = "http://droidcon.evonove.it/api/tweets/?";

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
                Log.e(TAG_LOG, String.valueOf(statusCode));
            }

            // Close connection
            twitterConnector.disconnect();

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG_LOG, e.getMessage());
        } catch (IllegalStateException e) {
            Log.e(TAG_LOG, e.getMessage());
        } catch (MalformedURLException e) {
            Log.e(TAG_LOG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG_LOG, e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG_LOG, e.getMessage());
        }

        return twitterTimeline;
    }

    private String buildQuery(long lastFetchedId) {
        List<BasicNameValuePair> parameters = Arrays.asList(
                new BasicNameValuePair("id", String.valueOf(lastFetchedId)));

        return STREAM_API + URLEncodedUtils.format(parameters, "UTF-8");
    }
}
