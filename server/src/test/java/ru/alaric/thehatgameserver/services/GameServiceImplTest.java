package ru.alaric.thehatgameserver.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.alaric.thehatgameserver.dto.GameDto;
import ru.alaric.thehatgameserver.exceptions.ListIsAlreadyFullException;
import ru.alaric.thehatgameserver.exceptions.NoSuchGameException;
import ru.alaric.thehatgameserver.models.Game;
import ru.alaric.thehatgameserver.repositories.GameRepository;
import ru.alaric.thehatgameserver.repositories.WordListRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GameServiceImplTest {
    private GameServiceImpl gameService;
    private GameRepository gameRepository;
    private WordListRepository wordListRepository;
    private CodeWordService codeWordService;

    private static final String CODE_WORD = "CodeWord";
    private static final GameDto GAME_DTO = new GameDto(2, 2);

    @BeforeEach
    void init() {
        codeWordService = mock(CodeWordService.class);
        gameRepository = mock(GameRepository.class);
        wordListRepository = mock(WordListRepository.class);
        gameService = new GameServiceImpl(codeWordService, gameRepository, wordListRepository);
    }

    @Test
    void shouldCreateNewGame() {
        when(codeWordService.generate()).thenReturn(CODE_WORD);
        when(gameRepository.findByCodeWord(CODE_WORD)).thenReturn(Optional.empty());
        Game resultGame = mock(Game.class);
        when(gameRepository.save(
                eq(Game.createNew(GAME_DTO.getPlayersCount(), GAME_DTO.getWordsForPlayer(), CODE_WORD))))
                .thenReturn(resultGame);

        assertThat(gameService.createNew(GAME_DTO)).isEqualTo(resultGame);
    }

    @Test
    void shouldAddWords() throws ListIsAlreadyFullException, NoSuchGameException {
        Game game = mock(Game.class);
        List<String> words = List.of("one", "two");
        when(gameRepository.findByCodeWord(CODE_WORD)).thenReturn(Optional.of(game));

        gameService.addWordsToGame(CODE_WORD, words);
        verify(wordListRepository).addPlayerWords(game, words);
    }

}