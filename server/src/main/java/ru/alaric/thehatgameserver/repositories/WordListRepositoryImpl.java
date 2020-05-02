package ru.alaric.thehatgameserver.repositories;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ru.alaric.thehatgameserver.exceptions.ListIsAlreadyFullException;
import ru.alaric.thehatgameserver.models.Game;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Repository
public class WordListRepositoryImpl implements WordListRepository {
    private final StringRedisTemplate stringRedisTemplate;
    private final ListOperations<String, String> listOperations;

    public WordListRepositoryImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.listOperations = stringRedisTemplate.opsForList();
    }

    private final static String LIST_ID_FORMAT = "word-list:%d";
    private final static Random RANDOM = new Random();

    static String getListId(Game game) {
        return String.format(LIST_ID_FORMAT, game.getId());
    }

    @Override
    public boolean addPlayerWords(Game game, List<String> words) throws ListIsAlreadyFullException {
        String id = getListId(game);
        Long oldLength = listOperations.size(id);
        assert oldLength != null;
        if (oldLength >= game.getMaxWordCount())
            throw new ListIsAlreadyFullException();

        Long currentLength = listOperations.rightPushAll(id, words);
        assert currentLength != null;
        if (oldLength == 0)
            stringRedisTemplate.expire(id, 24, TimeUnit.HOURS);

        return currentLength >= game.getMaxWordCount();
    }

    @Override
    public List<String> getWords(Game game) {
        List<String> list = listOperations.range(getListId(game), 0, game.getMaxWordCount() - 1);
        Collections.shuffle(Objects.requireNonNull(list), RANDOM);
        return list;
    }

    @Override
    public void updateWords(Game game, List<String> words) {
        String listId = getListId(game);
        stringRedisTemplate.delete(listId);
        listOperations.rightPushAll(listId, words);
    }
}
