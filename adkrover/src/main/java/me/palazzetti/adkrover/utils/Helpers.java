package me.palazzetti.adkrover.utils;

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
}
