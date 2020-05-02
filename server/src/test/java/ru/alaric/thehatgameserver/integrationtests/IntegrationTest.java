package ru.alaric.thehatgameserver.integrationtests;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alaric.thehatgameserver.embeddedredis.EmbeddedRedisTestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EmbeddedRedisTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class IntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String gamePath = null;

    private static final int PLAYERS_COUNT = 3;
    private static final int WORDS_FOR_PLAYER = 5;

    private JSONObject createTestGameJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playersCount", PLAYERS_COUNT);
        jsonObject.put("wordsForPlayer", WORDS_FOR_PLAYER);
        return jsonObject;
    }

    @Test
    @Order(0)
    void shouldCreateGame() throws Exception{
        JSONObject gameJson = createTestGameJson();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(gameJson.toString(), headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl("http://localhost").port(port).path("/game").build().toUri(),
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        assertThat(responseEntity)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.CREATED);

        assertThat(responseEntity.getHeaders().getLocation())
                .isNotNull();

        gamePath = responseEntity.getHeaders().getLocation().getPath();
    }

    @Order(10)
    @Test
    void shouldGetGame() {
        ResponseEntity<?> responseEntity = restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl("http://localhost").port(port).path(gamePath)
                        .encode().build().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(20)
    @Test
    void shouldGetNotFoundWhenGameIsAbsent() {
        ResponseEntity<?> responseEntity = restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl("http://localhost").port(port).path("/game/absentGame")
                        .encode().build().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
