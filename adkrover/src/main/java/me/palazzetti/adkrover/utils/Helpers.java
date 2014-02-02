package me.palazzetti.adkrover.utils;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Some class helpers
 */

public class Helpers {
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static JSONObject createMockTweet(String text) throws JSONException{
        return new JSONObject().put("id", "240859602684612608").put("text", text);
    }

    public static String oAuth2TwitterEncoding(String consumerKey, String consumerSecret) {
        String combinedKey = consumerKey + ":" + consumerSecret;

        return Base64.encodeToString(combinedKey.getBytes(), Base64.NO_WRAP);
    }
}
