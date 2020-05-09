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
}
