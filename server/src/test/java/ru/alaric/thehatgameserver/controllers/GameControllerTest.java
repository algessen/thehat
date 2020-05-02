package ru.alaric.thehatgameserver.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alaric.thehatgameserver.dto.GameDto;
import ru.alaric.thehatgameserver.exceptions.NoSuchGameException;
import ru.alaric.thehatgameserver.models.Game;
import ru.alaric.thehatgameserver.services.GameService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameService gameService;

    private static final int PLAYERS_COUNT = 3;
    private static final int WORDS_FOR_PLAYER = 5;
    private static final String CODE_WORD = "CODE_WORD";

    private JSONObject createTestGameJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playersCount", PLAYERS_COUNT);
        jsonObject.put("wordsForPlayer", WORDS_FOR_PLAYER);
        return jsonObject;
    }

    @Test
    void shouldCreateGame() throws Exception {
        JSONObject jsonObject = createTestGameJson();
        GameDto gameDto = new GameDto(PLAYERS_COUNT, WORDS_FOR_PLAYER);
        Game game = mock(Game.class);
        when(gameService.createNew(eq(gameDto))).thenReturn(game);
        when(game.getCodeWord()).thenReturn(CODE_WORD);

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
                .andReturn();
    }

    @Test
    void shouldGetOkWhenGameExists() throws Exception {
        Game game = mock(Game.class);
        when(gameService.findByCodeWord(CODE_WORD)).thenReturn(game);

        mockMvc.perform(get("/game/{codeWord}", CODE_WORD))
                .andExpect(status().isOk())
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