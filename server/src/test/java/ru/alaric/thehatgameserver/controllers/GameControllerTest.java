package ru.alaric.thehatgameserver.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alaric.thehatgameserver.dto.GameDto;
import ru.alaric.thehatgameserver.dto.GameDtoUtils;
import ru.alaric.thehatgameserver.exceptions.NoSuchGameException;
import ru.alaric.thehatgameserver.models.Game;
import ru.alaric.thehatgameserver.services.GameService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.alaric.thehatgameserver.testutils.TestUtils.createTestGameJson;

@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameService gameService;

    private static final int PLAYERS_COUNT = 3;
    private static final int WORDS_FOR_PLAYER = 5;
    private static final int TURN_TIME = 20;
    private static final GameDto TEST_GAME_DTO = new GameDto(PLAYERS_COUNT, WORDS_FOR_PLAYER, TURN_TIME);

    private static final String CODE_WORD = "CODE_WORD";

    private Game createTestGame() {
        Game game = GameDtoUtils.createFromDto(TEST_GAME_DTO, CODE_WORD);
        game.setId(101L);
        return game;
    }

    @Test
    void shouldCreateGame() throws Exception {
        JSONObject jsonObject = createTestGameJson(PLAYERS_COUNT, WORDS_FOR_PLAYER, TURN_TIME);
        Game game = createTestGame();
        when(gameService.createNew(eq(TEST_GAME_DTO))).thenReturn(game);

        mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString())
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        UriComponentsBuilder.newInstance()
                                .scheme("http")
                                .host("localhost")
                                .path("/game/{id}")
                                .buildAndExpand(CODE_WORD)
                                .toUriString()
                ))
                .andExpect(jsonPath("$.codeWord").value(CODE_WORD))
                .andExpect(jsonPath("$.wordsForPlayer").value(WORDS_FOR_PLAYER))
                .andReturn();
    }

    @Test
    void shouldGetGame() throws Exception {
        Game game = createTestGame();
        when(gameService.findByCodeWord(CODE_WORD)).thenReturn(game);

        mockMvc.perform(get("/game/{codeWord}", CODE_WORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wordsForPlayer").value(WORDS_FOR_PLAYER))
                .andReturn();
    }

    @Test
    void shouldGetNotFoundWhenGameNotExists() throws Exception {
        when(gameService.findByCodeWord(CODE_WORD)).thenThrow(NoSuchGameException.class);
        mockMvc.perform(get("/game/{codeWord}", CODE_WORD))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldAddWord() throws Exception {
        String[] wordList = {"one", "two"};
        JSONArray jsonArray = new JSONArray(wordList);

        mockMvc.perform(post("/game/{codeWord}/words", CODE_WORD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonArray.toString())
        )
                .andExpect(status().isOk())
                .andReturn();

        verify(gameService).addWordsToGame(eq(CODE_WORD), eq(List.of(wordList)));
    }
}