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
    private static final int NORMALIZE_LIMIT = 5;
    private static final int MAX_CONSECUTIVE_COMMANDS = 3;
    private static final List<String> TWITTER_COMMANDS = Arrays.asList("f", "b", "l", "r", "t");

    public static List<String> tweetsToCommands(JSONArray tweetList) {
        boolean normalizationRequired = tweetList.length() >= NORMALIZE_LIMIT;
        int lastCommandCounter = 0;
        String serialCommand;
        String lastCommand = null;

        List<String> serialCommandsList = new ArrayList<String>();

        for (int i = 0; i < tweetList.length(); i++) {
            try {
                serialCommand = parseText(tweetList.getJSONObject(i).getString("text"));

                // Avoid too much occurrence of the same command
                if (normalizationRequired) {
                    if (serialCommand.equals(lastCommand)) {
                        lastCommandCounter++;
                    } else {
                        lastCommandCounter = 0;
                        lastCommand = serialCommand;
                    }

                    if (lastCommandCounter >= MAX_CONSECUTIVE_COMMANDS) {
                        continue;
                    }
                }

                serialCommandsList.add(serialCommand);

            } catch (JSONException e) {
                // Exclude this command execution cause of malformed JSON
                e.printStackTrace();
            } catch (MalformedTwitterCommand e) {
                // Exclude this command execution cause of malformed Twitter command
                e.printStackTrace();
            }
        }

        return serialCommandsList;
    }

    private static String parseText(String tweetText) throws MalformedTwitterCommand {
        int command = -1;
        int speed = 0;

        String[] splitTweet = tweetText.split(" ");

        if (splitTweet.length >= 3) {
            command = TWITTER_COMMANDS.indexOf(splitTweet[1].toLowerCase());
            speed = Helpers.isInteger(splitTweet[2]) ? Integer.valueOf(splitTweet[2]) : 0;
        }

        if (command < 0 || speed <= 0) {
            throw new MalformedTwitterCommand("Malformed Twitter command");
        }

        return Arduino.commandBuilder(command, speed);
    }
}
