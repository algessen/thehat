package ru.alaric.thehatgameserver.repositories;

import ru.alaric.thehatgameserver.exceptions.ListIsAlreadyFullException;
import ru.alaric.thehatgameserver.models.Game;

import java.util.List;

public interface WordListRepository {
    /**
     * Добавляет слова от пользователей в игру
     * @return true - если больше слов не нужно
     */
    boolean addPlayerWords(Game game, List<String> words) throws ListIsAlreadyFullException;

    List<String> getWords(Game game);

    void updateWords(Game game, List<String> words);
}
