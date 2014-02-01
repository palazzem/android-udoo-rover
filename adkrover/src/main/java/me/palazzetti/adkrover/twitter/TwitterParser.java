package me.palazzetti.adkrover.twitter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.palazzetti.adkrover.arduino.Arduino;
import me.palazzetti.adkrover.utils.Helpers;

/**
 * Parse tweets into valid Arduino commands.
 */

public class TwitterParser {
    private static final int NORMALIZE_LIMIT = Integer.MAX_VALUE;
    private static final List<String> TWITTER_COMMANDS = Arrays.asList("F", "B", "L", "R", "T");

    public static List<String> tweetsToCommands(JSONArray tweetList) {
        List<String> serialCommandsList = new ArrayList<String>();
        JSONObject tweet;
        String tweetText;
        String serialCommand = null;

        try {
            if (requireNormalization(tweetList)) {
                // NotImplemented
            } else {
                for (int i = 0; i < tweetList.length(); i++) {
                    tweet = tweetList.getJSONObject(i);
                    tweetText = tweet.getString("text");
                    serialCommand = parseText(tweetText);

                    if (serialCommand != null) {
                        serialCommandsList.add(serialCommand);
                    }
                }
            }
        } catch (JSONException e) {
            // If JSON parsing error occurs, avoid any command execution
            serialCommandsList.clear();
        }

        return serialCommandsList;
    }

    private static boolean requireNormalization(JSONArray tweetList) {
        return tweetList.length() >= NORMALIZE_LIMIT;
    }

    private static String parseText(String tweetText) {
        int command;
        int speed;
        String serialCommand = null;

        String[] splitTweet = tweetText.split(" ");

        if (splitTweet.length == 2) {
            command = TWITTER_COMMANDS.indexOf(splitTweet[0]);
            speed = Helpers.isInteger(splitTweet[1]) ? Integer.valueOf(splitTweet[1]) : 0;

            if (command >= 0 && speed > 0) {
                serialCommand = Arduino.commandBuilder(command, speed);
            }
        }

        return serialCommand;
    }
}
