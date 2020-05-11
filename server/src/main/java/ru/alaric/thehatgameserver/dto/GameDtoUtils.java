package ru.alaric.thehatgameserver.dto;

import ru.alaric.thehatgameserver.models.Game;

public class GameDtoUtils {
    public static Game createFromDto(GameDto gameDto, String codeWord) {
        return Game.createNew(gameDto.getPlayersCount(),
                gameDto.getWordsForPlayer(),
                gameDto.getTurnTime(),
                codeWord);
    }

    public static GameDto createFromGame(Game game) {
        return new GameDto(game.getPlayersCount(),
                game.getWordsForPlayer(),
                game.getTurnTime(),
                game.getCodeWord());
    }
}
