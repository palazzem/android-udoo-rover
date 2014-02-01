package me.palazzetti.adkrover.utils;

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
}
