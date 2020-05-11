package ru.alaric.thehatgameserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
    private int playersCount;
    private int wordsForPlayer;
    private int turnTime;
    private String codeWord;

    public GameDto(int playersCount, int wordsForPlayer, int turnTime) {
        this.playersCount = playersCount;
        this.wordsForPlayer = wordsForPlayer;
        this.turnTime = turnTime;
    }
}
