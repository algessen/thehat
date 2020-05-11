package ru.alaric.thehatgameserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alaric.thehatgameserver.dto.GameDto;
import ru.alaric.thehatgameserver.dto.GameDtoUtils;
import ru.alaric.thehatgameserver.exceptions.ListIsAlreadyFullException;
import ru.alaric.thehatgameserver.exceptions.NoSuchGameException;
import ru.alaric.thehatgameserver.models.Game;
import ru.alaric.thehatgameserver.repositories.GameRepository;
import ru.alaric.thehatgameserver.repositories.WordListRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final CodeWordService codeWordService;
    private final GameRepository gameRepository;
    private final WordListRepository wordListRepository;

    @Override
    public Game createNew(GameDto gameDto) {
        String codeWord;
        do {
            codeWord = codeWordService.generate();
        } while (gameRepository.findByCodeWord(codeWord).isPresent());

        // TODO здесь есть потоконебезопасность, но шанс совпадения кодовых слов довольно мал
        Game game = GameDtoUtils.createFromDto(gameDto, codeWord);
        return gameRepository.save(game);
    }

    @Override
    public Game findByCodeWord(String codeWord) throws NoSuchGameException {
        return gameRepository.findByCodeWord(codeWord)
                .orElseThrow(NoSuchGameException::new);
    }

    @Override
    public void addWordsToGame(String codeWord,
                               List<String> wordList)
            throws ListIsAlreadyFullException, NoSuchGameException {
        Game game = findByCodeWord(codeWord);
        wordListRepository.addPlayerWords(game, wordList);
    }

    @Override
    public List<String> getWords(String codeWord) throws NoSuchGameException {
        Game game = findByCodeWord(codeWord);
        return wordListRepository.getWords(game);
    }


}
