package me.palazzetti.adkrover.utils;

import android.util.Base64;

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

    public static String oAuth2TwitterEncoding(String consumerKey, String consumerSecret) {
        String combinedKey = consumerKey + ":" + consumerSecret;

        return Base64.encodeToString(combinedKey.getBytes(), Base64.NO_WRAP);
    }
}
