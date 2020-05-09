package ru.alaric.thehatgameserver.testutils;

import org.json.JSONException;
import org.json.JSONObject;

public class TestUtils {
    public static JSONObject createTestGameJson(int playersCount,
                                                      int wordsForPlayer,
                                                      int turnTime) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playersCount", playersCount);
        jsonObject.put("wordsForPlayer", wordsForPlayer);
        jsonObject.put("turnTime", turnTime);
        return jsonObject;
    }
}
