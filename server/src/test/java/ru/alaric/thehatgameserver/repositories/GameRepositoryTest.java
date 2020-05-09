package ru.alaric.thehatgameserver.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.alaric.thehatgameserver.embeddedredis.EmbeddedRedisTestConfiguration;
import ru.alaric.thehatgameserver.models.Game;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@Import(EmbeddedRedisTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GameRepositoryTest {
    @Autowired
    private GameRepository repository;

    private static final int PLAYERS_COUNT = 5;
    private static final int WORDS_COUNT = 4;
    private static final int TURN_TIME = 10;
    private static final String CODE_WORD = "CODE_WORD";

    @Test
    void shouldAddGame() {
        Game game = Game.createNew(PLAYERS_COUNT, WORDS_COUNT, TURN_TIME, CODE_WORD);
        Game savedGame = repository.save(game);
        assertThat(savedGame)
                .isNotNull();
    }
}