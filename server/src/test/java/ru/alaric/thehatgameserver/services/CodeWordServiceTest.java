package ru.alaric.thehatgameserver.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodeWordServiceTest {
    private CodeWordService codeWordService;

    @BeforeEach
    void init() {
        codeWordService = new CodeWordService();
    }

    @Test
    void shouldGenerate() {
        String result = codeWordService.generate();
        assertThat(result)
                .hasSize(CodeWordService.LETTER_COUNT);
    }

}