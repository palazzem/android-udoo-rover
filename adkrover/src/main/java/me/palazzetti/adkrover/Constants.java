package me.palazzetti.adkrover;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String LOG_TAG = "DroidRover";
    public static final List<String> TWITTER_VERBS = Arrays.asList("f", "b", "l", "r", "t");
    public static final int MAX_CONSECUTIVE_COMMANDS = 3;
    public static final int NORMALIZE_LIMIT = 5;
    public static final int MYO_DEFAULT_SPEED = 250;

    public static enum Actions {
        LEFT, RIGHT, FORWARD, BACKWARD
    }
}
