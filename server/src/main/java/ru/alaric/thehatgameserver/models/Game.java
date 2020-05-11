package ru.alaric.thehatgameserver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@AllArgsConstructor
@Data
@RedisHash(value = "Game", timeToLive = 24 * 3600)
public class Game implements Serializable {
    private static final long serialVersionUID = 8218590856794973043L;

    @Id
    private Long id;
    private int playersCount;
    private int wordsForPlayer;
    private int turnTime;
    @Indexed
    private String codeWord;

    public static Game createNew(int playersCount,
                                 int wordsForPlayer,
                                 int turnTime,
                                 String codeWord) {
        return new Game(null, playersCount, wordsForPlayer, turnTime, codeWord);
    }

    public int getMaxWordCount() {
        return playersCount * wordsForPlayer;
    }
}
