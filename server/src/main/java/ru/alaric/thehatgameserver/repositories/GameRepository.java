package ru.alaric.thehatgameserver.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.alaric.thehatgameserver.models.Game;

import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    Optional<Game> findByCodeWord(String codeWord);
}
