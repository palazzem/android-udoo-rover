package me.palazzetti.adkrover.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import me.palazzetti.adkrover.RoverActivity;
import me.palazzetti.adkrover.twitter.TwitterParser;

/**
 * Parses a Twitter JSONArray and produce the correct sequence of commands.
 */

public class TestTwitterParsing extends ActivityInstrumentationTestCase2<RoverActivity> {
    private JSONArray tweetList;

    public TestTwitterParsing() {
        super(RoverActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String mockTweet = "[" +
                "{'id': 240859602684612608, " +
                "'text': 'F 100'}," +
                "{'id': 240859602684612609, " +
                "'text': 'B 200'}," +
                "{'id': 240859602684612610, " +
                "'text': 'L 300'}," +
                "{'id': 240859602684612611, " +
                "'text': 'R 400'}," +
                "{'id': 240859602684612612, " +
                "'text': 'T 0'}," +
                "{'id': 240859602684612612, " +
                "'text': 'T 50'}," +
                "{'id': 240859602684612613, " +
                "'text': 'Fake tweet'}," +
                "{'id': 240859602684612614, " +
                "'text': 'This is a more fake tweet'}," +
                "{'id': 240859602684612615, " +
                "'text': 'A F 200'}" +
                "]";

        JSONArray tweetList;
        try {
            tweetList = new JSONArray(mockTweet);
        } catch (JSONException e) {
            tweetList = new JSONArray();
            Log.e("Rover", e.getMessage());
        }

        this.tweetList = tweetList;
    }

    @SmallTest
    public void testTweetGeneration() {
        assertEquals(tweetList.length(), 9);
    }

    @SmallTest
    public void testTweetsParsing() {
        List<String> serialCommandsList = TwitterParser.tweetsToCommands(tweetList);

        assertEquals(serialCommandsList.size(), 5);
        assertEquals(serialCommandsList.get(0), "0-100");
        assertEquals(serialCommandsList.get(1), "1-200");
        assertEquals(serialCommandsList.get(2), "2-300");
        assertEquals(serialCommandsList.get(3), "3-400");
        assertEquals(serialCommandsList.get(4), "4-50");
    }
}
