package ru.alaric.thehatgameserver.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
class CodeWordService {
    static final String ALPHABET = "АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЭЮЯ";
    static final int LETTER_COUNT = 7;

    String generate() {
        return Stream
                .generate(() -> ALPHABET.charAt(ThreadLocalRandom.current().nextInt(ALPHABET.length())))
                .limit(LETTER_COUNT)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
