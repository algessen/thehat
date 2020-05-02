package ru.alaric.thehatgameserver.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.alaric.thehatgameserver.embeddedredis.EmbeddedRedisTestConfiguration;
import ru.alaric.thehatgameserver.exceptions.ListIsAlreadyFullException;
import ru.alaric.thehatgameserver.models.Game;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@Import({EmbeddedRedisTestConfiguration.class, WordListRepositoryImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WordListRepositoryImplTest {
    @Autowired
    WordListRepositoryImpl wordListRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final Game GAME = new Game(1L, 2, 2, "CODE_WORD");
    private static final List<String> LIST1 = Arrays.asList("one", "two");
    private static final List<String> LIST2 = Arrays.asList("three", "four");
    private static final List<String> BIG_LIST = Arrays.asList("one", "two", "three", "four");
    private static final int WORDS_COUNT = 4;

    @Test
    void shouldAddPlayerWords() throws ListIsAlreadyFullException {
        boolean result = wordListRepository.addPlayerWords(GAME, LIST1);
        assertThat(result).isEqualTo(false);

        List<String> resultList = stringRedisTemplate.opsForList().range(
                WordListRepositoryImpl.getListId(GAME), 0, WORDS_COUNT - 1);
        assertThat(resultList).containsExactlyElementsOf(LIST1);
    }

    @Test
    void shouldAddPlayerWordsAndGetFinishResult() throws ListIsAlreadyFullException {
        wordListRepository.addPlayerWords(GAME, LIST1);
        boolean result = wordListRepository.addPlayerWords(GAME, LIST2);
        assertThat(result).isEqualTo(true);

        List<String> resultList = stringRedisTemplate.opsForList().range(
                WordListRepositoryImpl.getListId(GAME), 0, WORDS_COUNT - 1);
        assertThat(resultList)
                .hasSize(WORDS_COUNT)
                .containsAll(LIST1)
                .containsAll(LIST2);
    }

    @Test
    void shouldGetWords() {
        stringRedisTemplate.opsForList().rightPushAll(WordListRepositoryImpl.getListId(GAME), BIG_LIST);

        List<String> result = wordListRepository.getWords(GAME);
        assertThat(result)
                .containsExactlyInAnyOrderElementsOf(BIG_LIST);
    }

    @Test
    void shouldUpdateWords() {
        stringRedisTemplate.opsForList().rightPushAll(WordListRepositoryImpl.getListId(GAME), BIG_LIST);

        wordListRepository.updateWords(GAME, LIST2);

        List<String> resultList = stringRedisTemplate.opsForList().range(
                WordListRepositoryImpl.getListId(GAME), 0, WORDS_COUNT - 1);
        assertThat(resultList)
                .containsExactlyInAnyOrderElementsOf(LIST2);
    }
}