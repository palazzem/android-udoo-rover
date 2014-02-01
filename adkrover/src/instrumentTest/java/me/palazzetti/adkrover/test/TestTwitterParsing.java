package me.palazzetti.adkrover.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import org.json.JSONArray;

import java.util.List;

import me.palazzetti.adkrover.RoverActivity;
import me.palazzetti.adkrover.twitter.TwitterParser;
import me.palazzetti.adkrover.utils.Helpers;

/**
 * Parses a Twitter JSONArray and produce the correct sequence of commands.
 */

public class TestTwitterParsing extends ActivityInstrumentationTestCase2<RoverActivity> {
    private JSONArray tweetList = new JSONArray();
    private JSONArray tweetListNormalized = new JSONArray();

    public TestTwitterParsing() {
        super(RoverActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Create some mock tweets
        tweetList.put(Helpers.createMockTweet("F 100"));
        tweetList.put(Helpers.createMockTweet("B 200"));
        tweetList.put(Helpers.createMockTweet("L 300"));
        tweetList.put(Helpers.createMockTweet("R 400"));
        tweetList.put(Helpers.createMockTweet("T 0"));
        tweetList.put(Helpers.createMockTweet("T 50"));
        tweetList.put(Helpers.createMockTweet("Fake tweet"));
        tweetList.put(Helpers.createMockTweet("This is a more fake tweet"));
        tweetList.put(Helpers.createMockTweet("A F 200"));

        // Create some mock tweets which should be normalized
        tweetListNormalized.put(Helpers.createMockTweet("F 100"));
        tweetListNormalized.put(Helpers.createMockTweet("F 100"));
        tweetListNormalized.put(Helpers.createMockTweet("F 100"));
        tweetListNormalized.put(Helpers.createMockTweet("F 100"));
        tweetListNormalized.put(Helpers.createMockTweet("F 100"));
        tweetListNormalized.put(Helpers.createMockTweet("R 400"));
        tweetListNormalized.put(Helpers.createMockTweet("R 400"));
        tweetListNormalized.put(Helpers.createMockTweet("F 100"));
        tweetListNormalized.put(Helpers.createMockTweet("L 300"));
        tweetListNormalized.put(Helpers.createMockTweet("L 300"));
        tweetListNormalized.put(Helpers.createMockTweet("L 300"));
        tweetListNormalized.put(Helpers.createMockTweet("R 400"));
        tweetListNormalized.put(Helpers.createMockTweet("B 200"));
        tweetListNormalized.put(Helpers.createMockTweet("B 200"));
        tweetListNormalized.put(Helpers.createMockTweet("B 200"));
        tweetListNormalized.put(Helpers.createMockTweet("B 200"));
    }

    @SmallTest
    public void testTweetGeneration() {
        assertEquals(tweetList.length(), 9);
        assertEquals(tweetListNormalized.length(), 16);
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

    @SmallTest
    public void testTweetsNormalizingParsing() {
        List<String> serialCommandsList = TwitterParser.tweetsToCommands(tweetListNormalized);

        assertEquals(serialCommandsList.size(), 13);
    }
}
