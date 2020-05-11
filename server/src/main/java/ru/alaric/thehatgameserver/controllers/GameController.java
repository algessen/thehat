package ru.alaric.thehatgameserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alaric.thehatgameserver.dto.GameDto;
import ru.alaric.thehatgameserver.dto.GameDtoUtils;
import ru.alaric.thehatgameserver.exceptions.ListIsAlreadyFullException;
import ru.alaric.thehatgameserver.exceptions.NoSuchGameException;
import ru.alaric.thehatgameserver.models.Game;
import ru.alaric.thehatgameserver.services.GameService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("/game")
    public ResponseEntity<?> createGame(@RequestBody GameDto gameDto, UriComponentsBuilder builder) {
        Game game = gameService.createNew(gameDto);
        UriComponents uriComponents = builder.path("/game/{codeWord}").buildAndExpand(game.getCodeWord());
        return ResponseEntity
                .created(uriComponents.toUri())
                .body(GameDtoUtils.createFromGame(game));
    }

    @GetMapping("/game/{codeWord}")
    public GameDto getGame(@PathVariable("codeWord") String codeWord) throws NoSuchGameException {
        Game game = gameService.findByCodeWord(codeWord);
        return GameDtoUtils.createFromGame(game);
    }

    @PostMapping("/game/{codeWord}/words")
    void addWordsToGame(@PathVariable("codeWord") String codeWord, @RequestBody List<String> words)
            throws ListIsAlreadyFullException, NoSuchGameException {
        gameService.addWordsToGame(codeWord, words);
    }

    //TODO вставлено исключительно в отладочных целях, позже удалить
    @GetMapping("/game/{codeWord}/words")
    List<String> getWords(@PathVariable("codeWord") String codeWord) throws NoSuchGameException {
        return gameService.getWords(codeWord);
    }

    @ExceptionHandler(NoSuchGameException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void gameNotFound() {

    }

    @ExceptionHandler(ListIsAlreadyFullException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String wordListIsAlreadyFull() {
        return "Word list is already full";
    }
}
