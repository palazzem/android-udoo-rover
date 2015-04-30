package me.palazzetti.adkrover.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Factories {
    public static JSONObject createTweet(String text) throws JSONException {
        return new JSONObject().put("id", "240859602684612608").put("text", text);
    }
}
