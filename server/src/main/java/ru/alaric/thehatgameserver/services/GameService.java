package ru.alaric.thehatgameserver.services;

import ru.alaric.thehatgameserver.dto.GameDto;
import ru.alaric.thehatgameserver.exceptions.ListIsAlreadyFullException;
import ru.alaric.thehatgameserver.exceptions.NoSuchGameException;
import ru.alaric.thehatgameserver.models.Game;

import java.util.List;

public interface GameService {
    Game createNew(GameDto gameDto);
    Game findByCodeWord(String codeWord) throws NoSuchGameException;
    void addWordsToGame(String codeWord, List<String> wordList) throws ListIsAlreadyFullException, NoSuchGameException;
    List<String> getWords(String codeWord) throws NoSuchGameException;
}
