package me.palazzetti.adkrover.test;

import java.util.List;
import org.json.JSONArray;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import me.palazzetti.adkrover.RoverActivity;
import me.palazzetti.adkrover.robots.Rover;
import me.palazzetti.adkrover.utils.Factories;

public class TestTwitterParsing extends ActivityInstrumentationTestCase2<RoverActivity> {
    private Rover mRover;
    private JSONArray tweetList;
    private JSONArray tweetListNormalized;

    public TestTwitterParsing() {
        super(RoverActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mRover = new Rover(null);

        // creates some mocked tweets
        tweetList = new JSONArray();
        tweetList.put(Factories.createTweet("@Username F 100"));
        tweetList.put(Factories.createTweet("@Username B 200"));
        tweetList.put(Factories.createTweet("@Username L 300"));
        tweetList.put(Factories.createTweet("@Username r 400"));
        tweetList.put(Factories.createTweet("@Username t 0"));
        tweetList.put(Factories.createTweet("@Username t 50"));
        tweetList.put(Factories.createTweet("@Username Fake tweet"));
        tweetList.put(Factories.createTweet("This is a more fake tweet"));
        tweetList.put(Factories.createTweet("@Username Another 200"));

        // creates some mocked tweets that should be normalized
        tweetListNormalized = new JSONArray();
        tweetListNormalized.put(Factories.createTweet("@Username F 100"));
        tweetListNormalized.put(Factories.createTweet("@Username F 100"));
        tweetListNormalized.put(Factories.createTweet("@Username F 100"));
        tweetListNormalized.put(Factories.createTweet("@Username F 100"));
        tweetListNormalized.put(Factories.createTweet("@Username F 100"));
        tweetListNormalized.put(Factories.createTweet("@Username R 400"));
        tweetListNormalized.put(Factories.createTweet("@Username R 400"));
        tweetListNormalized.put(Factories.createTweet("@Username F 100"));
        tweetListNormalized.put(Factories.createTweet("@Username L 300"));
        tweetListNormalized.put(Factories.createTweet("@Username L 300"));
        tweetListNormalized.put(Factories.createTweet("@Username L 300"));
        tweetListNormalized.put(Factories.createTweet("@Username R 400"));
        tweetListNormalized.put(Factories.createTweet("@Username B 200"));
        tweetListNormalized.put(Factories.createTweet("@Username B 200"));
        tweetListNormalized.put(Factories.createTweet("@Username B 200"));
        tweetListNormalized.put(Factories.createTweet("@Username B 200"));
    }

    @SmallTest
    public void testTweetsParsing() {
        List<String> serialCommandsList = mRover.listen(tweetList);

        assertEquals(serialCommandsList.size(), 5);
        assertEquals(serialCommandsList.get(0), "0-100");
        assertEquals(serialCommandsList.get(1), "1-200");
        assertEquals(serialCommandsList.get(2), "2-300");
        assertEquals(serialCommandsList.get(3), "3-400");
        assertEquals(serialCommandsList.get(4), "4-50");
    }

    @SmallTest
    public void testTweetsNormalizingParsing() {
        List<String> serialCommandsList = mRover.listen(tweetListNormalized);

        assertEquals(serialCommandsList.size(), 13);
    }
}
