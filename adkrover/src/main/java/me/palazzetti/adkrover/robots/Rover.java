package me.palazzetti.adkrover.robots;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import me.palazzetti.adkrover.Constants;
import me.palazzetti.adktoolkit.AdkManager;

public class Rover {
    private AdkManager mADK;

    public Rover(AdkManager manager) {
        this.mADK = manager;
    }

    public void turnOn() {
        this.mADK.open();
    }

    public void turnOff() {
        this.mADK.close();
    }

    public void execute(String action) {
        mADK.write(action);
    }

    public void execute(JSONArray tweets) {
        List<String> actions = listen(tweets);
        for (String action : actions) {
            execute(action);
        }
    }

    public void forward(int speed) {
        execute(command(0, speed));
    }

    public void back(int speed) {
        execute(command(1, speed));
    }

    public void left(int speed) {
        execute(command(2, speed));
    }

    public void right(int speed) {
        execute(command(3, speed));
    }

    public void turn(int speed) {
        execute(command(4, speed));
    }

    public List<String> listen(JSONArray tweets) {
        // TODO: this method should not be public (visibility only for testing purpose in the middle of a refactoring)
        boolean normalizationRequired = tweets.length() >= Constants.NORMALIZE_LIMIT;
        int lastCommandCounter = 0;
        String action;
        String lastCommand = null;
        List<String> actions = new ArrayList<>();

        for (int i = 0; i < tweets.length(); i++) {
            try {
                action = recognize(tweets.getJSONObject(i).getString("text"));

                // Avoids too much occurrence of the same command
                if (normalizationRequired) {
                    if (action.equals(lastCommand)) {
                        lastCommandCounter++;
                    } else {
                        lastCommandCounter = 0;
                        lastCommand = action;
                    }

                    if (lastCommandCounter >= Constants.MAX_CONSECUTIVE_COMMANDS) {
                        continue;
                    }
                }

                actions.add(action);

            } catch (JSONException | UnrecognizedCommand e) {
                Log.e(Constants.LOG_TAG, e.getMessage());
            }
        }

        return actions;
    }

    public String recognize(String tweet) throws UnrecognizedCommand {
        // TODO: this method should not be public (visibility only for testing purpose in the middle of a refactoring)
        int action = -1;
        int speed = 0;

        try {
            String[] splitTweet = tweet.split(" ");

            if (splitTweet.length >= 3) {
                action = Constants.TWITTER_VERBS.indexOf(splitTweet[1].toLowerCase());
                speed = Integer.valueOf(splitTweet[2]);
            }

            if (action == -1 || speed == 0) {
                throw new UnrecognizedCommand();
            }
        } catch (Exception e) {
            throw new UnrecognizedCommand();
        }

        return command(action, speed);
    }

    /**
     * Creates a serial command with format DIRECTION-SPEED (D-SSS)
     * @param action used in Arduino
     * @param speed with range 0-400
     * @return a valid Arduino command
     */
    private String command(int action, int speed) {
        return String.valueOf(action) + "-" + String.valueOf(speed);
    }
}
